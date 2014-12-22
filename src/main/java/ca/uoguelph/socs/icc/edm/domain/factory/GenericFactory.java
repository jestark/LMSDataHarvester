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

import java.util.Set;

/**
 * High-level factory interface.  Implementations of this interface along with
 * <code>ConcreteFactory</code> specify a two part factory structure.  The two
 * part factory is intended for use cases where a given interface has multiple
 * implementations, where one will be selected at runtime.  For the interface
 * in question there should be one <code>GenericFactory</code> and one
 * <code>ConcreteFactory</code> for each implementation.
 * 
 * @author  James E. Stark
 * @version 1.0
 * @param   <K> The type used to index the registered factories
 * @param   <T> The type of the objects to be created by the factory
 * @param   <X> The type of the objects to be used as parameters for creation
 * @see     ConcreteFactory
 */

public interface GenericFactory<K, T, X>
{
	/**
	 * Register an implementation with the factory.
	 *
	 * @param  key                       The registration key, not null
	 * @param  factory                   The factory used to instantiate the class
	 * @throws IllegalArguementException if the implementation class is already
	 *                                   registered
	 */

	public abstract void registerClass (K key, ConcreteFactory<T, X> factory);

	/**
	 * Get the set of classes which have been registered with this factory.
	 *
	 * @return A <code>Set</code> containing the classes which are registered with
	 *         the factory.
	 */

	public abstract Set<K> getRegisteredClasses ();

	/**
	 * Create an instance of an implementation class.  This method will call the
	 * <code>create</code> method on the <code>ConcreteFactory</code> which is
	 * associated with the specified implementation class.
	 *
	 * @param  key  The registration key, not null
	 * @param  arg  Parameter to be used to create the instance
	 * @return      An instance of the requested class
	 * @see    ConcreteFactory#create
	 */

	public abstract T create (K key, X arg);
}
