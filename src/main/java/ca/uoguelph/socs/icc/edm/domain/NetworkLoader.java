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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Load <code>Network</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Network</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class NetworkLoader extends AbstractLoader<Network>
{
	/**
	 * Get an instance of the <code>NetworkLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>NetworkLoader</code>
	 */

	public NetworkLoader getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return new NetworkLoader (model.getDataStore ());
	}

	/**
	 * Create the <code>NetworkLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public NetworkLoader (final DataStore datastore)
	{
		super (Network.class, datastore);
	}

	/**
	 * Retrieve a <code>Network</code> object from the underlying
	 * <code>DataStore</code> based on its name.
	 *
	 * @param  name The name of the <code>Network</code>, not null
	 * @return      A <code>Network</code> object
	 */

	public Network fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Network name is NULL");
			throw new NullPointerException ();
		}

		Query<Network> query = this.fetchQuery (Network.Selectors.NAME);
		query.setProperty (Network.Properties.NAME, name);

		return query.query ();
	}
}
