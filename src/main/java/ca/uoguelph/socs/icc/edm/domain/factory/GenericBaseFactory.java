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
 * @param   <T> The type of the objects to be created by the factory
 * @param   <X> The type of the objects to be used as parameters for creation
 */

public class GenericBaseFactory<T, X> implements GenericFactory<T, X>
{
	/** Map containing the classes and factories */
	private final Map<Class<? extends T>, ConcreteFactory<T, X>> factories;

	/** Log */
	private final Log log;

	/**
	 * Create the Factory.
	 */

	public GenericBaseFactory ()
	{
		this.factories = new HashMap<Class<? extends T>, ConcreteFactory<T, X>> ();

		this.log = LogFactory.getLog (GenericBaseFactory.class);
	}

	/**
	 * Register an implementation with the factory.
	 *
	 * @param  impl                      The implementation class to register, not
	 *                                   null
	 * @param  factory                   The factory used to instantiate the class
	 * @throws IllegalArguementException if the implementation class is already
	 *                                   registered
	 */

	@Override
	public void registerClass (Class<? extends T> impl, ConcreteFactory<T, X> factory)
	{
		if (impl == null)
		{
			this.log.error ("Implementation Class is NULL");
			throw new NullPointerException ();
		}

		if (factory == null)
		{
			this.log.error ("Factory is NULL");
			throw new NullPointerException ();
		}

		if (this.factories.containsKey (impl))
		{
			String msg = impl.getName () + "has already been registered";

			this.log.error (msg);
			throw new IllegalArgumentException (msg);
		}

		this.factories.put (impl, factory);
	}

	/**
	 * Get the set of classes which have been registered with this factory.
	 *
	 * @return A <code>Set</code> containing the classes which are registered with
	 *         the factory.
	 */

	@Override
	public Set<Class<? extends T>> getRegisteredClasses ()
	{
		return this.factories.keySet ();
	}

	/**
	 * Create an instance of an implementation class.  This method will call the
	 * <code>create</code> method on the <code>ConcreteFactory</code> which is
	 * associated with the specified implementation class.
	 *
	 * @param  impl                     The implementation class to instantiate,
	 *                                  not null
	 * @param  key                      Parameter to be used to create the
	 *                                  instance
	 * @return                          An instance of the requested class
	 * @throws IllegalArgumentException if the requested implementation is not
	 *                                  registered
	 * @see    ConcreteFactory#create
	 */

	@Override
	public T create (Class<? extends T> impl, X key)
	{
		if (impl == null)
		{
			this.log.error ("Implementation Class is NULL");
			throw new NullPointerException ();
		}

		if (! this.factories.containsKey (impl))
		{
			String msg = "Class is not registered: " + impl.getName ();

			this.log.error (msg);
			throw new IllegalArgumentException (msg);
		}

		return (this.factories.get (impl)).create (key);
	}
}
