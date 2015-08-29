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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

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
	/** The associated <code>Course</code> */
	public static final Property<Course> COURSE;

	/** The associated <code>ActivityType</code> */
	public static final Property<ActivityType> TYPE;

	/** The name of the <code>Activity</code> */
	public static final Property<String> NAME;

	/** The <code>LogEntry</code> instances associated with the <code>Activity</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** Select all <code>Activity</code> instances by <code>ActivityType</code> */
	public static final Selector SELECTOR_TYPE;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Activity</code>.
	 */

	static
	{
		COURSE = Property.getInstance (Course.class, "course", false, true);
		TYPE = Property.getInstance (ActivityType.class, "type", false, true);
		NAME = Property.getInstance (String.class, "name", false, true);

		LOGENTRIES = Property.getInstance (LogEntry.class, "logentries", true, false);

		SELECTOR_TYPE = Selector.getInstance (TYPE, false);

		Definition.getBuilder (Activity.class, Element.class)
			.addProperty (NAME, Activity::getName)
			.addRelationship (COURSE, Activity::getCourse, Activity::setCourse)
			.addRelationship (TYPE, Activity::getType, Activity::setType)
			.addRelationship (LOGENTRIES, Activity::getLog, Activity::addLog, Activity::removeLog)
			.addSelector (SELECTOR_TYPE)
			.build ();
	}

	/**
	 * Get an <code>ActivityBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates a <code>ActivityBuilder</code>
	 * on the specified <code>DataStore</code> and initializes it with the
	 * contents of this <code>Activity</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>ActivityBuilder</code>
	 */

	@Override
	public ActivityBuilder getBuilder (final DataStore datastore)
	{
		if (datastore == null)
		{
			throw new NullPointerException ();
		}

		ActivityBuilder builder = new ActivityBuilder (datastore, this.getType ());
		builder.load (this);

		return builder;
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
