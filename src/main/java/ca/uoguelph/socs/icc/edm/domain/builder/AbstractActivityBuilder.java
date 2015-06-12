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
import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.element.AbstractActivity;

/**
 * Abstract implementation of the <code>ActivityBuilder</code> interface.  This
 * class acts as the common base for all of the <code>ActivityBuilder</code>
 * instances, handling all of the common <code>Activity</code> components.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Activity</code>
 */

public abstract class AbstractActivityBuilder<T extends Activity> extends AbstractBuilder<T> implements ActivityBuilder<T>
{
	/**
	 * Get an instance of the <code>ActivityBuilder</code> which corresponds to
	 * the specified <code>ActivityType</code>.
	 *
	 * @param  <T>       The <code>ActivityBuilder</code> type to be returned
	 * @param  type      The <code>ActivityType</code> of the
	 *                   <code>Activity</code> instances to be created
	 * @param  datastore The <code>DataStore</code> instance, not null
	 */

	public static final <T extends ActivityBuilder<U>, U extends Activity> T getInstance (final ActivityType type, final DataStore datastore)
	{
		assert type != null : "type is NULL";
		assert datastore != null : "datastore is NULL";
		assert datastore.contains (type) : "ActivityType is not in the DataStore";

		T builder = AbstractBuilder.getInstance (AbstractActivity.getActivityClass (type), datastore);
		((AbstractActivityBuilder) builder).setActivityType (type);

		return builder;
	}

	/**
	 * Create the <code>DefaultActivityBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Activity</code> instance will be
	 *                   inserted
	 */

	public AbstractActivityBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
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
	public void load (final T activity)
	{
		this.log.trace ("load: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Attempting to load a NULL Activity");
			throw new NullPointerException ();
		}

		if (! (this.getActivityType ()).equals (activity.getType ()))
		{
			this.log.error ("Invalid ActivityType:  required {}, received {}", this.getActivityType (), activity.getType ());
			throw new IllegalArgumentException ("Invalid ActivityType");
		}

		super.load (activity);
		this.setCourse (activity.getCourse ());

		this.setPropertyValue ("id", activity.getId ());
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getActivityType ()
	{
		return this.getPropertyValue (ActivityType.class, "type");
	}

	/**
	 * Set the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @param  type The <code>Course</code>, not null
	 */

	private void setActivityType (final ActivityType type)
	{
		this.log.trace ("setActivityType: type={}", type);

		assert type != null : "type is NULL";
		assert this.datastore.contains (type) : "ActivityType is not in the DataStore";

		this.setPropertyValue ("type", type);
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
		return this.getPropertyValue (Course.class, "course");
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException If the <code>Course</code> does not
	 *                                  exist in the <code>DataStore</code>
	 */

	@Override
	public final void setCourse (final Course course)
	{
		this.log.trace ("setCourse: course={}", course);

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

		this.setPropertyValue ("course", course);
	}
}
