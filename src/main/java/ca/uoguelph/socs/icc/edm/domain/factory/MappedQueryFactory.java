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

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Factory for creating <code>DataStoreQuery</code> objects.  This class
 * requires all of the elements to register with it.  During the registration
 * process this class internally generate a query factory for the element,
 * which will be used to create the <code>DataStoreQuery</code> instances for
 * that element when they are requested.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type of the queries to be created
 */

public final class MappedQueryFactory<T extends Element>
{
	/**
	 * Interface to hide the implementation class for the query factory from the
	 * Map.
	 */

	private interface IntQueryFactory<T extends Element>
	{
		/**
		 * Create a query.
		 *
		 * @param  datastore The <code>DataStore</code> for which the query is to be
		 *                   created, not null
		 * @return           The initialized query.
		 */

		public DataStoreQuery<T> create (DataStore datastore);
	}

	/**
	 * Factory to create queries based on interface and implementation classes.
	 *
	 * @param   <T> The type of the interface class
	 * @param   <X> The type of the implementation class
	 */

	private final class IntQueryFactoryImpl<T extends Element, X extends T> implements IntQueryFactory<T>
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

		public IntQueryFactoryImpl (Class<T> type, Class<X> impl)
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
	private final Logger log;

	/** The interface type of the query */
	private final Class<T> type;

	/** Query factories */
	private final Map<Class<? extends Element>, IntQueryFactory<T>> queries;

	/** <code>DataStoreQuery</code> instance cache */
	private final Map<DataStore, Map<Class<? extends Element>, DataStoreQuery<T>>> cache;

	/**
	 * Create the <code>MappedQueryFactory</code>.
	 *
	 * @param  type The domain model interface class for which this factory is to
	 *              create queries, not null
	 */

	public MappedQueryFactory (Class<T> type)
	{
		this.log = LoggerFactory.getLogger (MappedQueryFactory.class);

		if (type == null)
		{
			this.log.error ("Domain model interface type NULL");
			throw new NullPointerException ();
		}

		this.type = type;

		this.queries = new HashMap<Class<? extends Element>, IntQueryFactory<T>> ();
		this.cache = new HashMap<DataStore, Map<Class<? extends Element>, DataStoreQuery<T>>> ();
	}

	/**
	 * Register an element class.
	 *
	 * @param  impl The implementation class to be used in the query, not null
	 */

	public <X extends T> void registerClass (Class<X> impl)
	{
		this.log.trace ("Registering Element implementation class: {} ({})", impl, this.type);

		if (impl == null)
		{
			this.log.error ("Attempting to register a NULL implementation class");
			throw new NullPointerException ();
		}

		if (this.queries.containsKey (impl))
		{
			this.log.error ("Class has already been registered: {}", impl);
			throw new IllegalArgumentException ("Duplicate class registration");
		}

		this.queries.put (impl, new IntQueryFactoryImpl<T, X> (this.type, impl));
	}

	/**
	 * Get a <code>Set</code> of all of the <code>Element</code> implementations
	 * that have been registered with the factory.
	 *
	 * @return A <code>Set</code> of all of the registered classes
	 */

	public Set<Class<? extends Element>> getRegisteredClasses ()
	{
		return this.queries.keySet ();
	}

	/**
	 * Determine if the specified <code>Element</code> implementation class has
	 * been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>Element</code> implementation has
	 *         been registered, <code>false</code> otherwise
	 */

	public boolean isRegistered (Class<? extends T> impl)
	{
		return this.queries.containsKey (impl);
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
	 */

	public DataStoreQuery<T> create (DataStore datastore)
	{
		this.log.debug ("Creating query for interface {}, on using DataStore {}", this.type, datastore);

		if (datastore == null)
		{
			this.log.error ("Attempting to create a query for a NULL DataStore");
			throw new NullPointerException ();
		}

		Class<? extends Element> impl = (datastore.getProfile ()).getImplClass (this.type);

		if ((! this.cache.containsKey (datastore)) || (! (this.cache.get (datastore)).containsKey (impl)))
		{
			if (! this.queries.containsKey (impl))
			{
				this.log.error ("Element implementation class is not registered: {}", impl);
				throw new IllegalStateException ("Unregistered Element implementation");
			}

			if (! this.cache.containsKey (datastore))
			{
				this.cache.put (datastore, new HashMap<Class<? extends Element>, DataStoreQuery<T>> ());
			}

			(this.cache.get (datastore)).put (impl, (this.queries.get (impl)).create (datastore));
		}

		return (this.cache.get (datastore)).get (impl);
	}

	/**
	 * Remove all of the <code>DataStoreQuery</code> instances for the specified
	 * <code>DataStore</code> from the cache.
	 *
	 * @param  datastore The <code>DataStore</code> for which all of the cached
	 *                   queries are to be purged, not null
	 */

	public void remove (DataStore datastore)
	{
		this.log.trace ("Removing all cached query instances for DataStore: {}", datastore);

		if (datastore == null)
		{
			this.log.error ("Attempting to remove cached queries for a NULL DataStore");
			throw new NullPointerException ();
		}

		this.queries.remove (datastore);
	}

	/**
	 * Remove all queries from the cache.
	 */

	public void flush ()
	{
		this.log.trace ("Flushing cache");
	
		this.queries.clear ();
	}
}
