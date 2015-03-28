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

	/** Implementation to interface mapping */
	private final Map<Class<? extends Element>, Class<? extends ElementFactory<? extends Element>>> interfaces;

	/** <code>ElementFactory</code> instances */
	private final Map<Class<? extends Element>, ElementFactory<? extends Element>> factories;

	/**
	 * Create the <code>MappedElementFactory</code>.
	 */

	public MappedElementFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedElementFactory.class);

		this.interfaces = new HashMap<Class<? extends Element>, Class<? extends ElementFactory<? extends Element>>> ();
		this.factories = new HashMap<Class<? extends Element>, ElementFactory<? extends Element>> ();
	}

	/**
	 * Register an <code>ElementFactory</code> instance.
	 *
	 * @param  factoryClass The <code>ElementFactory</code> interface class,
	 *                      not null
	 * @param  elementClass The <code>Element</code> implementation class, not
	 *                      null
	 * @param  factory      The <code>ElementFactory</code> instance, not null
	 */

	public <T extends ElementFactory<U>, U extends Element> void registerFactory (final Class<T> factoryClass, final Class<? extends U> elementClass, final T factory)
	{
		this.log.trace ("registerFactory: factoryClass={}, elementClass={}, impl={}", factoryClass, elementClass, factory);

		assert factoryClass != null : "factoryClass is NULL";
		assert elementClass != null : "elementClass is NULL";
		assert factory != null : "factory is NULL";
		assert (! this.factories.containsKey (elementClass)) : "Class already registered: " + elementClass.getSimpleName ();

		this.interfaces.put (elementClass, factoryClass);
		this.factories.put (elementClass, factory);
	}

	/**
	 * Get the <code>ElementFactory</code> instance for the specified
	 * <code>Element</code> implementation class.
	 *
	 * @param  factoryClass The <code>ElementFactory</code> interface class,
	 *                      not null
	 * @param  elementClass The <code>Element</code> implmentation class, not
	 *                      null
	 *
	 * @return The <code>ElementFactory</code> instance
	 */

	public <T extends ElementFactory<U>, U extends Element> T getFactory (final Class<T> factoryClass, final Class<? extends Element> elementClass)
	{
		this.log.trace ("getFactory: factoryClass={}, elementClass={}", factoryClass, elementClass);

		assert factoryClass != null : "factoryClass is NULL";
		assert elementClass != null : "elementClass is NULL";
		assert factoryClass.isAssignableFrom (this.interfaces.get (elementClass)) : "Element not registered for factory:" + factoryClass.getSimpleName () + " (" + elementClass.getSimpleName () + ")";
		assert this.factories.containsKey (elementClass) : "Class not registered: " + elementClass.getSimpleName ();

		T factory = null;

		if ((this.interfaces.containsKey (elementClass)) && (factoryClass.isAssignableFrom (this.interfaces.get (elementClass))))
		{
			factory = factoryClass.cast (this.factories.get (elementClass));
		}

		return factory;
	}
}
