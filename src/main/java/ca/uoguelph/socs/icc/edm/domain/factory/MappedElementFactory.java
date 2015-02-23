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

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

/**
 * Factory for creating <code>ElementBuilder</code> objects.   
 * 
 * @author  James E.Stark
 * @version 1.0
 * @param   <T> The domain model interface represented by this factory
 * @param   <X> The builder interface created by this factory
 * @param   <Y> The element factory interface used by the builder created by
 *              this factory
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory
 */

public final class MappedElementFactory
{
	/** The logger */
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<Pair<Class<?>, Class<?>>, ElementFactory<? extends Element>> factories;

	/**
	 * Create the <code>MappedBuilderFactory</code>.
	 *
	 * @param  type The domain model interface class for which the builders are
	 *              being created, not null
	 */

	public MappedElementFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedElementFactory.class);

		this.factories = new HashMap<Pair<Class<?>, Class<?>>, ElementFactory<? extends Element>> ();
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

	public <T extends ElementFactory<U>, U extends Element> void registerFactory (final Class<T> type, final Class<? extends U> impl, final T factory)
	{
		this.log.trace ("Registering ElementFactory: {} ({})", impl, factory);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		Pair<Class<?>, Class<?>> key = new ImmutablePair<Class<?>, Class<?>> (type, impl);

		assert (! this.factories.containsKey (key)) : "Class already registered: " + impl.getSimpleName ();

		this.factories.put (key, factory);
	}

	public <T extends ElementFactory<U>, U extends Element> T getFactory (final Class<T> type, final Class<? extends U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		Pair<Class<?>, Class<?>> key = new ImmutablePair<Class<?>, Class<?>> (type, impl);

		assert this.factories.containsKey (key) : "Class not registered: " + impl.getSimpleName ();

		return type.cast (this.factories.get (key));
	}
}
