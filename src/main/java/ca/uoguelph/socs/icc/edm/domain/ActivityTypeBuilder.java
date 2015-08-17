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

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

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
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>ActivityTypeBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityTypeBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, ActivityType.class, ActivityTypeBuilder::new);
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActivityTypeBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static ActivityTypeBuilder getInstance (final DomainModel model)
	{
		return ActivityTypeBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Create the <code>ActivityTypeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 * @param  metadata  The <code>MetaData</code> for the
	 *                   <code>Element</code>, not null
	 */

	protected ActivityTypeBuilder (final DataStore datastore, final Creator<ActivityType> metadata)
	{
		super (datastore, metadata);
	}

	/**
	 * Load a <code>ActivityType</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>ActivityType</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  type                     The <code>ActivityType</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>ActivityType</code> instance to be
	 *                                  loaded are not valid
	 */

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

		this.builder.setProperty (ActivityType.ID, type.getId ());
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (ActivityType.NAME);
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

		this.builder.setProperty (ActivityType.NAME, name);
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public ActivitySource getActivitySource ()
	{
		return this.builder.getPropertyValue (ActivityType.SOURCE);
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

		this.builder.setProperty (ActivityType.SOURCE, source);
	}
}
