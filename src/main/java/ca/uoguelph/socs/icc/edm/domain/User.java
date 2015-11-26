/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a user within the domain model.  Classes implementing
 * this interface contain the information required to identify the person who
 * is associated with the data (<code>Enrolment</code>, <code>Grade</code>,
 * and <code>LogEntry</code> instances, etc.) contained within the domain
 * model.  As such, instances of the <code>User</code> interface are treated
 * somewhat specially.
 * <p>
 * In the domain model, <code>User</code> is a root level element, as its
 * existence is not dependent on any other components of the domain model.
 * Only <code>Enrolment</code> depends on <code>User</code>, and that
 * dependency is weak.  The domain model is designed such that
 * <code>User</code> is optional.  However, it would be wise to make sure that
 * any <code>DomainModel</code> instance that does not contain <code>User</code>
 * instances is completely immutable.
 * <p>
 * With the exception of adding and removing <code>Enrolment<code> instances,
 * <code>User</code> instances, once created, are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class User extends Element
{
	/**
	 * Create and modify <code>User</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the necessary functionality to
	 * handle <code>User</code> instances.  The "Firstname" and "Lastname"
	 * fields of existing <code>User</code> instances, may be modified in place.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Builder implements Element.Builder<User>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** Helper to operate on <code>User</code> instances */
		private final Persister<User> persister;

		/** Query instance to look up <code>User</code> instances by <code>Enrolment</code> */
		private final Query<User> enrolmentQuery;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<User> supplier;

		/** The loaded or previously built <code>User</code> instance */
		private User user;

		/** The <code>DataStore</code> id number for the <code>User</code> */
		private Long id;

		/** The firstname of the <code>User</code> */
		private String firstname;

		/** The last name of the <code>User</code> */
		private String lastname;

		/** The username of the <code>User</code> */
		private String username;

		/** The associates <code>Enrolment</code> instances */
		private final Set<Enrolment> enrolments;

		/**
		 * Create an instance of the <code>Builder</code>.
		 *
		 * @param  supplier           Method reference to the constructor of the
		 *                            implementation class, not null
		 * @param  persister          The <code>Persister</code> used to store
		 *                            the <code>User</code>, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  enrolmentQuery     <code>Query</code> to check if an
		 *                            <code>Enrolment</code> instances is
		 *                            already associated with another
		 *                            <code>User</code> instance, not null
		 */

		protected Builder (
				final Supplier<User> supplier,
				final Persister<User> persister,
				final Retriever<Enrolment> enrolmentRetriever,
				final Query<User> enrolmentQuery)
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			this.enrolmentRetriever = enrolmentRetriever;
			this.enrolmentQuery = enrolmentQuery;
			this.persister = persister;
			this.supplier = supplier;

			this.user = null;
			this.id = null;
			this.firstname = null;
			this.lastname = null;
			this.username = null;

			this.enrolments = new HashSet<Enrolment> ();
		}

		/**
		 * Create an instance of the <code>User</code>.
		 *
		 * @return                       The new <code>User</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public User build ()
		{
			this.log.trace ("build:");

			if (this.firstname == null)
			{
				this.log.error ("Attempting to create an User without a First name");
				throw new IllegalStateException ("firstname is NULL");
			}

			if (this.lastname == null)
			{
				this.log.error ("Attempting to create an User without an Last name");
				throw new IllegalStateException ("lastname is NULL");
			}

			if (this.username == null)
			{
				this.log.error ("Attempting to create an User without a username");
				throw new IllegalStateException ("username is NULL");
			}

			if ((this.user == null)
					|| (! this.persister.contains (this.user))
					|| (! this.username.equals (this.user.getUsername ())))
			{
				User result = this.supplier.get ();
				result.setId (this.id);
				result.setFirstname (this.firstname);
				result.setLastname (this.lastname);
				result.setUsername (this.username);

				this.enrolments.forEach (x -> result.addEnrolment (x));

				this.user = this.persister.insert (this.user, result);

				if (! this.user.equalsAll (result))
				{
					this.log.error ("User is already in the datastore with a name of: {} vs. the specified value of: {}", this.user.getName (), result.getName ());
					throw new IllegalArgumentException ("User is already in the datastore with a different name");
				}
			}
			else
			{
				this.user.setFirstname (this.firstname);
				this.user.setLastname (this.lastname);

				this.enrolments.stream ()
					.filter (x -> ! this.user.getEnrolments ().contains (x))
					.forEach (x -> this.user.addEnrolment (x));
			}

			return this.user;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.user = null;
			this.id = null;
			this.firstname = null;
			this.lastname = null;
			this.username = null;

			this.enrolments.clear ();

			return this;
		}

		/**
		 * Load a <code>User</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>User</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  user                     The <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>User</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final User user)
		{
			this.log.trace ("load: user={}", user);

			if (user == null)
			{
				this.log.error ("Attempting to load a NULL User");
				throw new NullPointerException ();
			}

			this.clear ();

			this.user = user;
			this.id = user.getId ();
			this.setUsername (user.getUsername ());
			this.setLastname (user.getLastname ());
			this.setFirstname (user.getFirstname ());

			user.getEnrolments ().forEach (x -> this.addEnrolment (x));

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>User</code>
		 * instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the first name (given name) of the <code>User</code>.
		 *
		 * @return A <code>String</code> containing the given name of the
		 *         <code>User</code>
		 */

		public String getFirstname ()
		{
			return this.firstname;
		}

		/**
		 * Set the first name of the <code>User</code>.
		 *
		 * @param  firstname                The first name of the
		 *                                  <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the first name is empty
		 */

		public Builder setFirstname (final String firstname)
		{
			this.log.trace ("setFirstname: firstname={}", firstname);

			if (firstname == null)
			{
				this.log.error ("The specified first name is NULL");
				throw new NullPointerException ("The specified first name is NULL");
			}

			if (firstname.length () == 0)
			{
				this.log.error ("firstname is an empty string");
				throw new IllegalArgumentException ("firstname is empty");
			}

			this.firstname = firstname;

			return this;
		}

		/**
		 * Get the last name (surname) of the <code>User</code>.
		 *
		 * @return A String containing the surname of the <code>User</code>.
		 */

		public String getLastname ()
		{
			return this.lastname;
		}

		/**
		 * Set the last name of the <code>User</code>.
		 *
		 * @param  lastname                 The last name of the
		 *                                  <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the last name is empty
		 */

		public Builder setLastname (final String lastname)
		{
			this.log.trace ("setLastname: lastname={}", lastname);

			if (lastname == null)
			{
				this.log.error ("The specified last name is NULL");
				throw new NullPointerException ("The specified last name is NULL");
			}

			if (lastname.length () == 0)
			{
				this.log.error ("lastname is an empty string");
				throw new IllegalArgumentException ("lastname is empty");
			}

			this.lastname = lastname;

			return this;
		}

		/**
		 * Get the username for the <code>User</code>.  This will be the
		 * username that the <code>User</code> used to access the LMS from which
		 * the data associated with the <code>User</code> was harvested.  The
		 * username is expected to be unique.
		 *
		 * @return A <code>String</code> containing the username for the
		 *         <code>User</code>
		 */

		public String getUsername ()
		{
			return this.username;
		}

		/**
		 * Set the username of the <code>User</code>.
		 *
		 * @param  username                 The user name of the
		 *                                  <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the user name is empty
		 */

		public Builder setUsername (final String username)
		{
			this.log.trace ("setUsername: username={}", username);

			if (username == null)
			{
				this.log.error ("Specified username is NULL");
				throw new NullPointerException ();
			}

			if (username.length () == 0)
			{
				this.log.error ("Specified username is empty (String has length 0)");
				throw new IllegalArgumentException ("Username is empty");
			}

			this.username = username;

			return this;
		}

		/**
		 * Create an association between the <code>User</code> and the specified
		 * <code>Enrolment</code>.  Note that only one <code>User</code> may be
		 * associated with a given <code>Enrolment</code>.
		 *
		 * @param  enrolment                The <code>Enrolment</code> to be
		 *                                  associated with the
		 *                                  <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If there is already a
		 *                                  <code>User</code> associated with
		 *                                  the <code>Enrolment</code>
		 */

		public Builder addEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("addEnrolment: enrolment={}", enrolment);

			if (enrolment == null)
			{
				this.log.error ("Specified enrolment is NULL");
				throw new NullPointerException ();
			}

			Enrolment add = this.enrolmentRetriever.fetch (enrolment);

			if (add == null)
			{
				this.log.error ("Specified Enrolment does not exist in the DataStore");
				throw new IllegalArgumentException ("Enrolment not in DataStore");
			}

			if (! this.enrolmentQuery.setValue (User.ENROLMENTS, add).queryAll ().isEmpty ())
			{
				this.log.error ("The Enrolment is already assigned to another user");
				throw new IllegalArgumentException ("The Enrolment is already assigned to another User");
			}

			this.enrolments.add (add);

			return this;
		}

		/**
		 * Break an association between the <code>User</code> and the specified
		 * <code>Enrolment</code>.  To break an association between the
		 * <code>User</code> and the specified <code>Enrolment</code>, both
		 * the <code>User</code> and <code>Enrolment</code> must be exist in
		 * the <code>DataStore</code> associated with the <code>Builder</code>
		 * instance that is to break the association. Furthermore, there must be
		 * an existing association between the <code>User</code> and the
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment                The <code>Enrolment</code> to remove
		 *                                  from the <code>User</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If there is no association between
		 *                                  the <code>User</code> and the
		 *                                  <code>Enrolment</code>
		 */

		public Builder removeEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("removeEnrolment: enrolment={}", enrolment);

			if (enrolment == null)
			{
				this.log.error ("Specified enrolment is NULL");
				throw new NullPointerException ();
			}

			Enrolment del = this.enrolmentRetriever.fetch (enrolment);

			if (del == null)
			{
				this.log.error ("Specified Enrolment does not exist in the DataStore");
				throw new IllegalArgumentException ("Enrolment not in DataStore");
			}

			this.enrolments.remove (del);

			return this;
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>User</code> */
	protected static final MetaData<User> METADATA;

	/** The <code>DataStore</code> identifier of the <code>User</code> */
	public static final Property<User, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>User</code> */
	public static final Property<User, DomainModel> MODEL;

	/** The first name of the <code>User</code> */
	public static final Property<User, String> FIRSTNAME;

	/** The last name of the <code>User</code> */
	public static final Property<User, String> LASTNAME;

	/** The username of the <code>User</code> */
	public static final Property<User, String> USERNAME;

	/** The <code>Enrolment</code> instances associated with the <code>User</code> */
	public static final Property<User, Enrolment> ENROLMENTS;

	/** Select the <code>User</code> instance by its id */
	public static final Selector<User> SELECTOR_ID;

	/** Select all of the <code>User</code> instances */
	public static final Selector<User> SELECTOR_ALL;

	/** Select the <code>User</code> instance for a <code>Enrolment</code> */
	public static final Selector<User> SELECTOR_ENROLMENTS;

	/** Select an <code>User</code> instance by its username */
	public static final Selector<User> SELECTOR_USERNAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>User</code>.
	 */

	static
	{
		ID = Property.of (User.class, Long.class, "id",
				User::getId, User::setId);

		MODEL = Property.of (User.class, DomainModel.class, "domainmodel",
				User::getDomainModel, User::setDomainModel);

		FIRSTNAME = Property.of (User.class, String.class, "firstname",
				User::getFirstname, User::setFirstname,
				Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		LASTNAME = Property.of (User.class, String.class, "lastname",
				User::getLastname, User::setLastname,
				Property.Flags.REQUIRED, Property.Flags.MUTABLE);

		USERNAME = Property.of (User.class, String.class, "username",
				User::getUsername, User::setUsername,
				Property.Flags.REQUIRED);

		ENROLMENTS = Property.of (User.class, Enrolment.class, "enrolments",
				User::getEnrolments, User::addEnrolment, User::removeEnrolment,
				Property.Flags.MUTABLE);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_USERNAME = Selector.of (Selector.Cardinality.SINGLE, USERNAME);
		SELECTOR_ENROLMENTS = Selector.of (Selector.Cardinality.SINGLE, ENROLMENTS);

		SELECTOR_ALL =  Selector.builder (User.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (User.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (FIRSTNAME)
			.addProperty (LASTNAME)
			.addProperty (USERNAME)
			.addRelationship (ENROLMENTS, Enrolment.METADATA, SELECTOR_ENROLMENTS)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_USERNAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>Builder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>User</code>
	 */

	protected User ()
	{
		super ();
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("firstname", this.getFirstname ())
			.add ("lastname", this.getLastname ())
			.add ("username", this.getUsername ());
	}

	/**
	 * Compare two <code>User</code> instances to determine if they are
	 * equal.  The <code>User</code> instances are compared based upon the
	 * this ID number and the username.
	 *
	 * @param  obj The <code>User</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>User</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof User)
			&& Objects.equals (this.getUsername (), ((User) obj).getUsername ());
	}

	/**
	 * Compare two <code>User</code> instances to determine if they are equal
	 * using all of the instance fields.  For <code>User</code> the
	 * <code>equals</code> methods excludes the mutable fields from the
	 * comparison.  This methods compares two <code>User</code> instances
	 * using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 *
	 * @return         <code>True</code> if the two <code>User</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof User)
			&& Objects.equals (this.getUsername (), ((User) element).getUsername ())
			&& Objects.equals (this.getFirstname (), ((User) element).getFirstname ())
			&& Objects.equals (this.getLastname (), ((User) element).getLastname ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>User</code> instance.
	 * The hash code is computed based upon the following fields ID number and
	 * the username.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getUsername ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>User</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>User</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return User.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return User.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code>
	 * on the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>User</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return User.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public abstract String getFirstname ();

	/**
	 * Set the first name of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  firstname The first name, not null
	 */

	protected abstract void setFirstname (String firstname);

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public abstract String getLastname ();

	/**
	 * Set the last name of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  lastname The last name, not null
	 */

	protected abstract void setLastname (String lastname);

	/**
	 * Get the username for the <code>User</code>.  This will be the username
	 * that the <code>User</code> used to access the LMS from which the data
	 * associated with the <code>User</code> was harvested.  The username is
	 * expected to be unique.
	 *
	 * @return A <code>String</code> containing the username for the
	 *         <code>User</code>
	 */

	public abstract String getUsername ();

	/**
	 * Set the username of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  username The username, not null
	 */

	protected abstract void setUsername (String username);

	/**
	 * Get the full name of the <code>User</code>.  This method will return a
	 * concatenation of the <code>firstname</code> and <code>lastname</code> of
	 * the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the name of the user.
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Enrolment</code> instance for the <code>User</code> in the
	 * specified <code>Course</code>.
	 *
	 * @param  course The <code>Course</code> for which the
	 *                <code>Enrolment</code> instance is to be retrieved
	 * @return        The <code>Enrolment</code> instance for the
	 *                <code>User</code> in the specified <code>Course</code>,
	 *                or null
	 */

	public abstract Enrolment getEnrolment (Course course);

	/**
	 * Get the <code>Set</code> of <code>Enrolment<code> instances which are
	 * associated with this <code>User</code>.  If there are no associated
	 * <code>Enrolment</code> instances, then the <code>Set</code> will be
	 * empty.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>User</code> instance.  This method is intended
	 * to be used to initialize a new <code>User</code> instance.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (Set<Enrolment> enrolments);

	/**
	 * Add an <code>Enrolment</code> to the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add
	 * @return           <code>True</code> if the enrolment was added to the
	 *                   <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove an <code>Enrolment</code> from the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove
	 * @return           <code>True</code> if the enrolment was removed from
	 *                   the <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

