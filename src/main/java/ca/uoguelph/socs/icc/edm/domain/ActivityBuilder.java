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

package ca.uoguelph.socs.icc.edm.domain;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

/**
 * Create <code>Activity</code> instances.  This class extends
 * <code>AbstractActivityBuilder</code>, adding the necessary functionality to
 * handle named <code>Activity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Activity
 */

public final class ActivityBuilder extends AbstractActivityBuilder<Activity>
{
	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DataStore</code> and <code>ActivityType</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if there is no <code>Activity</code> class
	 *                               registered for the specified
	 *                               <code>ActivityType</code>
	 */

	public static ActivityBuilder getInstance (final DataStore datastore, ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";
		assert datastore.contains (type) : "type is NULL";

		Class<? extends Activity> aclass = NamedActivity.getActivityClass (type);

		if (aclass == null)
		{
			throw new IllegalStateException ("No registered Activity class for to specified ActivityType");
		}

		return AbstractActivityBuilder.getInstance (datastore, datastore.getProfile ().getCreator (Activity.class, aclass), type, ActivityBuilder::new);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code> and <code>ActivityType</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if there is no <code>Activity</code> class
	 *                               registered for the specified
	 *                               <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityBuilder getInstance (final DomainModel model, ActivityType type)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		if (! model.contains (type))
		{
			throw new IllegalArgumentException ("type is not in the datastore");
		}

		return ActivityBuilder.getInstance (AbstractBuilder.getDataStore (model), type);
	}

	/**
	 * Create the <code>NamedActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 */

	protected ActivityBuilder (final DataStore datastore, final Creator<Activity> metadata)
	{
		super (datastore, metadata);
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
	public void load (final Activity activity)
	{
		this.log.trace ("load: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Attempting to load a NULL Activity");
			throw new NullPointerException ();
		}

		super.load (activity);
		this.setName (activity.getName ());
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return The name of the <code>Activity</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Activity.NAME);
	}

	/**
	 * Set the name of the <code>Activity</code>.
	 *
	 * @param  name                     The name of the <code>Activity</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException if the name is empty
	 */

	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("Attempting to set a NULL name");
			throw new NullPointerException ();
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.builder.setProperty (Activity.NAME, name);
	}
}
