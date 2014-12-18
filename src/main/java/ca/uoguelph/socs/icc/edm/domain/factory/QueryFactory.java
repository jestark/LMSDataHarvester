/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.factory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Factory to create queries based on interface and implementation classes.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the interface class
 * @param   <X> The type of the implementation class
 */

public final class QueryFactory<T extends Element, X extends T> implements ConcreteFactory<DataStoreQuery<T>, DataStore>
{
	private final Class<T> type;
	private final Class<X> impl;

	/**
	 * Create the <code>QueryFactory</code>.
	 *
	 * @param  type The class representing the interface for which the queries are
	 *              to be created, not null
	 * @param  impl The class implementing the interface that is to be queried,
	 *              not null
	 */

	protected QueryFactory (Class<T> type, Class<X> impl)
	{
		if (type == null)
		{
			throw new NullPointerException ("Interface class is NULL");
		}

		if (impl == null)
		{
			throw new NullPointerException ("Implementation Class is NULL");
		}

		this.type = type;
		this.impl = impl;
	}

	/**
	 * Create a query.
	 *
	 * @param  datastore The <code>DataStore</code> for which the query is to be
	 *                   created, not null
	 * @return           The initialized query.
	 */

	@Override
	public DataStoreQuery<T> create (DataStore datastore)
	{
		if (datastore == null)
		{
			throw new NullPointerException ("DataStore is NULL");
		}

		return datastore.createQuery (type, impl);
	}
}
