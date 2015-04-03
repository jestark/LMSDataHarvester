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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Abstract implementation of the <code>ActivityBuilder</code> interface.  This
 * class acts as the common base for all of the <code>ActivityBuilder</code>
 * instances, handling all of the common <code>Activity</code> components.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Activity</code>
 */

public abstract class AbstractActivityBuilder<T extends ActivityElementFactory> extends AbstractBuilder<Activity, T> implements ActivityBuilder
{
	/** The type of the Activity */
	protected final ActivityType type;

	/** The course which uses the Activity */
	protected Course course;

	/**
	 * Create the <code>DefaultActivityBuilder</code>.
	 *
	 * @param  manager The <code>ActivityManager</code> which the
	 *                 <code>ActivityBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	public AbstractActivityBuilder (final Class<T> factory, final ManagerProxy<Activity> manager)
	{
		super (Activity.class, factory, manager);

		assert this.manager.getArgument () != null : "ActivityType is NULL";

		if (! (this.manager.getManager (ActivityType.class, ActivityTypeManager.class)).contains ((ActivityType) this.manager.getArgument ()))
		{
			this.log.error ("The specified ActivityType does not exist in the DataStore: {}", this.manager.getArgument ());
			throw new IllegalArgumentException ("ActivityType is not in the DataStore");
		}

		this.type = (ActivityType) this.manager.getArgument ();
	}

	@Override
	protected void postInsert ()
	{
		CourseElementFactory factory = AbstractBuilder.getFactory (CourseElementFactory.class, this.course.getClass ());

		factory.addActivity (this.course, this.element);
	}

	@Override
	protected void postRemove ()
	{
		CourseElementFactory factory = AbstractBuilder.getFactory (CourseElementFactory.class, this.course.getClass ());

		factory.removeActivity (this.course, this.element);
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("clear:");

		super.clear ();
		this.course = null;
	}

	/**
	 * Load a <code>Activity</code> instance into the
	 * <code>ActivityBuilder</code>.  This method resets the
	 * <code>ActivityBuilder</code> and initializes all of its parameters from
	 * the specified <code>Activity</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  activity                 The <code>Activity</code> to load into
	 *                                  the <code>ActivityBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Activity</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Activity activity)
	{
		this.log.trace ("load: activity={}", activity);

		super.load (activity);
		this.setCourse (activity.getCourse ());
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getActivityType ()
	{
		return this.type;
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public final Course getCourse ()
	{
		return this.course;
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @return                          This <code>ActivityBuilder</code>
	 * @throws IllegalArgumentException If the <code>Course</code> does not
	 *                                  exist in the <code>DataStore</code>
	 */

	@Override
	public final ActivityBuilder setCourse (final Course course)
	{
		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		if (! (this.manager.getManager (Course.class, CourseManager.class)).contains (course))
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		this.course = course;

		return this;
	}
}
