/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import java.util.function.Function;

/**
 * An ID number generator.  This class and its subclasses provide ID numbers
 * suitable for use with the underlying <code>DataStore</code>.  Each class
 * implementing this interface is responsible for determining how the  ID
 * numbers are calculated, with different classes providing different
 * distributions of ID numbers.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class IdGenerator
{
	/** Factories for the <code>IdGenerator</code> implementations */
	private static final Map<Class<? extends IdGenerator>, Function<List<Long>, ? extends IdGenerator>> factories;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		factories = new HashMap<> ();
	}

	/**
	 * Register a concrete <code>IdGenerator</code> implementation with the
	 * factory.  This method is intended to be used by the concrete
	 * <code>IdGenerator</code> implementations to register themselves with the
	 * factory so that they may be instantiated on demand.
	 *
	 * @param  <T>       The <code>IdGenerator</code> type to be registered
	 * @param  generator The <code>IdGenerator</code> implementation class,
	 *                   not null
	 * @param  factory   The factory to create instances of the implementation
	 *                   class, not null
	 */

	protected static <T extends IdGenerator> void registerGenerator (final Class<T> generator, final Function<List<Long> ,T> factory)
	{
		assert generator != null : "generator is NULL";
		assert factory != null : "factory is NULL";
		assert (! IdGenerator.factories.containsKey (generator)) : "Class already registered: " + generator.getSimpleName ();

		IdGenerator.factories.put (generator, factory);
	}

	/**
	 * Get an <code>IdGenerator</code> instance.
	 *
	 * @param  generator The <code>IdGenerator</code> implementation class,
	 *                   not null
	 * @param  ids       The <code>List</code> of previously used Id numbers,
	 *                   not null
	 *
	 * @return The <code>IdGenerator</code>
	 */

	public static IdGenerator getInstance (final Class<?> generator, List<Long> ids)
	{
		assert generator != null : "generator is NULL";
		assert ids != null : "ids is NULL";
		assert IdGenerator.factories.containsKey (generator) : "Generator class is not registered";

		return IdGenerator.factories.get (generator).apply (ids);
	}

	/**
	 * Return the next available ID number.
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();
}
