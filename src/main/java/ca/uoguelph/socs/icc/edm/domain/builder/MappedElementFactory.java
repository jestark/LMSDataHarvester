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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * <code>ElementFactory</code> map.
 * 
 * @author  James E.Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory
 */

public final class MappedElementFactory
{
	/** The logger */
	private final Logger log;

	/** <code>ElementFactory</code> instances */
	private final Map<Pair<Class<?>, Class<?>>, ElementFactory<? extends Element>> factories;

	/**
	 * Create the <code>MappedElementFactory</code>.
	 */

	public MappedElementFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedElementFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, ElementFactory<? extends Element>> ();
	}

	/**
	 * Register an <code>ElementFactory</code> instance.
	 *
	 * @param  factory The <code>ElementFactory</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 * @param  impl    The <code>ElementFactory</code> instance, not null
	 */

	public <T extends ElementFactory<U>, U extends Element> void registerFactory (final Class<T> factory, final Class<? extends U> element, final T impl)
	{
		this.log.trace ("Registering ElementFactory: {} ({}) for element {}", factory, impl, element);

		assert factory != null : "factory is NULL";
		assert element != null : "element is NULL";
		assert impl != null : "impl is NULL";

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (factory, element);

		assert (! this.factories.containsKey (factoryKey)) : "Class already registered: " + element.getSimpleName ();

		this.factories.put (factoryKey, impl);
	}

	/**
	 * Get the <code>ElementFactory</code> instance for the specified
	 * <code>Element</code> implementation class.
	 *
	 * @param  factory The <code>ElementFactory</code> interface class, not null
	 * @param  element The <code>Element</code> implmentation class, not null
	 *
	 * @return The <code>ElementFactory</code> instance
	 */

	public <T extends ElementFactory<U>, U extends Element> T getFactory (final Class<T> factory, final Class<? extends U> element)
	{
		assert factory != null : "factory is NULL";
		assert element != null : "element is NULL";

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (factory, element);

		assert this.factories.containsKey (factoryKey) : "Class not registered: " + element.getSimpleName ();

		return factory.cast (this.factories.get (factoryKey));
	}
}
