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

import java.util.function.BiFunction;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Abstract builder for <code>Activity</code> instances.  This class acts as
 * the common base for all of the builders which produce <code>Activity</code>
 * instances, handling all of the common <code>Activity</code> components.
 * <p>
 * To create builders for <code>Activity</code> instances, the
 * <code>ActivityType</code> must be supplied when the builder is created.  The
 * <code>ActivityType</code> is needed to determine which <code>Activity</code>
 * implementation class is to be created by the builder.  It is possible to
 * specify an <code>ActivityType</code> which does not match the selected
 * builder.  In this case the builder will be created successfully, but an
 * exception will occur when a field is set in the builder that does not exist
 * in the implementation, or when the implementation is built and a required
 * field is determined to be missing.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Activity</code>
 * @see     Activity
 */

public abstract class AbstractActivityBuilder<T extends Activity> extends AbstractBuilder<T>
{
	/**
	 * Get an instance of the <code>ActivityBuilder</code> which corresponds to
	 * the specified <code>ActivityType</code>.
	 *
	 * @param  <T>                   The <code>Element</code> type of the 
	 *                               builder
	 * @param  <U>                   The type of the builder
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 * @param  create                Method reference to the constructor for
	 *                               the builder
	 *
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 */

	public static <T extends Activity, U extends AbstractActivityBuilder<T>> U getInstance (final DataStore datastore, final ActivityType type, final BiFunction<DataStore, Class<? extends Element>, U> create)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";
		assert create != null : "create is NULL";
		assert datastore.contains (type) : "type is not in the datastore";

		// Exception here because this is the fist time that it is checked
		if (! datastore.isOpen ())
		{
			throw new IllegalStateException ("datastore is closed");
		}

		// Exception here because this is the fist time that it is checked
		if (datastore.getProfile ().getElementClass (Activity.class) == null)
		{
			throw new IllegalStateException ("Element is not available for this datastore");
		}

		U builder = create.apply (datastore, Activity.getActivityClass (type));
		builder.setActivityType (type);

		return builder;
	}

	/**
	 * Create the <code>AbstractActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 */

	protected AbstractActivityBuilder (final DataStore datastore, final Class<? extends Element> element)
	{
		super (datastore, element);
	}

	/**
	 * Load a <code>Activity</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Activity</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
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

		this.builder.setProperty (Activity.ID, activity.getId ());
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public final ActivityType getActivityType ()
	{
		return this.builder.getPropertyValue (Activity.TYPE);
	}

	/**
	 * Set the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @param  type The <code>Course</code>, not null
	 */

	protected void setActivityType (final ActivityType type)
	{
		this.log.trace ("setActivityType: type={}", type);

		assert type != null : "type is NULL";
		assert this.datastore.contains (type) : "ActivityType is not in the DataStore";

		this.builder.setProperty (Activity.TYPE, type);
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public final Course getCourse ()
	{
		return this.builder.getPropertyValue (Activity.COURSE);
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

		this.builder.setProperty (Activity.COURSE, course);
	}
}
