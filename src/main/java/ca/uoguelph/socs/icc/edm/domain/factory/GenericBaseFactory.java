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
 * Base implementation of <code>GenericFactory</code>.  This class implements
 * the <code>GenericFactory</code> interface using a <code>HashMap</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <K> The type used to index the registered factories
 * @param   <T> The type of the objects to be created by the factory
 * @param   <X> The type of the objects to be used as parameters for creation
 */

public class GenericBaseFactory<K, T, X> implements GenericFactory<K, T, X>
{
	/** Map containing the classes and factories */
	private final Map<K, ConcreteFactory<T, X>> factories;

	/** Log */
	private final Log log;

	/**
	 * Create the Factory.
	 */

	public GenericBaseFactory ()
	{
		this.factories = new HashMap<K, ConcreteFactory<T, X>> ();

		this.log = LogFactory.getLog (GenericBaseFactory.class);
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
		if (key == null)
		{
			this.log.error ("Key is NULL");
			throw new NullPointerException ();
		}

		if (factory == null)
		{
			this.log.error ("Factory is NULL");
			throw new NullPointerException ();
		}

		if (this.factories.containsKey (key))
		{
			String msg = key + "has already been registered";

			this.log.error (msg);
			throw new IllegalArgumentException (msg);
		}

		this.factories.put (key, factory);
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
		return this.factories.keySet ();
	}

	/**
	 * Create an instance of an implementation class.  This method will call the
	 * <code>create</code> method on the <code>ConcreteFactory</code> which is
	 * associated with the specified implementation class.
	 *
	 * @param  impl                     The registration key, not null
	 * @param  arg                      Parameter to be used to create the
	 *                                  instance
	 * @return                          An instance of the requested class
	 * @throws IllegalArgumentException if the requested implementation is not
	 *                                  registered
	 * @see    ConcreteFactory#create
	 */

	@Override
	public T create (K key, X arg)
	{
		if (key == null)
		{
			this.log.error ("Registration key is NULL");
			throw new NullPointerException ("Registration key is NULL");
		}

		if (! this.factories.containsKey (key))
		{
			String msg = "Key not registered: " + key;

			this.log.error (msg);
			throw new IllegalArgumentException (msg);
		}

		return (this.factories.get (key)).create (arg);
	}
}