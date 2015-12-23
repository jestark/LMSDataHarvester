/* Copyright (C) 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.moodle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import dagger.Component;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Preconditions;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.Semester;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;
import ca.uoguelph.socs.icc.edm.resolver.ARINQuery;
import ca.uoguelph.socs.icc.edm.resolver.Resolver;

/**
 * Extract data for a <code>Course</code> from the Moodle database.  This class
 * is responsible for converting the data stored in the Moodle database to
 * match the schema of the course database.
 * <p>
 * Currently, <code>Enrolment</code> instances are not loaded from the Moodle
 * database as it is difficult to do in a reliable manner.  In order to populate
 * the destination <code>DomainModel</code> with enrolment data, this class uses
 * a set of <code>Registration</code> instances, which must be supplied before
 * the data is extracted from the moodle database.  If a user is encountered,
 * while processing the moodle log, for which there is no corresponding
 * <code>Registration</code>, that user will be assigned to special role, the
 * name of which is the value if the <code>UNKNOWN_ROLE_NAME</code> constant.
 * <p>
 * Moodle does not directly reference <code>SubActivity</code> instances in its
 * log.  To determine when a <code>SubActivity</code> is being referenced, this
 * class requires a set of <code>Matcher</code> instances to be loaded.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityConverter
 * @see     Matcher
 * @see     SubActivityConverter
 */

@AutoFactory
public final class Extractor
{
	/**
	 * Processor for performing the data extraction from the Moodle database.
	 * This class contains the instance specific information needed to perform
	 * an extraction.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	private final class Processor
	{
		/** The log */
		private final Logger log;

		/** The destination <code>DomainModel</code> */
		private final DomainModel dest;

		/** The destination <code>Course</code> instance */
		private final Course course;

		/** The <code>ActivityConverter</code> instance */
		private final ActivityConverter aConverter;

		/** The <code>SubActivityConverter</code> instance */
		private final SubActivityConverter sConverter;

		/**
		 * Create the <code>Processor</code>
		 *
		 * @param  aConverter The <code>ActivityConverter</code>, not null
		 * @param  sConverter The <code>SubActivityConverter</code>, not null
		 * @param  dest       The destination <code>DomainModel</code>, not null
		 */

		private Processor (
				final ActivityConverter aConverter,
				final SubActivityConverter sConverter,
				final DomainModel dest)
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			assert dest != null : "dest is NULL";
			this.dest = dest;

			this.course = Extractor.this.course.getBuilder (this.dest)
				.build ();

			this.aConverter = new ActivityConverter (this.dest, Extractor.this.source);
			this.sConverter = new SubActivityConverter (this.dest, Extractor.this.source, Extractor.this.subActivities);
		}

		/**
		 * Create an <code>Action</code> instance on the destination
		 * <code>DomainModel</code> with the specified name.
		 *
		 * @param  name The name of the <code>Action</code>, not null
		 * @return      The <code>Action</code>
		 */

		public Action getAction (final String name)
		{
			this.log.trace ("getAction: name={}", name);

			assert name != null : "name is NULL";

			return Action.builder (this.dest)
				.setName (name)
				.build ();
		}

		/**
		 * Retrieve the <code>Enrolment</code> from the destination
		 * <code>DomainModel</code>, which is associated with the specified user
		 * ID number.  The user id number should identify a <code>User</code>
		 * instance in the source <code>DomainModel</code>.
		 *
		 * @param  id The <code>DataStore</code> ID for the <code>User</code>,
		 *            not null
		 * @return    The <code>Enrolment</code>
		 */

		public Enrolment getEnrolment (final Long userId)
		{
			this.log.trace ("getEnrolment: userId={}", userId);

			assert userId != null : "userId is NULL";

			User user = Extractor.this.source.getQuery (User.SELECTOR_ID)
				.setValue (User.ID, userId)
				.query ()
				.map (x -> this.importUser (x))
				.orElseGet (() -> this.createUser ());

			if (user.getEnrolment (course) == null)
			{
				User.enrol (user, (Extractor.this.registrations.containsKey (user.getUsername ()))
					? this.createEnrolment (Extractor.this.registrations.get (user.getUsername ()))
					: this.createEnrolment ());
			}

			return user.getEnrolment (course);
		}

		/**
		 * Create an <code>Enrolment</code> instance on the destination
		 * <code>DomainModel</code>, from a <code>Registration</code>.
		 *
		 * @param  registration The <code>Registration</code>, not null
		 * @return              The <code>Enrolment</code>
		 */

		public Enrolment createEnrolment (final Registration registration)
		{
			this.log.trace ("createEnrolment: registration={}", registration);

			assert registration != null : "registration is NULL";

			return Enrolment.builder (this.dest)
				.setCourse (Extractor.this.course)
				.setRole (this.createRole (registration.getRole ()))
				.setUsable (registration.getUsable ())
				.setFinalGrade (registration.getGrade ())
				.build ();
		}

		/**
		 * Create an <code>Enrolment</code> instance on the destination
		 * <code>DomainModel</code> for an unknown <code>User</code>.
		 *
		 * @return The <code>Enrolment</code>
		 */

		public Enrolment createEnrolment ()
		{
			this.log.trace ("createEnrolment:");

			return Enrolment.builder (this.dest)
				.setCourse (Extractor.this.course)
				.setRole (this.createRole (Extractor.UNKNOWN_ROLE_NAME))
				.setUsable (false)
				.build ();
		}

		/**
		 * Create a <code>LogEntry</code> from the provided
		 * <code>MoodleLogData</code> instance.
		 *
		 * @param  entry The <code>MoodleLogData</code> instance, not null
		 * @return       The <code>LogEntry</code>
		 */

		public LogEntry createLogEntry (final MoodleLogData entry)
		{
			this.log.trace ("createLogEntry: entry={}", entry);

			assert entry != null : "entry is NULL";

			Activity activity = this.aConverter.getActivity (this.course, entry);

			return LogEntry.builder (this.dest)
				.setAction (this.getAction (entry.getActionName ()))
				.setNetwork (this.getNetwork (entry.getIpAddress ()))
				.setEnrolment (this.getEnrolment (entry.getUserId ()))
				.setActivity (activity)
				.setSubActivity (this.sConverter.getSubActivity (activity, entry)
						.orElse (null))
				.setTime (entry.getTime ())
				.build ();
		}

		/**
		 * Create an <code>Network</code> instance on the destination
		 * <code>DomainModel</code> from the specified IP address.
		 *
		 * @param  ipAddress The IP address, not null
		 * @return           The <code>Network</code>
		 */

		public Network getNetwork (final String ipAddress)
		{
			this.log.trace ("getNetwork: ipAddress={}", ipAddress);

			assert ipAddress != null : "ipAddress is NULL";

			return Network.builder (this.dest)
				.setName (Extractor.this.resolver.getOrgName (ipAddress))
				.build ();
		}

		/**
		 * Create a <code>Role</code> instance on the destination
		 * <code>DomainModel</code> with the specified name.
		 *
		 * @param  name The name of the <code>Role</code>, not null
		 * @return      The <code>Role</code>
		 */

		public Role createRole (final String name)
		{
			this.log.trace ("createRole: name={}", name);

			assert name != null : "name is NULL";

			return Role.builder (this.dest)
				.setName (name)
				.build ();
		}

		/**
		 * Create a placeholder <code>User</code> instance on the destination
		 * <code>DomainModel</code>.
		 *
		 * @return The <code>User</code>
		 */

		public User createUser ()
		{
			this.log.trace ("createuser:");

			return User.builder (this.dest)
				.setFirstname (NULL_USER_FIRSTNAME)
				.setLastname (NULL_USER_LASTNAME)
				.setUsername (NULL_USER_USERNAME)
				.build ();
		}

		/**
		 * Import a <code>User</code> instance into the destination
		 * <code>DomainModel</code>.
		 *
		 * @param  user The <code>User</code>, not null
		 * @return      The <code>User</code>
		 */

		public User importUser (final User user)
		{
			this.log.trace ("importUser: user={}", user);

			assert user != null : "user is NULL";

			return user.getBuilder (this.dest)
				.build ();
		}
	}

	/**
	 * Dagger component to create the Extractor.  The component currently
	 * declared a hard dependency on the <code>ARINQuery</code> implementation
	 * for the <code>Resolver</code>.  Ideally this would be handled in a more
	 * independent way.
	 */

	@Component (modules = {ARINQuery.ARINQueryModule.class})
	@Singleton
	public static interface ExtractorComponent
	{
		/**
		 * Get a reference to the <code>ExtractorFactory</code>.
		 *
		 * @return The <code>ExtractorFactory</code>
		 */

		public abstract ExtractorFactory getExtractorFactory ();
	}

	/** The CSV dialect to parse */
	private static final CSVFormat FORMAT;

	/** The username for the NULL (unknown) <code>User</code> */
	private static final String NULL_USER_USERNAME;

	/** The first name for the null (unknown) <code>User</code> */
	private static final String NULL_USER_FIRSTNAME;

	/** The last name for the null (unknown) <code>User</code> */
	private static final String NULL_USER_LASTNAME;

	/** The name of the <code>Role</code> to assign to unknown <code>user</code> instances */
	private static final String UNKNOWN_ROLE_NAME;

	/** Dagger Component to get Extractor instances */
	private static final ExtractorComponent COMPONENT;

	/** The Log*/
	private final Logger log;

	/** The IP address to <code>Network</code> resolver */
	private final Resolver resolver;

	/** The source <code>DomainModel</code> */
	private final DomainModel source;

	/** <code>Activity</code> to <code>SubActivity</code> mapping */
	private final Map<Class<? extends Activity>, List<Matcher>> subActivities;

	/** username to <code>Registration</code> mapping */
	private final Map<String, Registration> registrations;

	/** The <code>Course</code> to process */
	private @Nullable Course course;

	/**
	 * Static intializer to set the constants, and to create the Dagger
	 * Component instance.
	 */

	static
	{
		FORMAT = CSVFormat.EXCEL.withNullString("#N/A");
		NULL_USER_USERNAME = "@@NONE@@";
		NULL_USER_FIRSTNAME = "NULL";
		NULL_USER_LASTNAME = "USER";
		UNKNOWN_ROLE_NAME = "UNKNOWN";

		COMPONENT = DaggerExtractor_ExtractorComponent.create ();
	}

	/**
	 * Create the <code>Extractor</code>.
	 *
	 * @param  source The source <code>DomainModel</code>, not null
	 * @return The <code>Extractor</code>
	 */

	public static Extractor create (final DomainModel source)
	{
		Preconditions.checkNotNull (source, "source");

		return Extractor.COMPONENT.getExtractorFactory ()
			.create (source);
	}

	/**
	 * Create the <code>Extractor</code>.
	 *
	 * @param  resolver The <code>Resolver</code>, not null
	 * @param  source   The source <code>DomainModel</code>, not null
	 */

	protected Extractor (final @Provided Resolver resolver, final DomainModel source)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		assert resolver != null : "resolver is NULL";
		assert source != null : "source is NULL";

		this.resolver = resolver;
		this.source = source;

		this.registrations = new HashMap<> ();
		this.subActivities = new HashMap<> ();

		this.registrations.put (Extractor.NULL_USER_USERNAME,
			Registration.create (Extractor.UNKNOWN_ROLE_NAME,
				Extractor.NULL_USER_USERNAME, false, null));
	}

	/**
	 * Remove the <code>Course</code> and all of the <code>Registration</code>
	 * instances from the <code>Extractor</code>.
	 *
	 * @return This <code>Extractor</code>
	 */

	public Extractor clearRegistrations ()
	{
		this.log.trace ("clearRegistrations:");

		this.course = null;
		this.registrations.clear ();

		return this;
	}

	/**
	 * Clear the <code>Extractor</code>.  This method removes the
	 * <code>Course</code> and all of the <code>Registration</code> and
	 * <code>Matcher</code> instances from the <code>Extractor</code>.
	 *
	 * @return This <code>Extractor</code>
	 */

	public Extractor clear ()
	{
		this.log.trace ("clear:");

		this.course = null;
		this.registrations.clear ();
		this.subActivities.clear ();

		return this;
	}

	/**
	 * Close the <code>Extractor</code>.  This method removes the
	 * <code>Course</code>, all of the <code>Registration</code> and
	 * <code>Matcher</code> instances from the <code>Extractor</code>, then
	 * closes the <code>DomainModel</code>.
	 */

	public void close ()
	{
		this.log.trace ("close:");

		this.course = null;
		this.registrations.clear ();
		this.subActivities.clear ();
		this.source.close ();
	}

	/**
	 * Get the <code>Course</code> to be processed.
	 *
	 * @return The <code>Course</code> to be processed
	 */

	public Optional<Course> getCourse ()
	{
		return Optional.ofNullable (this.course);
	}

	/**
	 * Set the <code>Course</code> to be processed.  This method will
	 * retrieve the <code>Course</code> to be processed from the source
	 * <code>DomainModel</code> based on its <code>DataStore</code> ID.
	 *
	 * @param id The <code>DataStore</code> ID of the <code>Course</code>,
	 *           not null
	 * @return   This <code>Extractor</code>
	 */

	public Extractor setCourse (final Long id)
	{
		this.log.trace ("setCourse: id={}", id);

		this.course = this.source.getQuery (Course.SELECTOR_ID)
			.setValue (Course.ID, Preconditions.checkNotNull (id, "id"))
			.query ()
			.orElseThrow (() -> new IllegalStateException ("Course not found"));

		return this;
	}

	/**
	 * Set the <code>Course</code> to be processed.  This method will
	 * retrieve the <code>Course</code> to be processed from the source
	 * <code>DomainModel</code> based on its name and time of offering.
	 *
	 * @param  name     The name of the <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 * @return          This <code>Extractor</code>
	 */

	public Extractor setCourse (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("setCourse: name={}, semester={}, year={}", name, semester, year);

		this.course = this.source.getQuery (Course.SELECTOR_OFFERING)
			.setValue (Course.NAME, Preconditions.checkNotNull (name, "name"))
			.setValue (Course.YEAR, Preconditions.checkNotNull (year, "name"))
			.setValue (Course.SEMESTER, Preconditions.checkNotNull (semester, "name"))
			.query ()
			.orElseThrow (() -> new IllegalStateException ("Course not found"));

		return this;
	}

	/**
	 * Get a <code>List</code> containing all of the <code>Registration</code>
	 * instances in the <code>Extractor</code>
	 *
	 * @return A <code>List</code> of <code>Registration</code> instances
	 */

	public List<Registration> getRegistrations ()
	{
		return this.registrations.entrySet ()
			.stream ()
			.map (x -> x.getValue ())
			.collect (Collectors.collectingAndThen (Collectors.toList (), Collections::unmodifiableList));
	}

	/**
	 * Add a <code>Registration</code> instance to the <code>Extractor</code>.
	 * This method creates a <code>Registration</code> instance using the
	 * supplied username and role, and adds it to the <code>Extractor</code>.
	 * The <code>Registration</code> is created with usable set to
	 * <code>false</code> and the final grade set to <code>null</code>.
	 * <p>
	 * A pre-existing <code>Registration</code> instance will be replaced with
	 * the new <code>Registration</code> instance.
	 *
	 * @param  role The name of the user's <code>Role</code>, not null
	 * @param  user The user's username, not null
	 * @return      This <code>Extractor</code>
	 */

	public Extractor addRegistration (final String role, final String user)
	{
		this.log.trace ("addRegistration: role={}, user={}", role, user);

		Preconditions.checkNotNull (role, "role");
		Preconditions.checkNotNull (role, "user");

		this.registrations.put (user, Registration.create (role, user, false, null));

		return this;
	}

	/**
	 * Add a <code>Registration</code> instance to the <code>Extractor</code>.
	 * This method creates a <code>Registration</code> instance using the
	 * supplied parameters and adds it to the <code>Extractor</code>.
	 * <p>
	 * A pre-existing <code>Registration</code> instance will be replaced with
	 * the new <code>Registration</code> instance.
	 *
	 * @param  role   The name of the user's <code>Role</code>, not null
	 * @param  user   The user's username, not null
	 * @param  usable Indication if the user's data can be used for research,
	 *                not null
	 * @param  grade  The user's final grade, may be null
	 * @return        This <code>Extractor</code>
	 */

	public Extractor addRegistration (
			final String role,
			final String user,
			final Boolean usable,
			final @Nullable Integer grade)
	{
		this.log.trace ("addRegistration: role={}, user={}, usable={}, grade={}", role, user, usable, grade);

		Preconditions.checkNotNull (role, "role");
		Preconditions.checkNotNull (role, "user");
		Preconditions.checkNotNull (role, "usable");
		Preconditions.checkNotNull (role, "grade");

		this.registrations.put (user, Registration.create (role, user, usable, grade));

		return this;
	}

	/**
	 * Load <code>Registration</code> instances into the <code>Extractor</code>
	 * from the specified CSV file.  The CSV file is expected to be formatted
	 * using the Excel CSV dialect, and to contain fields for: username,
	 * final grade, and usable in that order.  The grade and usable fields may
	 * be null, in which case the grade will be set to <code>null</code> and
	 * usable will be set to <code>false</code>.
	 *
	 * @param  role The name of the user's <code>Role</code>, not null
	 * @param  data The CSV file containing the registration data, not null
	 * @return      This <code>Extractor</code>
	 *
	 * @throws IOException if the CSV parser encounters any errors reading the
	 *                     specified file
	 */

	public Extractor addRegistrations (final String role, final File data) throws IOException
	{
		this.log.trace ("addRegistrations: role={}, data={}", role, data);

		for (CSVRecord rec : CSVParser.parse (data, Charset.forName ("UTF-8"), FORMAT))
		{
			String user = rec.get(0);
			Integer grade = (rec.get (1) != null) ? Integer.valueOf ((int) Math.ceil (Double.valueOf (rec.get (1)))) : null;
			Boolean usable = (rec.get (2) != null) ? true : false;

			this.registrations.put (user, Registration.create (role, user, usable, grade));
		}

		return this;
	}

	/**
	 * Get the <code>List</code> of <code>Matcher</code> instances which are
	 * associated with the specified <code>Activity</code>.  If there are no
	 * <code>Matcher</code> instances associated with the specified
	 * <code>Activity</code> then the <code>List</code> will be empty.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 * @return          The <code>List</code> of <code>Matcher</code>
	 *                  instances
	 */

	public List<Matcher> getMatchers (final Class<? extends Activity> activity)
	{
		return (this.subActivities.containsKey (activity))
			? Collections.unmodifiableList (this.subActivities.get (activity))
			: Collections.unmodifiableList (new ArrayList<> ());
	}

	/**
	 * Add the specified <code>Matcher</code> to its associated
	 * <code>Activity</code>.  Note that this method does not check for
	 * duplicates.  Multiple <code>Matcher</code> instances matching a
	 * given <code>LogEntry</code> will lead to inconsistent results.
	 *
	 * @param  matcher The <code>Matcher</code>, not null
	 * @return         This <code>Builder</code>
	 */

	public Extractor addMatcher (final Matcher matcher)
	{
		this.log.trace ("addMatcher: matcher={}", matcher);

		Preconditions.checkNotNull (matcher, "matcher");

		if (! this.subActivities.containsKey (matcher.getActivityClass ()))
		{
			this.log.trace ("creating entry for activity");
			this.subActivities.put (matcher.getActivityClass (), new ArrayList<> ());
		}

		this.subActivities.get (matcher.getActivityClass ())
			.add (matcher);

		return this;
	}

	/**
	 * Extract the data for a <code>Course</code> from the Moodle
	 * <code>DomainModel</code> into the specified <code>DomainModel</code>.
	 *
	 * @param  dest The destination <code>DomainModel</code>, not null
	 * @return      The destination <code>DomainModel</code>
	 *
	 * @throws IllegalStateException if a <code>Course</code> has not been
	 *                               specified
	 */

	public DomainModel extract (final DomainModel dest)
	{
		this.log.trace ("extract: dest={}", dest);

		Preconditions.checkNotNull (dest, "dest");
		Preconditions.checkState (this.course != null, "course is not set");

		dest.getTransaction ().begin ();

		final Processor processor = new Processor (
				new ActivityConverter (dest, this.source),
				new SubActivityConverter (dest, this.source, this.subActivities),
				dest);

		this.source.getQuery (LogEntry.SELECTOR_COURSE)
			.setValue (LogEntry.COURSE, this.course)
			.stream ()
			.map (x -> (MoodleLogData) x)
			.forEach (x -> processor.createLogEntry (x));

		dest.getTransaction ().commit ();

		return dest;
	}
}
