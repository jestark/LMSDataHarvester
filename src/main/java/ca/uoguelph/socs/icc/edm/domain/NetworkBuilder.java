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
 * Create new <code>Network</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Network</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Network
 */

public final class NetworkBuilder extends AbstractBuilder<Network>
{
	/**
	 * Get an instance of the <code>NetworkBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>NetworkBuilder</code> instance
	 */

	public static NetworkBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new NetworkBuilder (datastore, AbstractBuilder.getBuilder (datastore, datastore.getElementClass (Network.class)));
	}

	/**
	 * Get an instance of the <code>NetworkBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Network</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  network   The <code>Network</code>, not null
	 *
	 * @return           The <code>NetworkBuilder</code> instance
	 */

	public static NetworkBuilder getInstance (final DataStore datastore, Network network)
	{
		assert datastore != null : "datastore is NULL";
		assert network != null : "network is NULL";

		NetworkBuilder builder = NetworkBuilder.getInstance (datastore);
		builder.load (network);

		return builder;
	}

	/**
	 * Get an instance of the <code>NetworkBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 *
	 * @return         The <code>NetworkBuilder</code> instance
	 */


	public static NetworkBuilder getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return NetworkBuilder.getInstance (model.getDataStore ());
	}

	/**
	 * Get an instance of the <code>NetworkBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Network</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  network The <code>Network</code>, not null
	 *
	 * @return         The <code>NetworkBuilder</code> instance
	 */

	public static NetworkBuilder getInstance (final DomainModel model, Network network)
	{
		if (network == null)
		{
			throw new NullPointerException ("network is NULL");
		}

		NetworkBuilder builder = NetworkBuilder.getInstance (model);
		builder.load (network);

		return builder;
	}

	/**
	 * Create the <code>NetworkBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected NetworkBuilder (final DataStore datastore, final Builder<Network> builder)
	{
		super (datastore, builder);
	}

	@Override
	public void load (final Network network)
	{
		this.log.trace ("load: network={}", network);

		if (network == null)
		{
			this.log.error ("Attempting to load a NULL Network");
			throw new NullPointerException ();
		}

		super.load (network);
		this.setName (network.getName ());

		this.builder.setProperty (Network.Properties.ID, network.getId ());
	}

	/**
	 * Get the name of the <code>Network</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Network</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Network.Properties.NAME);
	}

	/**
	 * Set the name of the <code>Network</code>.
	 *
	 * @param  name                     The name of the <code>Network</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("Name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.builder.setProperty (Network.Properties.NAME, name);
	}
}
