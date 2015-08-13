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

import java.util.List;
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.element.ActivityDataMap;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the content of a <code>Course</code>.  All of the
 * content in a <code>Course</code> is represented by a <code>Set</code> of
 * <code>Activity</code> instances.  <code>Activity</code> instances will exist
 * for assignments, quizzes, forums and lessons among many other things.  Some
 * of these will be graded and some will not be graded.  Graded
 * <code>Activity</code> instances will have <code>Grade</code> instances
 * associated with them.
 * <p>
 * Since the <code>Activity</code> interface represents all of the content of a
 * <code>Course</code>, its implementation can be complex.  Each instance of
 * the <code>Activity</code> interface must contain the
 * <code>ActviityType</code>, the associated <code>Course</code>,
 * <code>Grade</code>, and <code>LogEntry</code> instances.  Depending on the
 * <code>ActivityType</code> an <code>Activity</code> instance can contain
 * additional data including, but not limited to, <code>SubActivity</code>
 * instances.  All </code>SubActivity</code> instances must implement the
 * <code>Activity</code> interface, but may return local data where necessary.
 * <p>
 * Instances of the <code>Activity</code> interface have strong dependencies on
 * the associated instances of the <code>ActivityType</code> and
 * <code>Course</code> interfaces.  If an instance of one of these interfaces
 * is deleted, then the associated instance of the <code>Activity</code>
 * interface and its dependants must me deleted as well.  Similarly, instances
 * of the <code>Grade</code> and <code>LogEntry</code> interfaces, along with
 * any <code>SubActivity</code> instances have a strong dependency on the
 * associated instance of the <code>Activity</code> interface, and must be
 * deleted along with the instance of the <code>Activity</code> interface.
 * <p>
 * Once created, with the exception of adding and removing <code>Grade</code>
 * and <code>LogEntry</code> instances (and sub-activities if they exist),
 * <code>Activity</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityBuilder
 * @see     ActivityLoader
 */

public abstract class Activity extends ParentActivity
{
	/** Mapping of <code>ActivityType</code> instances to implementation classes */
	private static final ActivityDataMap activityImpl;

	/** The <code>MetaData</code> definition for the <code>Activity</code> */
	protected static final Definition<Activity> metadata;

	/** The <code>DataStore</code> identifier of the <code>Element</code> */
	public static final Property<Long> ID;

	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityType> TYPE;

	/** The name of the <code>Activity</code> */
	public static final Property<String> NAME;

	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<Grade> GRADES;

	/** The <code>LogEntry</code> instances associated with the <code>Activity</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** The <code>SubActivity</code> instances for the <code>Activity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/** Select the <code>Activity</code> instance by its id */
	public static final Selector<Activity> SELECTOR_ID;

	/** Select all of the <code>Activity</code> instances */
	public static final Selector<Activity> SELECTOR_ALL;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector<Activity> SELECTOR_TYPE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		ID = Property.getInstance (Activity.class, Long.class, "id", false, false);
		COURSE = Property.getInstance (Activity.class, Course.class, "course", false, true);
		TYPE = Property.getInstance (Activity.class, ActivityType.class, "type", false, true);
		NAME = Property.getInstance (Activity.class, String.class, "name", false, true);

		GRADES = Property.getInstance (Activity.class, Grade.class, "grade", true, false);
		LOGENTRIES = Property.getInstance (Activity.class, LogEntry.class, "logentries", true, false);
		SUBACTIVITIES = Property.getInstance (Activity.class, SubActivity.class, "subactivities", true, false);

		SELECTOR_ID = Selector.getInstance (Activity.class, ID, true);
		SELECTOR_ALL = Selector.getInstance (Activity.class, "all", false);
		SELECTOR_TYPE = Selector.getInstance (Activity.class, TYPE, false);

		metadata = Definition.getBuilder (Activity.class, Element.class)
			.addProperty (ID, Activity::getId, Activity::setId)
			.addProperty (NAME, Activity::getName)
			.addRelationship (COURSE, Activity::getCourse, Activity::setCourse)
			.addRelationship (TYPE, Activity::getType, Activity::setType)
			.addRelationship (LOGENTRIES, Activity::getLog, Activity::addLog, Activity::removeLog)
			.addRelationship (TYPE, SELECTOR_TYPE)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();

		Profile.registerMetaData (metadata);

		activityImpl = new ActivityDataMap ();
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends Activity> getActivityClass (final ActivityType type)
	{
		return Activity.activityImpl.getActivityClass (type);
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the
	 * class implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static final void registerImplementation (final String source, final String type, final Class<? extends Activity> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		Activity.activityImpl.registerActivityClass (source, type, impl);
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (Course course);

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected abstract void setType (ActivityType type);
}
