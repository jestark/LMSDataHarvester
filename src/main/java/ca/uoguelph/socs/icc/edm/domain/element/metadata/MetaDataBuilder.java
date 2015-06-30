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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

/**
 * <code>MetaData</code> builder for <code>Element</code> implementation
 * classes.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @param   <U> The <code>Element</code> implementation type
 */

public final class MetaDataBuilder<T extends Element, U extends T>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Definition</code> of the <code>Element</code> */
	private final Definition<T> definition;

	/** The <code>Element</code> implementation class */
	private final Class<U> impl;

	/** The <code>Set</code> of <code>Property</code> instances */
	private final Set<Property<?>> properties;

	/** The <code>ElementBuilder</code> implementation class */
	private Class<? extends ElementBuilder<T>> builder;

	/** Reference to the Constructor for the implementation class */
	private Supplier<U> create;

	/** <code>Property</code> to <code>PropertyReference</code> mapping */
	private final Map<Property<?>, PropertyReference<T, U, ?>> refs;

	/**
	 * Create the <code>MetaDataBuilder</code>.
	 *
	 * @param  <T>  The interface type of the <code>Element</code>
	 * @param  <U>  The implementation type of the <code>Element</code>
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The <code>Element</code> implementation class, not null
	 *
	 * @return      The <code>MetaDataBuilder</code>
	 */

	public static <T extends Element, U extends T> MetaDataBuilder<T, U> newInstance (final Class<T> type, final Class<U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		return new MetaDataBuilder<T, U> (type, impl);
	}

	/**
	 * Create the <code>MetaDataBuilder</code>.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 * @param  impl The <code>Element</code> implementation class, not null
	 */

	@SuppressWarnings ("unchecked")
	private MetaDataBuilder (final Class<T> type, final Class<U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		this.log = LoggerFactory.getLogger (MetaDataBuilder.class);

		// Force the interface to be loaded, because it might not be and the
		// next step will fail without it
		for (Class<?> cls : type.getClasses ())
		{
			this.forceClassLoad (cls);
		}

		this.definition = (Definition<T>) MetaData.getDefinition (type);
		this.impl = impl;

		assert this.definition != null : "Element Definition is not Registered";
		this.properties = this.definition.getProperties ();

		this.refs = new HashMap<Property<?>, PropertyReference<T, U, ?>> ();
	}

	/**
	 * Force the JVM to load the specified <code>Class</code>.  This is a
	 * utility method used to ensure that classes are loaded, initialized and
	 * registered before they are needed.
	 *
	 * @param  cls The <code>Class</code> to load
	 */

	private void forceClassLoad (final Class<?> cls)
	{
		try
		{
			Class.forName (cls.getName ());
		}
		catch (ClassNotFoundException ex)
		{
			// this should never happen, but we have to process the exception
			this.log.error ("Interface Class not found: {}", cls.getName ());
			throw new IllegalStateException (ex);
		}
	}

	/**
	 * Get the Java type of the <code>Element</code> interface.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public Class<T> getElementType ()
	{
		return this.definition.getElementType ();
	}

	/**
	 * Get the Java type of the <code>Element</code> implementation.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public Class<U> getElementClass ()
	{
		return this.impl;
	}

	/**
	 * Set a Method reference to the no-argument constructor of the
	 * <code>Element</code> implementation class.
	 *
	 * @param  create Method Reference to the constructor, not null
	 */

	public void setCreateMethod (final Supplier<U> create)
	{
		this.log.trace ("setCreateMethod: create={}", create);

		assert create != null : "create is NULL";

		this.create = create;
	}

	/**
	 * Set the <code>ElementBuilder</code> implementation class to be used to
	 * builder new instances of the <code>Element</code>.
	 *
	 * @param  <B>     The <code>ElementBuilder</code> interface type
	 * @param  builder The <code>ElementBuilder</code> implementation class,
	 *                 not null
	 */

	public <B extends ElementBuilder<T>> void setBuilder (final Class<B> builder)
	{
		this.log.trace ("setBuilder: builder={}", builder);

		assert builder != null : "builder is NULL";

		this.forceClassLoad (builder);

		this.builder = builder;
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation, however the
	 * <code>set</code> reference is allowed to be <code>null</code> which will
	 * result in a read-only <code>Property</code> for the <code>Element</code>
	 * implementation.  The specified <code>Property</code> instance must
	 * already exist in the <code>Definition</code> for the
	 * <code>Element</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, may be null
	 */

	public <V> void addProperty (final Property<V> property, final Function<T, V> get, final BiConsumer<U, V> set)
	{
		this.log.trace ("addProperty: property={}, get={}, set={}", property, get, set);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert this.properties.contains (property) : "property is not registered for Element";

		this.refs.put (property, new PropertyReference<T, U, V> (get, set));
	}

	/**
	 * Build the <code>MetaData</code> for the <code>Element</code>
	 * implementation class.
	 *
	 * @return The <code>MetaData</code>
	 */

	public MetaData<T, U> build ()
	{
		this.log.trace ("build:");

		assert this.builder != null : "builder is NULL";
		assert this.create != null : "create is NULL";
		assert this.properties.equals (this.refs.keySet ()) : "Missing references from some properties";

		return new MetaData<T, U> (this.definition, this.impl, this.builder, this.create, this.refs);
	}
}
