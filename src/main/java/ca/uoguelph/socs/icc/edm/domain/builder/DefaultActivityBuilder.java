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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public abstract class DefaultActivityBuilder extends AbstractBuilder<Activity, ActivityElementFactory> implements ActivityBuilder
{
	/** The logger */
	private final Logger log;

	/** The type of the Activity */
	private ActivityType type;

	/** The course which uses the Activity */
	private Course course;

	/**
	 * Create the <code>DefaultActivityBuilder</code>.
	 *
	 * @param  manager The <code>ActivityManager</code> which the 
	 *                 <code>ActivityBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	public DefaultActivityBuilder (final ManagerProxy<Activity> manager)
	{
		super (Activity.class, ActivityElementFactory.class, manager);

		this.log = LoggerFactory.getLogger (DefaultActivityBuilder.class);
	}

	@Override
	protected Activity buildElement ()
	{
		if (this.course == null)
		{
			this.log.error ("Can not build: The activity's course is not set");
			throw new IllegalStateException ("course not set");
		}

		return null; //this.factory.create (type, course);
	}

	@Override
	protected void postInsert ()
	{
	}

	@Override
	protected void postRemove ()
	{
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.course = null;
		this.type = null;
	}

	/**
	 * Load a <code>Activity</code> instance into the
	 * <code>ActivityBuilder</code>.  This method resets the
	 * <code>ActivityBuilder</code> and initializes all of its parameters from the
	 * specified <code>Activity</code> instance.  The parameters are validated as
	 * they are set.
	 *
	 * @param  activity                 The <code>Activity</code> to load into the
	 *                                  <code>ActivityBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the 
	 *                                  <code>Activity</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Activity activity)
	{
		this.log.trace ("Load Activity: {}", activity);

		super.load (activity);
		this.setCourse (activity.getCourse ());

		// this.type = activity.getType ();
	}

	@Override
	public final ActivityType getActivityType ()
	{
		return this.type;
	}

	@Override
	public final Course getCourse ()
	{
		return this.course;
	}

	@Override
	public final ActivityBuilder setCourse (final Course course)
	{
		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		this.course = course;

		return this;
	}
}
