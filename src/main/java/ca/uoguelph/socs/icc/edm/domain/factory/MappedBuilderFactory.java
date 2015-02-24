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

package ca.uoguelph.socs.icc.edm.domain.factory;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

/**
 * Factory for creating <code>ElementBuilder</code> objects.   
 * 
 * @author  James E.Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory
 */

public final class MappedBuilderFactory
{
	/** The logger */
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<Pair<Class<?>, Class<?>>, BuilderFactory<? extends ElementBuilder<? extends Element>>> factories;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private final Map<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> elements;

	/**
	 * Create the <code>MappedBuilderFactory</code>.
	 *
	 * @param  type The domain model interface class for which the builders are
	 *              being created, not null
	 */

	public MappedBuilderFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedBuilderFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, BuilderFactory<? extends ElementBuilder<? extends Element>>> ();
		this.elements = new HashMap<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> ();
	}

	/**
	 * Register a <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementBuilder</code> when the classes initialize.
	 *
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  impl    The <code>ElementBuilder</code> implementation class, not
	 *                 null
	 * @param  factory The <code>BuilderFactory</code> used to create the
	 *                 <code>ElementBuilder</code>, not null
	 */

	public <T extends ElementBuilder<? extends Element>> void registerFactory (final Class<T> builder, final Class<? extends T> impl, final BuilderFactory<T> factory)
	{
		this.log.trace ("Registering Builder: {} ({})", impl, factory);

		assert builder != null : "builder is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (builder, impl);

		assert (! this.factories.containsKey (factoryKey)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (factoryKey, factory);
	}

	/**
	 * Register a <code>Element</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>Element</code> when the classes initialize.
	 *
	 * @param  element The <code>Element</code> implementation class, not null
	 * @param  builder The builder class to be used to build the
	 *                 <code>Element</code>, not null
	 */

	public <T extends ElementBuilder<U>, U extends Element> void registerElement (final Class<? extends U> element, final Class<T> builder)
	{
		this.log.trace ("Registering Element: {} ({})", element, builder);

		assert element != null : "element is NULL";
		assert builder != null : "builder is NULL";
		assert ! this.elements.containsKey (element) : "Class already registered: " + element.getSimpleName ();

		this.elements.put (element, builder);
	}

	/**
	 * Create a <code>ElementBuilder</code> for the specified
	 * <code>ElementManager</code>.
	 *
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 * @param  manager The <code>ElementManager</code>, not null
	 * @return         The <code>ElementBuilder</code>
	 */

	@SuppressWarnings("unchecked")
	public <T extends ElementBuilder<U>, U extends Element> T create (final Class<T> builder, final Class<? extends U> element, final DomainModel manager)
	{
		this.log.debug ("Creating builder {} for element {} on manager {}", builder, element, manager);

		assert builder != null : "type is NULL";
		assert element != null : "impl is NULL";
		assert manager != null : "manager is NULL";
		
		assert this.elements.containsKey (element) : "Element class is not registered: " + element.getSimpleName ();

		Pair<Class<?>, Class<?>> factoryKey = new ImmutablePair<Class<?>, Class<?>> (builder, this.elements.get (element));

		assert this.factories.containsKey (factoryKey) : "Class not registered: " + (this.elements.get (element)).getSimpleName ();

		return ((BuilderFactory<T>) this.factories.get (factoryKey)).create (manager);
	}
}
