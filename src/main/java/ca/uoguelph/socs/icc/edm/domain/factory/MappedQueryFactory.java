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

package ca.uoguelph.socs.icc.edm.domain.factory;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Factory for creating <code>DataStoreQuery</code> objects.  This class is
 * implemented using a <code>CachedMappedFactory<code>.  However, unlike the
 * other mapped factories, the elements that register with this class do not
 * provide a factory.  The factory is generated internally, such that it can
 * provide the necessary information to the <code>DataStore</code> to allow
 * the <code>DataStore</code> to create the <code>DataStoreQuery</code>
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type of the queries to be created
 * @see     ChachedMappedFactory
 */

public final class MappedQueryFactory<T extends Element>
{
	/**
	 * Factory to create queries based on interface and implementation classes.
	 *
	 * @param   <T> The type of the interface class
	 * @param   <X> The type of the implementation class
	 */

	private final class QueryFactory<T extends Element, X extends T> implements ConcreteFactory<DataStoreQuery<T>, DataStore>
	{
		/** The interface type of the query */
		private final Class<T> type;

		/** The implementation type of the query */
		private final Class<X> impl;

		/**
		 * Create the <code>QueryFactory</code>.
		 *
		 * @param  type The class representing the interface for which the queries are
		 *              to be created, not null
		 * @param  impl The class implementing the interface that is to be queried,
		 *              not null
		 */

		public QueryFactory (Class<T> type, Class<X> impl)
		{
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
			return datastore.createQuery (type, impl);
		}
	}

	/** The Log */
	private final Log log;

	/** The interface type of the query */
	Class<T> type;

	/** Factory used to store and build the queries */
	private final CachedMappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> queries;

	/**
	 * Create the <code>MappedQueryFactory</code>.
	 *
	 * @param  type The domain model interface class for which this factory is to
	 *              create queries
	 */

	public MappedQueryFactory (Class<T> type)
	{
		this.log = LogFactory.getLog (MappedQueryFactory.class);

		if (this.type == null)
		{
			this.log.error ("Type is NULL");
			throw new NullPointerException ("Type is NULL");
		}

		this.type = type;
		this.queries = new CachedMappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> (new BaseMappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> ());
	}

	/**
	 * Register an element class.
	 *
	 * @param  impl The implementation class to be used in the query
	 * @see    MappedFactory#registerClass
	 */

	public <X extends T> void registerClass (Class<X> impl)
	{
		this.queries.registerClass (impl, new QueryFactory<T, X> (this.type, impl));
	}

	/**
	 * Get a <code>Set</code> of all of the <code>Element</code> implementations
	 * that have been registered with the factory.
	 *
	 * @return A <code>Set</code> of all of the registered classes
	 * @see    MappedFactory#getRegisteredClasses
	 */

	public Set<Class<? extends Element>> getRegisteredClasses ()
	{
		return this.queries.getRegisteredClasses ();
	}

	/**
	 * Determine if the specified <code>Element</code> implementation class has
	 * been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>Element</code> implementation has
	 *         been registered, <code>false</code> otherwise
	 * @see    MappedFactory#isRegistered
	 */

	public boolean isRegistered (Class<? extends T> impl)
	{
		return this.queries.isRegistered (impl);
	}

	/**
	 * Create a <code>DataStoreQuery</code> for the specified
	 * <code>DataStore</code>.  If the query already exists in the cache,
	 * then the cached copy will be returned, otherwise a new 
	 * <code>DataStoreQuery</code> will be created.
	 * 
	 * @param  datastore             The <code>DataStore</code> for which the
	 *                               query is to be created, not null
	 * @return                       The <code>DataStoreQuery</code>
	 * @throws IllegalStateException if the query implementation class is not 
	 *                               registered
	 * @see    MappedFactory#create
	 */

	public DataStoreQuery<T> create (DataStore datastore)
	{
		if (datastore == null)
		{
			this.log.error ("datastore is NULL");
			throw new NullPointerException ("datastore is NULL");
		}

		Class<? extends Element> impl = (datastore.getProfile ()).getImplClass (this.type);

		if (! this.queries.isRegistered (impl))
		{
			String message = "Element class is not registered: " + impl;

			this.log.error (message);
			throw new IllegalStateException (message);
		}

		return this.queries.create (impl, datastore);
	}

	/**
	 * Remove all of the Queries for the specified <code>DataStore</code> from the
	 * cache.
	 *
	 * @param  datastore The <code>DataStore</code> for which all of the cached
	 *                   queries are to be purged
	 * @see    CachedMappedfactory#remove
	 */

	public void remove (DataStore datastore)
	{
		this.queries.remove (datastore);
	}

	/**
	 * Remove all queries from the cache.
	 *
	 * @see    CachedMappedfactory#flush
	 */

	public void flush ()
	{
		this.queries.flush ();
	}
}
