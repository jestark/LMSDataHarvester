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
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Abstract implementation of the <code>ActivityBuilder</code> interface.  This
 * class acts as the common base for all of the <code>ActivityBuilder</code>
 * instances, handling all of the common <code>Activity</code> components.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Activity</code>
 */

public abstract class AbstractActivityBuilder extends AbstractBuilder<Activity> implements ActivityBuilder
{
	/** The type of the Activity */
	protected final ActivityType type;

	/** The course which uses the Activity */
	protected Course course;

	/**
	 * Create the <code>DefaultActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Activity</code> instance will be
	 *                   inserted
	 */

	public AbstractActivityBuilder (final DataStore datastore)
	{
		super (Activity.class, datastore);

		this.type = null; //FIXME
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

		if (! this.datastore.contains (course))
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		this.course = course;

		return this;
	}
}
