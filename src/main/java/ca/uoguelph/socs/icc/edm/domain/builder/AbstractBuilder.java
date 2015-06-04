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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Map;

import java.util.EnumMap;
import java.util.HashMap;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Abstract implementation of the <code>ElementBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractBuilder<T extends Element, E extends Enum<E>> implements ElementBuilder<T>
{
	/** Factory for the <code>ElementBuilder</code> implementations */
	private static final Map<Class<? extends ElementBuilder<? extends Element>>, Function<DataStore, ? extends ElementBuilder<? extends Element>>> FACTORIES;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private static final Map<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> ELEMENTS;

	/** The Logger */
	protected final Logger log;

	/** The <code>DataStore</code> */
	protected final DataStore datastore;

	private final Map<E, Object> elementData;

	/** The <code>Element</code> produced by the <code>ElementBuilder</code> */
	protected T element;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORIES = new HashMap<Class<? extends ElementBuilder<? extends Element>>, Function<DataStore, ? extends ElementBuilder<? extends Element>>> ();
		ELEMENTS = new HashMap<Class<? extends Element>, Class<? extends ElementBuilder<? extends Element>>> ();
	}

	/**
	 * Register an <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementBuilder</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>     The <code>ElementBuilder</code> type to be registered
	 * @param  <U>     The <code>Element</code> type produced by the builder
	 * @param  builder The <code>ElementBuilder</code> implementation class,
	 *                 not null
	 * @param  factory The factory to create instances of the implementation
	 *                 class, not null
	 */

	protected static final <T extends ElementBuilder<U>, U extends Element> void registerBuilder (final Class<? extends T> builder, final Function<DataStore, T> factory)
	{
		assert builder != null : "builder is NULL";
		assert factory != null : "factory is NULL";
		assert (! AbstractBuilder.FACTORIES.containsKey (builder)) : "Class already registered: " + builder.getSimpleName ();

		AbstractBuilder.FACTORIES.put (builder, factory);
	}

	/**
	 * Get an instance of the <code>ElementBuilder</code> which corresponds to
	 * the specified <code>Element</code> implementation class.  This method is
	 * intended to be used by the <code>ElementManager</code> implementations
	 * to get the relevant builder for the <code>ElementManager</code> instance
	 * and the <code>Element</code> upon which it is operating.
	 *
	 * @param  <T>       The <code>ElementBuilder</code> type to be returned
	 * @param  <U>       The <code>Element</code> type produced by the builder
	 * @param  builder   The <code>ElementBuilder</code> interface class, not
	 *                   null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 * @param  datastore The <code>ElementManager</code> instance, not null
	 */

	@SuppressWarnings("unchecked")
	public static final <T extends ElementBuilder<U>, U extends Element> T getInstance (final Class<? extends Element> element, final DataStore datastore)
	{
		assert element   != null : "element is NULL";
		assert datastore != null : "manager is NULL";

		assert AbstractBuilder.ELEMENTS.containsKey (element) : "Element class is not registered: " + element.getSimpleName ();
		assert AbstractBuilder.FACTORIES.containsKey (AbstractBuilder.ELEMENTS.get (element)) : "Class not registered: " + (AbstractBuilder.ELEMENTS.get (element)).getSimpleName ();

		return ((Function<DataStore, T>) AbstractBuilder.FACTORIES.get (AbstractBuilder.ELEMENTS.get (element))).apply (datastore);
	}

	/**
	 * Register the association between an <code>Element</code> implementation
	 * and the appropriate <code>ElementBuilder</code> implementation.  This
	 * method is intended to be used by the <code>Element</code>
	 * implementations to specify the appropriate <code>ElementBuilder</code>
	 * implementation to use for creating instances of that
	 * <code>Element</code>.
	 *
	 * @param  <T>     The interface type of the <code>Element</code>
	 * @param  <U>     The implementation type of the <code>Element</code>
	 * @param  element The implementation class, not null
	 * @param  builder The <code>ElementBuilder</code> implementation class,
	 *                 not null
	 */

	public static final <T extends ElementBuilder<U>, U extends Element> void registerElement (final Class<? extends U> element, final Class<T> builder)
	{
		assert element != null : "element is NULL";
		assert builder != null : "builder is NULL";
		assert (! AbstractBuilder.ELEMENTS.containsKey (element)) : "Class already registered: " + element.getSimpleName ();

		AbstractBuilder.ELEMENTS.put (element, builder);
	}

	/**
	 * Create the <code>AbstractBuilder</code>.
	 *
	 * @param  type      The <code>Element</code> type produced by the
	 *                   <code>ElementBuilder</code>
	 * @param  datastore The <code>DataStore</code> into which new
	 *                   <code>Element</code> instances will be inserted
	 */

	protected AbstractBuilder (final Class<E> properties, final DataStore datastore)
	{
		assert properties != null : "properties is NULL";
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;

		this.elementData = new EnumMap<E, Object> (properties);
	}

	protected final <V> V getPropertyValue (final E property)
	{
		return null;
	}

	protected final <V> void setPropertyValue (final E property, final V value)
	{

	}

	/**
	 *
	 * @return                       The new <code>Element</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 */

	@Override
	public final T build ()
	{
		this.log.trace ("build:");

		return null;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public final void clear ()
	{
		this.log.trace ("clear:");

		this.element = null;
	}

	/**
	 * Load a <code>Element</code> instance into the
	 * <code>ElementBuilder</code>.  This method resets the
	 * <code>ElementBuilder</code> and initializes all of its parameters from
	 * the specified <code>Element</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  element                  The <code>Element</code> to load into
	 *                                  the <code>ElementBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Element</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final T element)
	{
		this.log.trace ("load: element={}", element);

		if (this.datastore.contains (element))
		{
			this.element = element;
		}
	}
}
