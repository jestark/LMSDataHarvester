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

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Caching implementation of <code>MappedFactory</code>.  This class decorates
 * another instance of the <code>MappedFactory</code> adding a cache of all of
 * the previously created objects.  This first time that an object is created
 * with a given argument, the factory simply passes the request though to the
 * underlying factory, and caches the result.  On subsequent creation requests,
 * the factory will return the cached object.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <K> The type used to index the registered factories
 * @param   <T> The type of the objects to be created by the factory
 * @param   <X> The type of the objects to be used as parameters for creation
 */

public final class CachedMappedFactory<K, T, X> implements MappedFactory<K, T, X>
{
	/** The cache */
	private final Map<X, T> cache;

	/** The underlying factory */
	private final MappedFactory<K, T, X> factory;

	/** The log */
	private final Log log;

	/**
	 * Create the factory.
	 *
	 * @param  factory The factory to use as the base for the
	 *                 <code>CachedMappedFactory</code> instance, not null
	 */

	public CachedMappedFactory (MappedFactory<K, T, X> factory)
	{
		this.log = LogFactory.getLog (CachedMappedFactory.class);

		if (factory == null)
		{
			this.log.error ("Underlying factory is NULL");
			throw new NullPointerException ();
		}

		this.factory = factory;

		this.cache = new HashMap<X, T> ();
	}

	/**
	 * Register an implementation with the factory.
	 *
	 * @param  key                       The registration key, not null
	 * @param  factory                   The factory used to instantiate the class
	 * @throws IllegalArguementException if the implementation class is already
	 *                                   registered
	 */

	@Override
	public void registerClass (K key, ConcreteFactory<T, X> factory)
	{
		this.factory.registerClass (key, factory);
	}

	/**
	 * Get the set of classes which have been registered with this factory.
	 *
	 * @return A <code>Set</code> containing the classes which are registered with
	 *         the factory.
	 */

	@Override
	public Set<K> getRegisteredClasses ()
	{
		return this.factory.getRegisteredClasses ();
	}

	/**
	 * Create an instance of an implementation class.  This method will call the
	 * <code>create</code> method on the underlying factory, and cache the result.
	 * Subsequent calls will return the cached object without making a call to the
	 * underlying factory.
	 * <p>
	 * The arguement is used as the parameter by which the objects created by the
	 * factory are cached.  So only one instance of an object created using the
	 * specified key will be returned.  Subsequent calls with the same key object
	 * will return a reference to the previously created object.  As a result, the
	 * key can not be null, unlike other instances of the
	 * <code>MappedFactory</code>.
	 *
	 * @param  arg  Parameter to be used to create the instance
	 * @return      An instance of the requested class, not null
	 * @see    MappedFactory#create
	 */

	/**
	 * Determine if an implementation class has been registered with the factory.
	 *
	 * @param  key
	 * @return     <code>true</code> if the class is registered,
	 *             <code>false</code> otherwise.
	 */

	@Override
	public boolean isRegistered (K key)
	{
		return this.factory.isRegistered (key);
	}

	/**
	 * Create an instance of an implementation class.  This method will call the
	 * <code>create</code> method on the <code>ConcreteFactory</code> which is
	 * associated with the specified implementation class.
	 * <p>
	 * The key is used as the parameter by which the objects created by the
	 * factory are cached.  So only one instance of an object created using the
	 * specified key will be returned.  Subsequent calls with the same key object
	 * will return a reference to the previously created object.  As a result, the
	 * key can not be null, unlike other instances of the
	 * <code>MappedFactory</code>.
	 *
	 * @param  key  The registration key, not null
	 * @param  arg  Parameter to be used to create the instance
	 * @return      An instance of the requested class, not null
	 * @see    ConcreteFactory#create
	 */

	@Override
	public T create (K key, X arg)
	{
		if (! this.cache.containsKey (arg))
		{
			this.cache.put (arg, this.factory.create (key, arg));
		}

		return this.cache.get (arg);
	}

	/**
	 * Remove the object with the specified key from ant cache.
	 *
	 * @param  item The cache key associated with the object to be removed, not
	 *              null
	 */

	public void remove (X item)
	{
		if (item == null)
		{
			this.log.error ("Item is NULL");
			throw new NullPointerException ("Item is NULL");
		}

		this.cache.remove (item);
	}

	/**
	 * Remove all objects from the cache.
	 */

	public void flush ()
	{
		this.cache.clear ();
	}
}

