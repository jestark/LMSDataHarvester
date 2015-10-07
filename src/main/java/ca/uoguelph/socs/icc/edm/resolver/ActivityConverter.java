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

package ca.uoguelph.socs.icc.edm.resolver;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections4.keyvalue.MultiKey;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.NamedActivity;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Convert <code>Activity</code> data in the moodle log to something sane.
 * This class sits between a Moodle database instance and the destination
 * <code>DataStore</code> and processes <code>Activity</code> instances, which
 * are associated with <code>LogEntry</code> instances, into a form that is
 * valid for the destination <code>DataStore</code>.
 * <p>
 * The <code>Activity</code> component of a Moodle log entry consists of two
 * pieces of information:  The module (stored as a test string), and the
 * course-module id (stored as an integer).  These two pieces of data exist in
 * the Moodle log in the following combinations:
 *
 * <ol>
 *   <li> The entry has a non-zero course-module id, which exists with a
 *        matching module type
 *   <li> The entry has a non-zero course-module id, which does not exist (it
 *        was probably deleted at some point)
 *   <li> The entry has a non-zero course-module id, which exists with a
 *        different module type
 *   <li> The entry has a course-module id of zero, with a module that does not
 *        exist in the course (and may not have an entry in the modules table)
 *   <li> The entry has a course-module id of zero, with a module that does
 *        exist in the course
 * </ol>
 * <p>
 * The first case is the normal and expected behaviour.  In this case (which
 * should be the majority of the log) the activity information is passed though
 * to the output <code>LogEntry</code>.  For the second case, this class will
 * create a "dummy" <code>Activity</code> to stand in for the missing data
 * using the name specified in the <code>MISSING_ACTIVITY_NAME</code> constant.
 * With the dummy <code>Activity</code> all of the new <code>LogEntry</code>
 * instances will point to the correct activity even though it has no useful
 * data.  For the third case, the class detects that the module data is
 * incorrect, and ignores the module data in favour of the pre-existing
 * <code>Activity</code> instance
 * <p>
 * For the two remaining cases, the course-module ID is zero (and the
 * <code>Activity</code>) does not exist.  Moodle records events in the log
 * with an ID of zero for a number of modules which have no entries in the
 * course-modules table.  Furthermore a few of the modules do not exist (and
 * do not have entries in the modules table).  In this situation new
 * <code>ActivityType</code> instances are added to the destination
 * <code>DataStore</code> to match the text in the module field of the Moodle
 * log.  The Moodle modules table is ignored and the log is assumed to be
 * authoritative.  Stealth (generic) <code>Activity</code> instances are
 * created against these <code>ActivityType</code> instances and all
 * <code>LogEntry</code> instances for <code>ActivityType</code> will point to
 * the single stealth <code>Activity</code> instance.
 * <p>
 * For the final case, it appears that the <code>Action</code> in question was
 * not associated specifically with a particular instance of the recored
 * module.  In this case a new <code>Activity</code> instance, of the
 * appropriate type, will be created using the name specified in the
 * <code>NULL_ACTIVITY_NAME</code> constant.  All <code>LogEntry</code>
 * instances which match the module in question with a course-module id of zero
 * will point to the single <code>Activity</code> instance.
 * <p>
 * Moodle's complete lack of referential integrity presents some additional
 * challenges that this class must address.  Since the course-module id and
 * module name may point to entries which do not exist in their respective
 * tables, none of the <code>Activity</code> data can be loaded with the log
 * via a join.  Furthermore, since this class needs to fabricate
 * <code>Activity</code> instances it must track the course-module id number.
 * As a result, this class must be responsible for loading
 * <code>Activity</code> instances from the Moodle database.
 * <p>
 * Loading <code>Activity</code> instances from the Moodle database presents
 * its own challenge.  The course-module id in the Moodle log points to the
 * course-modules table, which contains a reference to the course the module
 * and the ID of the entry containing the data in the table in a separate set
 * of module data tables (which are different for each module).  Each module
 * data table has its own primary key, and does not share an identifier with
 * the course-modules table.  As a result, a given ID can map to multiple
 * different tables containing <code>Activity</code> data.  To handle the
 * problem of multiple <code>Activity</code> instances sharing an ID number, a
 * multi-stage load must be used.  To perform the multi-stage load, this class
 * must load an instance of <code>MoodleActivity</code> from the Moodle
 * database.  <code>MoodleActivity</code> will then transparently load the
 * actual <code>Activity</code> data on demand.  Since
 * <code>MoodleActivity</code> is not registered to any
 * <code>ActivityType</code> it must be loaded with a specially constructed
 * <code>Query</code> instance.
 * <p>
 * By their nature, <code>Activity</code> instances are not uniquely
 * identifiable, meaning that multiple identical <code>Activity</code>
 * instances are valid, including any unintentional duplicates.  The
 * <code>ActivityBuilder</code> and the <code>DataStore</code> do attempt to
 * prevent unintentional duplicate <code>Activity</code> instances.  However,
 * the duplication prevention will only work for the <code>Activity</code>
 * instances that were loaded from the Moodle database, because the associated
 * <code>MoodleActivity</code> instance acts as a reference.  All of the
 * <code>Activity</code> instances created by this class are intended to be
 * unique, and the <code>ActivityBuilder</code> can not prevent them from being
 * duplicated.  Therefore this class needs to cache all of the
 * <code>Activity</code> instances that it creates.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.element.MoodleActivity
 */

public final class ActivityConverter
{
	/** The name to assign to missing <code>Activity</code> instances */
	public static final String MISSING_ACTIVITY_NAME = "-=- MISSING ACTIVITY -=-";

	/** The name to assign to null </code>Activity</code> instances  */
	public static final String NULL_ACTIVITY_NAME = "-=- NO ACTIVITY SPECIFIED -=-";

	/** The log */
	private final Logger log;

	/** The destination <code>DomainModel</code> */
	private final DomainModel dest;

	/** Query for loading <code>Activity</code> instances */
	private final Query<ActivityReference> idQuery;

	/** Builder to create the <code>ActivityType</code> instances */
	private final ActivityTypeBuilder typeBuilder;

	/** Cache of the keys associated with the ID's */
	private final Map<Long, MultiKey<Object>> keys;

	/** Cache of <code>Activity</code> instances */
	private final Map<MultiKey<Object>, Activity> cache;

	/**
	 * Create the <code>ActivityConverter</code>.
	 *
	 * @param  dest   The destination <code>DomainModel</code>, not null
	 * @param  source The source <code>DomainModel</code>, not null
	 */

	public ActivityConverter (final DomainModel dest, final DomainModel source)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.dest = dest;

		this.idQuery = source.getQuery (ActivityReference.class, ActivityReference.SELECTOR_ID);

		this.typeBuilder = ActivityType.builder (this.dest)
			.setActivitySource (ActivitySource.builder (this.dest)
					.setName ("moodle")
					.build ());

		this.keys = new HashMap<> ();
		this.cache = new HashMap<> ();
	}

	/**
	 * Create a generic <code>NamedActivity</code>.
	 *
	 * @param  type   The <code>ActivityType</code>, not null
	 * @param  course The <code>Course</code>, not null
	 * @param  name   The name of the <code>Activity</code>, not null
	 *
	 * @return        The <code>Activity</code>
	 */

	private Activity buildNamedActivity (final ActivityType type, final Course course, final String name)
	{
		assert type != null : "type is NULL";
		assert course != null : "course is NULL";
		assert name != null : "name is NULL";

		return NamedActivity.builder (this.dest, type)
			.setName (name)
			.setCourse (course)
			.build ();
	}

	/**
	 * Load an <code>Activity</code> instance from the moodle database and
	 * convert it to a format that works with the destination database.
	 *
	 * @param  activityId The <code>DataStore</code> ID of the
	 *                    <code>Activity</code>, not null
	 * @param  module     The name of the module (<code>ActivityType</code>),
	 *                    not null
	 * @param  course     The <code>Course</code>, not null
	 *
	 * @return            The <code>Activity</code>
	 */

	public Activity convert (final Long activityId, final String module, final Course course)
	{
		this.log.trace ("convert: activityId={}, module={}, course={}", activityId, module, course);

		if (activityId == null)
		{
			throw new NullPointerException ("activityId is NULL");
		}

		if (module == null)
		{
			throw new NullPointerException ("module is NULL");
		}

		if (course == null)
		{
			throw new NullPointerException ("course is NULL");
		}

		ActivityType type = this.typeBuilder.setName (module)
			.build ();

		MultiKey<Object> key = this.keys.get (activityId);

		if (key == null)
		{
			key = new MultiKey<Object> (type, activityId);
		}

		if (! this.cache.containsKey (key))
		{
			if (activityId != 0)
			{
				ActivityReference moodleActivity = this.idQuery.setValue (Activity.ID, activityId)
					.query ();

				if (moodleActivity != null)
				{
					this.log.info ("Loaded activity instance for module: {} id: {}", type.getName (), activityId);
					this.cache.put (key, (Activity) moodleActivity.getBuilder (this.dest)
							.build ());
					this.keys.put (activityId, key);
				}
				else
				{
					this.log.info ("Created dummy activity instance for module: {} id: {}", type.getName (), activityId);
					this.cache.put (key,
							this.buildNamedActivity (type, course,
								ActivityConverter.MISSING_ACTIVITY_NAME));
					this.keys.put (activityId, key);
				}
			}
			else
			{
				if (Activity.hasActivityClass (type))
				{
					this.log.info ("Created null activity instance for module: {}", type.getName ());
					this.cache.put (key, this.buildNamedActivity (type, course, ActivityConverter.NULL_ACTIVITY_NAME));
				}
				else
				{
					this.log.info ("Created stealth activity for module: {}", type.getName ());
					this.cache.put (key, Activity.builder (this.dest, type)
						.setCourse (course)
						.build ());
				}
			}
		}

		return this.cache.get (key);
	}
}
