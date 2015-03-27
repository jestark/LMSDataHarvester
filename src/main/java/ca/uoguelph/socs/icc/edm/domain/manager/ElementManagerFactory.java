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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.lang.ref.WeakReference;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Factory for creating <code>ElementManager</code> objects.  This class
 * requires all of the <code>ElementManager</code> implementations to register
 * a factory object (extending the <code>ManagerFactory</code> interface)
 * capable of instantiating and initializing the <code>ElementManager</code>.
 * <p>
 * As a caching factory, this factory will ensure that the
 * is no more than one instance of a given <code>ElementManager</code>
 * implementation for a given <code>DataStorel</code> at any time.  If the
 * requested <code>Elementmanager</code> already exists then the cached copy
 * will returned, otherwise a new instance will be created.
 *
 * @author  James E. Stark
 * @version 1.3
 * @see     ManagerFactory
 */

final class ElementManagerFactory
{
	/** The logger */
	private final Logger log;

	/** <code>ElementManager</code> factories */
	private final Map<Pair<Class<?>, Class<?>>, ManagerFactory<? extends ElementManager<? extends Element>>> factories;

	/** <code>ElementManager</code> instance Cache */
	private final Map<Triple<DataStore, Class<?>, Class<?>>, WeakReference<ElementManager<? extends Element>>> cache;

	/**
	 * Create the <code>MappedManagerFactory</code>.
	 */

	public ElementManagerFactory ()
	{
		this.log = LoggerFactory.getLogger (ElementManagerFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, ManagerFactory<? extends ElementManager<? extends Element>>> ();
		this.cache = new HashMap<Triple<DataStore, Class<?>, Class<?>>, WeakReference<ElementManager<? extends Element>>> ();
	}

	/**
	 * Register a <code>ElementManager</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementManager</code> when the classes initialize.
	 *
	 * @param  <T>     The <code>ElementManager</code> type being registered
	 * @param  manager The <code>ElementManager</code> interface class, not null
	 * @param  impl    The <code>ElementManager</code> implementation class,
	 *                 not null
	 * @param  factory The <code>ManagerFactory</code> used to create the
	 *                 <code>ElementManager</code> implementation class, not
	 *                 null
	 */

	public <T extends ElementManager<? extends Element>> void registerFactory (final Class<T> manager, final Class<? extends T> impl, final ManagerFactory<T> factory)
	{
		this.log.trace ("registerFactory: manager={}, impl={}, factory={}", manager, impl, factory);

		assert manager != null : "manager is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		Pair<Class<?>, Class<?>> key = new ImmutablePair<Class<?>, Class<?>> (manager, impl);

		assert (! this.factories.containsKey (key)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (key, factory);
	}

	/**
	 * Create a <code>ElementManager</code> to act upon the specified
	 * <code>DataStore</code>.  If the <code>ElementManager</code> already
	 * exists in the cache, then the cached copy will be returned, otherwise a
	 * new <code>ElementManager</code> will be created.
	 *
	 * @param  <T>       The <code>ElementManager</code> type to return
	 * @param  <U>       The <code>Element</code> represented by the
	 *                   <code>ElementManager</code>
	 * @param  element   The <code>Element</code> interface class, not null
	 * @param  manager   The <code>ElementManager</code> interface class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementManager</code> will be acting, not null
	 * @return           The <code>ElementManager</code>
	 */

	@SuppressWarnings("unchecked")
	public <T extends ElementManager<U>, U extends Element> T create (final Class<U> element, final Class<T> manager, final DataStore datastore)
	{
		this.log.trace ("create: element={}, manager={}, datastore={}", element, manager, datastore);

		assert element != null : "element is NULL";
		assert manager != null : "manager is NULL";
		assert datastore != null : "datastore is NULL";

		Class<?> impl = (datastore.getProfile ()).getManagerClass (element);
		this.log.debug ("Creating ElementManager using implementation class: {}", impl);

		Triple<DataStore, Class<?>, Class<?>> cacheKey = new ImmutableTriple<DataStore, Class<?>, Class<?>> (datastore, manager, impl);

		if ((! this.cache.containsKey (cacheKey)) || ((this.cache.get (cacheKey)).get () == null))
		{
			this.log.debug ("Creating new ElementManager instance");

			Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (manager, impl);

			assert this.factories.containsKey (factoryKey) : "Manager implementation Class not registered: " + impl.getSimpleName ();

			this.cache.put (cacheKey, new WeakReference<ElementManager<? extends Element>> (((ManagerFactory<T>) this.factories.get (factoryKey)).create (datastore)));
		}

		return manager.cast ((this.cache.get (cacheKey)).get ());
	}

	/**
	 * Remove all of the <code>ElementManager</code> instances for the
	 * specified <code>DataStore</code> from the cache.
	 *
	 * @param  datastore The <code>DataStore</code> for which all of the cached
	 *                   <code>ElementManager</code> instances are to be
	 *                   purged, not null
	 */

	public void remove (final DataStore datastore)
	{
		this.log.trace ("remove: datastore={}", datastore);

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
	 * Remove all managers from the cache.
	 */

	public void flush ()
	{
		this.log.trace ("flush:");

		this.cache.clear ();
	}
}
