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

package ca.uoguelph.socs.icc.edm.domain.loader;

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
import ca.uoguelph.socs.icc.edm.domain.ElementLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Factory for creating <code>ElementLoader</code> objects.  This class
 * requires all of the <code>ElementLoader</code> implementations to register
 * a factory object (extending the <code>LoaderFactory</code> interface)
 * capable of instantiating and initializing the <code>ElementLoader</code>.
 * <p>
 * As a caching factory, this factory will ensure that the
 * is no more than one instance of a given <code>ElementLoader</code>
 * implementation for a given <code>DataStorel</code> at any time.  If the
 * requested <code>ElementLoader</code> already exists then the cached copy
 * will returned, otherwise a new instance will be created.
 *
 * @author  James E. Stark
 * @version 1.3
 * @see     LoaderFactory
 */

final class ElementLoaderFactory
{
	/** The logger */
	private final Logger log;

	/** <code>ElementLoader</code> factories */
	private final Map<Pair<Class<?>, Class<?>>, LoaderFactory<? extends ElementLoader<? extends Element>>> factories;

	/**
	 * Create the <code>MappedLoaderFactory</code>.
	 */

	public ElementLoaderFactory ()
	{
		this.log = LoggerFactory.getLogger (ElementLoaderFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, LoaderFactory<? extends ElementLoader<? extends Element>>> ();
	}

	/**
	 * Register a <code>ElementLoader</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementLoader</code> when the classes initialize.
	 *
	 * @param  <T>     The <code>ElementLoader</code> type being registered
	 * @param  Loader The <code>ElementLoader</code> interface class, not null
	 * @param  impl    The <code>ElementLoader</code> implementation class,
	 *                 not null
	 * @param  factory The <code>LoaderFactory</code> used to create the
	 *                 <code>ElementLoader</code> implementation class, not
	 *                 null
	 */

	public <T extends ElementLoader<? extends Element>> void registerFactory (final Class<T> loader, final Class<? extends T> impl, final LoaderFactory<T> factory)
	{
		this.log.trace ("registerFactory: loader={}, impl={}, factory={}", loader, impl, factory);

		assert loader != null : "loader is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		Pair<Class<?>, Class<?>> key = new ImmutablePair<Class<?>, Class<?>> (loader, impl);

		assert (! this.factories.containsKey (key)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (key, factory);
	}

	/**
	 * Create a <code>ElementLoader</code> to act upon the specified
	 * <code>DataStore</code>.  If the <code>ElementLoader</code> already
	 * exists in the cache, then the cached copy will be returned, otherwise a
	 * new <code>ElementLoader</code> will be created.
	 *
	 * @param  <T>       The <code>ElementLoader</code> type to return
	 * @param  <U>       The <code>Element</code> represented by the
	 *                   <code>ElementLoader</code>
	 * @param  element   The <code>Element</code> interface class, not null
	 * @param  loader   The <code>ElementLoader</code> interface class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>ElementLoader</code> will be acting, not null
	 * @return           The <code>ElementLoader</code>
	 */

	@SuppressWarnings("unchecked")
	public <T extends ElementLoader<U>, U extends Element> T create (final Class<U> element, final Class<T> loader, final DataStore datastore)
	{
		this.log.trace ("create: element={}, loader={}, datastore={}", element, loader, datastore);

		assert element != null : "element is NULL";
		assert loader != null : "loader is NULL";
		assert datastore != null : "datastore is NULL";

		Class<?> impl = (datastore.getProfile ()).getLoaderClass (element);
		this.log.debug ("Creating ElementLoader using implementation class: {}", impl);

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (loader, impl);

		assert this.factories.containsKey (factoryKey) : "Loader implementation Class not registered: " + impl.getSimpleName ();

		return ((LoaderFactory<T>) this.factories.get (factoryKey)).create (datastore);
	}
}
