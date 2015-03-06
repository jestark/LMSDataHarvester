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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance;

import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;

/**
 * Moodle specific implementation of the core <code>Activity</code> data.
 * Rather than have the primary key of the tables containing the module
 * (<code>Activity</code>) data reference the core <code>Activity</code> table
 * (<code>ActivityInstance</code>), the Moodle database has a separate primary
 * key for each module table and stores a reference to that key in the course
 * modules table (core <code>Activity</code> data).  Such a design does not
 * allow the module (<code>Activity</code>) data to be loaded via a join to the
 * core <code>Activity</code> instance data (<code>ActivityInstance</code>).
 * To workaround the limitation imposed by the moodle database, this class
 * extends <code>ActivityInstance</code> to store the reference to the module
 * data so that the <code>MoodleActivityManager</code> can load the
 * <code>Activity</code> data in a second step.
 * <p>
 * Since the Moodle database is intended to be read-only, this class does not
 * implement an <code>ElementFactory</code> or require a builder capable of
 * setting the <code>instanceid</code> as should never be needed.
 *
 * @author  James E. Stark
 * @version 1.1
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance
 * @see     MoodleActivityManager
 */

public class MoodleActivityInstance extends ActivityInstance
{
	/**
	 * Implementation of the <code>ActivityElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>MoodleActivityInstance</code>.
	 */

	private static final class Factory implements ActivityElementFactory
	{
		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  type    The <code>ActivityType</code> of the
		 *                 <code>Activity</code>, not null
		 * @param  course  The <code>Course</code> which is associated with the
		 *                 <code>Activity</code> instance, not null
		 * @param  stealth Indicator if the <code>Activity</code> was added by the
		 *                 system, not null
		 *
		 * @return         The new <code>Activity</code> instance
		 */

		@Override
		public Activity create (ActivityType type, Course course, Boolean stealth)
		{
			return new MoodleActivityInstance (type, course, stealth);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Activity</code>.
		 *
		 * @param  action The <code>Action</code> to which the ID number is assigned,
		 *                not null
		 * @param  id     The ID number assigned to the <code>Action</code>, not null
		 */

		@Override
		public void setId (Activity activity, Long id)
		{
			((MoodleActivityInstance) activity).setId (id);
		}

		/**
		 * Add the instance specific data to the activity.  Note that the data to be
		 * added must not already be a part of another <code>Activity</code>, and the
		 * <code>Activity</code> must not already have data associated with it.
		 *
		 * @param  instance The <code>Activity</code> to which the data is to be
		 *                  added, not null
		 * @param  data     The data to add to the <code>Activity</code>, not null
		 */

		public void setInstaceData (Activity instance, Activity data)
		{
			((MoodleActivityInstance) instance).setActivity (data);
		}

		/**
		 * Add the specified <code>Grade</code> to the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> to which the
		 *                  <code>Grade</code> is to be added, not null
		 * @param  grade    The <code>Grade</code> to add to the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>Grade</code> was
		 *                  successfully added to the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		public boolean addGrade (Activity activity, Grade grade)
		{
			return ((MoodleActivityInstance) activity).addGrade (grade);
		}

		/**
		 * Remove the specified <code>Grade</code> from the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> from which the
		 *                  <code>Grade</code> is to be removed, not null
		 * @param  grade    The <code>Grade</code> to remove from the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>Grade</code> was
		 *                  successfully removed from the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		public boolean removeGrade (Activity activity, Grade grade)
		{
			return ((MoodleActivityInstance) activity).removeGrade (grade);
		}

		/**
		 * Add the specified <code>LogEntry</code> to the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> to which the
		 *                  <code>LogEntry</code> is to be added, not null
		 * @param  entry    The <code>LogEntry</code> to add to the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>LogEntry</code> was
		 *                  successfully added to the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		public boolean addLogEntry (Activity activity, LogEntry entry)
		{
			return ((MoodleActivityInstance) activity).addLog (entry);
		}

		/**
		 * Remove the specified <code>LogEntry</code> from the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> from which the
		 *                  <code>LogEntry</code> is to be removed, not null
		 * @param  entry    The <code>LogEntry</code> to remove from the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>LogEntry</code> was
		 *                  successfully removed from the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		public boolean removeLogEntry (Activity activity, LogEntry entry)
		{
			return ((MoodleActivityInstance) activity).removeLog (entry);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The instance data identifier */
	private Long instanceid;

	/**
	 * Static initializer to register the <code>MoodleActivityInstance</code>
	 * class with the factories.
	 */

	static
	{
		(ActivityFactory.getInstance ()).registerElement (MoodleActivityInstance.class, DefaultActivityBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public MoodleActivityInstance ()
	{
		super ();
		this.instanceid = null;
	}

	/**
	 * Create a new <code>Activity</code> instance.  This constructor is provided
	 * for compatibility with the <code>ActivityInstance</code> superclass.  It
	 * should never actually be called, since the moodle database is read-only.
	 * As such, there is no facility to initialize the <code>instanceid</code>.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  stealth Indicator if the <code>Activity</code> was added by the
	 *                 system, not null
	 */


	public MoodleActivityInstance (ActivityType type, Course course, Boolean stealth)
	{
		super (type, course, stealth);
		this.instanceid = null;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.
	 *
	 * @return A <code>Long</code> containing the identifier
	 */

	public Long getInstanceId ()
	{
		return this.instanceid;
	}

	/**
	 * Set the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.  This
	 * method is intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  instanceid The identifier
	 */

	protected void setInstanceId (Long instanceid)
	{
		this.instanceid = instanceid;
	}
}
