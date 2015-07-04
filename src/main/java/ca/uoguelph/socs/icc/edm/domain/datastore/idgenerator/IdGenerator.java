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

import java.util.Map;
import java.util.HashMap;

import java.util.function.BiFunction;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * An ID number generator.  Classes implementing this interface will provide
 * ID numbers suitable for use with the underlying <code>DataStore</code>.
 * Each class implementing this interface is responsible for determining how
 * the  ID numbers are calculated, with different classes providing different
 * distributions of ID numbers.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class IdGenerator
{
	/** Factories for the <code>IdGenerator</code> implementations */
	private static final Map<Class<? extends IdGenerator>, BiFunction<Class<? extends Element>, DataStore, ? extends IdGenerator>> factories;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		factories = new HashMap<Class<? extends IdGenerator>, BiFunction<Class<? extends Element>, DataStore, ? extends IdGenerator>> ();
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

	protected static <T extends IdGenerator> void registerGenerator (final Class<T> generator, final BiFunction<Class<? extends Element>, DataStore ,T> factory)
	{
		assert generator != null : "generator is NULL";
		assert factory != null : "factory is NULL";
		assert (! IdGenerator.factories.containsKey (generator)) : "Class already registered: " + generator.getSimpleName ();

		IdGenerator.factories.put (generator, factory);
	}

	/**
	 *
	 * @param  <T>
	 * @param  generator
	 * @param  datastore
	 *
	 * @return 
	 */

	public static <T extends IdGenerator, U extends Element> T getInstance (final Class<T> generator, final Class<U> element, final DataStore datastore)
	{
		assert generator != null : "generator is NULL";
		assert datastore != null : "datastore is NULL";
		assert IdGenerator.factories.containsKey (generator) : "Generator class is not registered";

		return generator.cast ((IdGenerator.factories.get (generator)).apply (element, datastore));
	}

	/**
	 * Return the next available ID number.
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();
}
