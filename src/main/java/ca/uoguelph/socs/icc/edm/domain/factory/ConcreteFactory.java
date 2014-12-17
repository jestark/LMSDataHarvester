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

package ca.uoguelph.socs.icc.edm.domain;

/**
 * Low-level factory interface.  This interface along with
 * <code>GenericFactory</code> specifies a two-part factory structure, for use
 * cases where there are multiple implementations of a given interface, and one
 * needs to be selected.
 * <p>
 * Implementations of this interface are responsible for actually creating the
 * target class and performing and necessary initialization.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the objects to be created by the factory
 * @param   <X> The type of the objects to be used as parameters for creation
 * @see     GenericFactory
 */

public interface ConcreteFactory<T, X>
{
	/**
	 * Create an instance of the class.
	 *
	 * @param  key  Parameter to be used to create the instance
	 * @return      An instance of the class
	 * @see    GenericFactory#create
	 */

	public abstract T create (X key);
}
