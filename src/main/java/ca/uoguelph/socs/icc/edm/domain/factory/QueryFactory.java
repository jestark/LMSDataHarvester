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

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

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
 * @version 1.3
 */

public final class QueryFactory
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

	private final class IntQueryFactoryImpl<T extends Element, U extends T> implements IntQueryFactory<T>
	{
		/** The interface type of the query */
		private final Class<T> type;

		/** The implementation type of the query */
		private final Class<U> impl;

		/**
		 * Create the <code>QueryFactory</code>.
		 *
		 * @param  type The class representing the interface for which the queries are
		 *              to be created, not null
		 * @param  impl The class implementing the interface that is to be queried,
		 *              not null
		 */

		public IntQueryFactoryImpl (final Class<T> type, final Class<U> impl)
		{
			assert type != null : "type is NULL";
			assert impl != null : "impl is NULL";

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
		public DataStoreQuery<T> create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return datastore.createQuery (this.type, this.impl);
		}
	}

	/** Singleton instance */
	private static final QueryFactory INSTANCE;

	/** The Log */
	private final Logger log;

	/** Query factories */
	private final Map<Pair<Class<?>, Class<?>>, IntQueryFactory<? extends Element>> factories;
		
	/** <code>DataStoreQuery</code> instance cache */
	private final Map<Triple<DataStore, Class<?>, Class<?>>, DataStoreQuery<? extends Element>> cache;

	/**
	 *  static initializer to create the singleton
	 */
	static
	{
		INSTANCE = new QueryFactory ();
	}

	/**
	 * Get an instance of the <code>QueryFactory</code>.
	 *
	 * @return      The <code>QueryFactory</code> instance
	 */

	public static QueryFactory getInstance ()
	{
		return INSTANCE;
	}

	/**
	 * Create the <code>QueryFactory</code>.
	 *
	 * @param  type The domain model interface class for which this factory is to
	 *              create queries, not null
	 */

	private QueryFactory ()
	{
		this.log = LoggerFactory.getLogger (QueryFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, IntQueryFactory<? extends Element>> ();
		this.cache = new HashMap<Triple<DataStore, Class<?>, Class<?>>, DataStoreQuery<? extends Element>> ();
	}

	/**
	 * Register an element class.  
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The implementation class to be used in the query, not null
	 */

	public <T extends Element, U extends T> void registerClass (final Class<T> type, final Class<U> impl)
	{
		this.log.trace ("Registering Element class: Type: {} Implementation {}", type, impl);

		assert type != null : "type is NULL";
		assert impl != null : "impl is null";

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (type, impl);

		assert (! this.factories.containsKey (factoryKey)) : "Class has already been registered: " + impl.getSimpleName ();

		this.factories.put (factoryKey, new IntQueryFactoryImpl<T, U> (type, impl));
	}

	/**
	 * Create a <code>DataStoreQuery</code> for the specified
	 * <code>DataStore</code>, and <code>Element</code> implementation class.  If
	 * the query already exists in the cache, then the cached copy will be
	 * returned, otherwise a new <code>DataStoreQuery</code> will be created.
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  impl      The implementation class for which the query is to be
	 *                   created, not null
	 * @param  datastore The <code>DataStore</code> for which the query is to be
	 *                   created, not null
	 * @return           The <code>DataStoreQuery</code>
	 */

	@SuppressWarnings("unchecked")
	public <T extends Element> DataStoreQuery<T> create (final Class<T> element, final Class<? extends Element> implclass, final DataStore datastore)
	{
		this.log.debug ("Creating query for interface {}, using implementation {}, on using DataStore {}", type, impl, datastore);

		assert element != null : "element is NULL";
		assert implclass != null : "implclass is NULL";
		assert datastore != null : "datastore is NULL";

		Triple<DataStore, Class<?>, Class<?>> cacheKey = new ImmutableTriple<DataStore, Class<?>, Class<?>> (datastore, type, impl);

		DataStoreQuery<T> query = (DataStoreQuery<T>) this.cache.get (cacheKey);

		// If the Query is not in the cache then create it.
		if (query == null)
		{
			Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (type, impl);

			assert this.queries.containsKey (factoryKey) : "";

			query = ((IntQueryFactory<T>) this.queries.get (factoryKey)).create (datastore);

			this.cache.put (cacheKey, query);
		}

		return query;
	}

	/**
	 * Create a <code>DataStoreQuery</code> for the specified
	 * <code>DataStore</code>.  If the query already exists in the cache,
	 * then the cached copy will be returned, otherwise a new
	 * <code>DataStoreQuery</code> will be created.
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  datastore The <code>DataStore</code> for which the query is to be
	 *                   created, not null
	 * @return           The <code>DataStoreQuery</code>
	 */

	public <T extends Element> DataStoreQuery<T> create (final Class<T> type, final DataStore datastore)
	{
		this.log.debug ("Creating query for interface {}, on using DataStore {}", type, datastore);

		assert type != null : "type is NULL";
		assert datastore != null : "datastore is NULL";

		return this.create (type, (datastore.getProfile ()).getImplClass (type), datastore);
	}

	/**
	 * Remove all of the <code>DataStoreQuery</code> instances for the specified
	 * <code>DataStore</code> from the cache.
	 *
	 * @param  datastore The <code>DataStore</code> for which all of the cached
	 *                   queries are to be purged, not null
	 */

	public void remove (final DataStore datastore)
	{
		this.log.trace ("Removing all cached query instances for DataStore: {}", datastore);

		assert datastore != null : "datastore is NULL";

		for (Triple<DataStore, Class<?>, Class<?>> key : this.cache.keySet ())
		{
			if (datastore.equals (key.getLeft ()))
			{
				this.cache.remove (key);
			}
		}
	}

	/**
	 * Remove all queries from the cache.
	 */

	public void flush ()
	{
		this.log.trace ("Flushing cache");

		this.cache.clear ();
	}
}
