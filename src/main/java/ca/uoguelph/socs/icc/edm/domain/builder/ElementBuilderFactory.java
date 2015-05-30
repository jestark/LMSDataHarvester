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

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Factory for creating <code>ElementBuilder</code> objects.
 *
 * @author  James E.Stark
 * @version 1.0
 * @see     BuilderFactory
 * @see     ElementFactory
 */

final class ElementBuilderFactory
{
	/** The logger */
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<Class<?>, BuilderFactory<?, ?>> factories;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private final Map<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> elements;

	/**
	 * Create the <code>ElementBuilderFactory</code>.
	 *
	 * @param  type The domain model interface class for which the builders are
	 *              being created, not null
	 */

	public ElementBuilderFactory ()
	{
		this.log = LoggerFactory.getLogger (ElementBuilderFactory.class);

		this.factories = new HashMap<Class<?>, BuilderFactory<?, ?>> ();
		this.elements = new HashMap<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> ();
	}

	/**
	 * Register a <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementBuilder</code> when the classes initialize.
	 *
	 * @param  <T>
	 * @param  <U>
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  impl    The <code>ElementBuilder</code> implementation class,
	 *                 not null
	 * @param  factory The <code>BuilderFactory</code> used to create the
	 *                 <code>ElementBuilder</code>, not null
	 */

	public <T extends ElementBuilder<U>, U extends Element> void registerFactory (final Class<T> builder, final Class<? extends T> impl, final BuilderFactory<T, U> factory)
	{
		this.log.trace ("registerFactory: builder={}, impl={}, factory={}", builder, impl, factory);

		assert builder != null : "builder is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		assert (! this.factories.containsKey (impl)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (impl, factory);
	}

	/**
	 * Register a <code>Element</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>Element</code> when the classes initialize.
	 *
	 * @param  <T>
	 * @param  <U>
	 * @param  element The <code>Element</code> implementation class, not null
	 * @param  builder The builder class to be used to build the
	 *                 <code>Element</code>, not null
	 */

	public <T extends ElementBuilder<U>, U extends Element> void registerElement (final Class<? extends U> element, final Class<T> builder)
	{
		this.log.trace ("registerElement: element={}, builder={}", element, builder);

		assert element != null : "element is NULL";
		assert builder != null : "builder is NULL";
		assert ! this.elements.containsKey (element) : "Class already registered: " + element.getSimpleName ();

		this.elements.put (element, builder);
	}

	/**
	 * Create an <code>ElementBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  <T>
	 * @param  <U>
	 * @param  builder   The <code>ElementBuilder</code> interface class, not
	 *                   null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 * @param  datastore The <code>DataStore</code>, not null
	 * @return           The <code>ElementBuilder</code>
	 */

	@SuppressWarnings("unchecked")
	public <T extends ElementBuilder<U>, U extends Element> T create (final Class<? extends Element> element, final DataStore datastore)
	{
		this.log.trace ("create: element={}, datastore={}", element, datastore);

		assert element != null : "impl is NULL";
		assert datastore != null : "datastore is NULL";

		assert this.elements.containsKey (element) : "Element class is not registered: " + element.getSimpleName ();
		assert this.factories.containsKey (this.elements.get (element)) : "Class not registered: " + (this.elements.get (element)).getSimpleName ();

		return ((BuilderFactory<T, U>) this.factories.get (this.elements.get (element))).create (datastore);
	}
}
