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

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

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
	 * @param  impl                     The <code>ElementBuilder</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>BuilderFactory</code> used to
	 *                                  create the <code>ElementBuilder</code>,
	 *                                  not null
	 */

	public <T extends ElementBuilder<? extends Element>> void registerFactory (final Class<T> type, final Class<? extends T> impl, final BuilderFactory<T> factory)
	{
		this.log.trace ("Registering Builder: {} ({})", impl, factory);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		ImmutablePair<Class<?>, Class<?>> key = new ImmutablePair<Class<?>, Class<?>> (type, impl);

		assert (! this.factories.containsKey (key)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (key, factory);
	}

	/**
	 * Register a <code>Element</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>Element</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>Element</code> implementation
	 *                                  which is being registered, not null
	 * @param  builder                  The builder class to be used to build the
	 *                                  <code>Element</code>, not null
	 */

	public <T extends ElementBuilder<U>, U extends Element> void registerElement (Class<? extends U> impl, Class<T> builder)
	{
		this.log.trace ("Registering Element: {} ({})", impl, builder);

		assert impl != null : "impl is NULL";
		assert builder != null : "builder is NULL";
		assert ! this.elements.containsKey (impl) : "Class already registered: " + impl.getSimpleName ();

		this.elements.put (impl, builder);
	}

	/**
	 * Create a <code>ElementBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code> for which the
	 *                               <code>ElementBuilder</code> is to be created,
	 *                               not null
	 * @return                       The <code>ElementBuilder</code>
	 * @throws IllegalStateException if the either of the element or builder 
	 *                               implementation classes is not registered
	 */

	public <T extends ElementBuilder<U>, U extends Element> T create (final Class<T> type, final Class<? extends T> impl, final AbstractManager<U> manager)
	{
		this.log.debug ("Creating builder for interface {} on manager {}", type, manager);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert manager != null : "manager is NULL";
		
		assert this.elements.containsKey (impl) : "Element class is not registered: " + impl.getSimpleName ();

		@SuppressWarnings("unchecked")
		BuilderFactory<T> factory = (BuilderFactory<T>) this.factories.get (ImmutablePair.of (type, this.elements.get (impl)));

		assert factory != null : "No builder registered for class: " + impl.getSimpleName ();

		return factory.create (manager.getDomainModel ());
	}
}
