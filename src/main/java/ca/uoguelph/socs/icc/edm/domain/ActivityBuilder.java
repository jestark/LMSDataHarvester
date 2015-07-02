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
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  type      The <code>ActivityType</code>, not null
	 *
	 * @return           The <code>ActivityBuilder</code> instance
	 */

	public static ActivityBuilder getInstance (final DataStore datastore, ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";
		assert datastore.contains (type) : "type is NULL";

		return AbstractActivityBuilder.getInstance (datastore, type, ActivityBuilder::new);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Activity</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  activity  The <code>Activity</code>, not null
	 *
	 * @return           The <code>ActivityBuilder</code> instance
	 */

	public static ActivityBuilder getInstance (final DataStore datastore, Activity activity)
	{
		assert datastore != null : "datastore is NULL";
		assert activity != null : "activity is NULL";

		ActivityBuilder builder = ActivityBuilder.getInstance (datastore, activity.getType ());
		builder.load (activity);

		return builder;
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code> and <code>ActivityType</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  type  The <code>ActivityType</code>, not null
	 *
	 * @return       The <code>ActivityBuilder</code> instance
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

		return ActivityBuilder.getInstance (model.getDataStore (), type);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Activity</code>.
	 *
	 * @param  model    The <code>DomainModel</code>, not null
	 * @param  activity The <code>Activity</code>, not null
	 *
	 * @return          The <code>ActivityBuilder</code> instance
	 */

	public static ActivityBuilder getInstance (final DomainModel model, Activity activity)
	{
		if (activity == null)
		{
			throw new NullPointerException ("activity is NULL");
		}

		ActivityBuilder builder = ActivityBuilder.getInstance (model, activity.getType ());
		builder.load (activity);

		return builder;
	}

	/**
	 * Create the <code>NamedActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected ActivityBuilder (final DataStore datastore, final Builder<Activity> builder)
	{
		super (datastore, builder);
	}

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
		return this.builder.getPropertyValue (Activity.Properties.NAME);
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

		this.builder.setProperty (Activity.Properties.NAME, name);
	}
}
