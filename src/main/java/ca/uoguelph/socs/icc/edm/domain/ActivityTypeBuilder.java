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

import java.util.Set;

import java.util.HashSet;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>ActivityType</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>ActivityType</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityType
 */

public final class ActivityTypeBuilder extends AbstractBuilder<ActivityType>
{
	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>ActivityTypeBuilder</code> instance
	 */

	public static ActivityTypeBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new ActivityTypeBuilder (datastore, AbstractBuilder.getBuilder (datastore, datastore.getElementClass (ActivityType.class)));
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>ActivityType</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  type      The <code>ActivityType</code>, not null
	 *
	 * @return           The <code>ActivityTypeBuilder</code> instance
	 */

	public static ActivityTypeBuilder getInstance (final DataStore datastore, ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";

		ActivityTypeBuilder builder = ActivityTypeBuilder.getInstance (datastore);
		builder.load (type);

		return builder;
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>ActivityTypeBuilder</code> instance
	 */


	public static ActivityTypeBuilder getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return ActivityTypeBuilder.getInstance (model.getDataStore ());
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>ActivityType</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  type  The <code>ActivityType</code>, not null
	 *
	 * @return       The <code>ActivityTypeBuilder</code> instance
	 */

	public static ActivityTypeBuilder getInstance (final DomainModel model, ActivityType type)
	{
		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		ActivityTypeBuilder builder = ActivityTypeBuilder.getInstance (model);
		builder.load (type);

		return builder;
	}

	/**
	 * Create the <code>ActivityTypeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected ActivityTypeBuilder (final DataStore datastore, final Builder<ActivityType> builder)
	{
		super (datastore, builder);
	}

	@Override
	public void load (final ActivityType type)
	{
		this.log.trace ("load: type={}", type);

		if (type == null)
		{
			this.log.error ("Attempting to load a NULL ActivityType");
			throw new NullPointerException ();
		}

		super.load (type);
		this.setName (type.getName ());
		this.setActivitySource (type.getSource ());

		this.builder.setProperty (ActivityType.Properties.ID, type.getId ());
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (ActivityType.Properties.NAME);
	}

	/**
	 * Set the name of the <code>ActivityType</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>ActivityType</code>, not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.builder.setProperty (ActivityType.Properties.NAME, name);
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public ActivitySource getActivitySource ()
	{
		return this.builder.getPropertyValue (ActivityType.Properties.SOURCE);
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @param  source                   The <code>ActivitySource</code> for the
	 *                                  <code>ActivityType</code>
	 *
	 * @throws IllegalArgumentException If the <code>AcivitySourse</code> does
	 *                                  not exist in the <code>DataStore</code>
	 */

	public void setActivitySource (final ActivitySource source)
	{
		this.log.trace ("setSource: source={}", source);

		if (source == null)
		{
			this.log.error ("source is NULL");
			throw new NullPointerException ("source is NULL");
		}

		if (! this.datastore.contains (source))
		{
			this.log.error ("The specified ActivitySource does not exist in the DataStore: {}", source);
			throw new IllegalArgumentException ("ActivitySource is not in the DataStore");
		}

		this.builder.setProperty (ActivityType.Properties.SOURCE, source);
	}

	/**
	 * Get the <code>Set</code> of <code>Action</code> instances which are
	 * associated with the <code>ActivityType</code>.  If there are no
	 * associated <code>Action</code> instances, then the <code>Set</code>
	 * will be empty.
	 *
	 * @return A <code>Set</code> of <code>Action</code> instances
	 */

	public Set<Action> getActions ()
	{
		return null;
	}

	/**
	 * Create an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.
	 *
	 * @param  action                   The <code>Action</code> to be
	 *                                  associated with the
	 *                                  <code>ActivityType</code>, not null
	 *
	 * @throws IllegalArgumentException If the <code>Action</code> does not
	 *                                  exist in the <code>DataStore</code>
	 */

	public void addAction (final Action action)
	{
	}

	/**
	 * Break an association between the <code>ActivityType</code> and an
	 * <code>Action</code>.
	 *
	 * @param  action                The <code>Action</code> to remove from the
	 *                               <code>ActivityType</code>, not null
	 *
	 * @throws IllegalStateException If there are any log entries containing
	 *                               the <code>ActivityType</code> and the
	 *                               <code>Action</code>
	 */

	public void removeAction (final Action action)
	{
	}
}
