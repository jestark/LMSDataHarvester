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
 * <code>ActivityBuilder</code>, adding the necessary functionality to
 * handle <code>NamedActivity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     NamedActivity
 */

public class NamedActivityBuilder extends ActivityBuilder
{
	/** The name of the <code>Activity</code> */
	private String name;

	/**
	 * Get an instance of the <code>NamedActivityBuilder</code> for the specified
	 * <code>DomainModel</code> and <code>ActivityType</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>NamedActivityBuilder</code>
	 *                               instance
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

	public static NamedActivityBuilder getInstance (final DomainModel model, ActivityType type)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		return new NamedActivityBuilder (model.getDataStore (), type);
	}

	/**
	 * Create the <code>NamedActivityBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 */

	protected NamedActivityBuilder (final DataStore datastore, final ActivityType type)
	{
		super (datastore, type);
	}

	/**
	 * Create an instance of the <code>NamedActivity</code>.
	 *
	 * @return                       The new <code>Activity</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Activity build ()
	{
		this.log.trace ("build:");

		if (this.course == null)
		{
			this.log.error ("course is NULL");
			throw new IllegalStateException ("course is NULL");
		}

		if (this.name == null)
		{
			this.log.error ("name is NULL");
			throw new IllegalStateException ("name is NULL");
		}

		NamedActivity result = (NamedActivity) this.activityProxy.create ();
		result.setId (this.id);
		result.setType (this.type);
		result.setCourse (this.course);
		result.setName (this.name);

		this.oldActivity = this.activityProxy.insert (this.oldActivity, result);

		return this.oldActivity;
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
	public NamedActivityBuilder load (final Activity activity)
	{
		this.log.trace ("load: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Attempting to load a NULL Activity");
			throw new NullPointerException ();
		}

		super.load (activity);
		this.setName (activity.getName ());

		return this;
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return The name of the <code>Activity</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Activity</code>.
	 *
	 * @param  name                     The name of the <code>Activity</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException if the name is empty
	 */

	public NamedActivityBuilder setName (final String name)
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

		this.name = name;

		return this;
	}
}
