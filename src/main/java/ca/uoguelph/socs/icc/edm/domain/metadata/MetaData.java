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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import java.util.function.Supplier;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Meta-data definition for an <code>Element</code> implementation class.  This
 * class contains the meta-data for an <code>Element</code> implementation
 * along with a reference to the <code>Definition</code> of the
 * <code>Element</code> interface which is being implemented.  Though an
 * instance of this class the referenced <code>Element</code> implementation
 * class can be created and its data can be manipulated.
 * <p>
 * This class is intended to be used internally by the
 * <code>ElementBuilder</code> and <code>DataStore</code> implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @param   <U> The <code>Element</code> implementation type
 * @see     Definition
 * @see     Property
 */

public class MetaData<T extends Element, U extends T>
{
	/** The Logger */
	private final Logger log;

	/** Map of <code>Element</code> interface definitions */
	private static final Map<Class<? extends Element>, Definition<? extends Element>> elements;

	/** The <code>MetaData</code> container */
	private static final Container container;

	/** The interface definition for the <code>Element</code> */
	private final Definition<T> element;

	/** The implementation class for the <code>Element</code> */
	private final Class<U> impl;

	/** Method reference for creating new instances */
	private final Supplier<U> create;

	/** All of the <code>Property</code> instances which are writable */
	private final Set<Property<?>> properties;

	/** <code>Property</code> to <code>Reference</code> instance mapping */
	private final Map<Property<?>, PropertyReference<T, U, ?>> references;

	/**
	 * Static initializer to create the elements <code>Map</code>
	 */

	static
	{
		elements = new HashMap<Class<? extends Element>, Definition<? extends Element>> ();
		container = new Container ();
	}

	/**
	 * Register a <code>MetaData</code> instance and add it to the
	 * <code>Container</code>.
	 *
	 * @param  <T>      The <code>Element</code> interface type
	 * @param  <U>      The <code>Element</code> implementation type
	 * @param  metadata The <code>MetaData</code>, not null
	 *
	 * @return          The <code>MetaData</code>
	 */

	protected static <T extends Element, U extends T> MetaData<T, U> registerMetaData (final MetaData<T, U> metadata)
	{
		assert metadata != null : "metadata is NULL";

		MetaData.container.put (metadata);

		return metadata;
	}

	/**
	 * Register a <code>Property</code> instance and add it to the
	 * <code>Definition</code>.
	 *
	 * @param  element                  The <code>Element</code> class, not null
	 * @param  property                 The <code>Property</code>, not null
	 *
	 * @return                          The <code>Property</code> in the
	 *                                  <code>Definition</code>
	 * @throws IllegalArgumentException if a different <code>Property</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 * @throws IllegalStateException    if <code>Element</code> is assignable
	 *                                  from more than one super-interface or
	 *                                  if the parent <code>Element</code> is
	 *                                  not registered
	 */

	protected static <T extends Element, V> Property<V> registerProperty (final Class<T> element, final Property<V> property)
	{
		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert element == property.getElementType () : "property is not of the element type";

		if (! MetaData.elements.containsKey (element))
		{
			MetaData.elements.put (element, new Definition<T> (element));
		}

		return (MetaData.getDefinition (element)).addProperty (property);
	}

	/**
	 * Register a <code>Selector</code> instance and add it to the
	 * <code>Definition</code>.
	 *
	 * @param  selector                 The <code>Selector</code>, not null
	 *
	 * @return                          The <code>Selector</code> in the
	 *                                  <code>Definition</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 * @throws IllegalStateException    If the <code>Element</code> associated
	 *                                  with the <code>Selector</code> has not
	 *                                  been registered
	 */

	protected static Selector registerSelector (final Selector selector)
	{
		assert selector != null : "selector is NULL";

		if (! MetaData.elements.containsKey (selector.getElementType ()))
		{
			throw new IllegalStateException ("No Properties registered for the Selector");
		}

		return (MetaData.getDefinition (selector.getElementType ())).addSelector (selector);
	}

	/**
	 * Get the <code>Set</code> of <code>Element</code> interfaces which have
	 * been registered.
	 *
	 * @return The <code>Set</code> of <code>Element</code> interfaces
	 */

	public static Set<Class<? extends Element>> getElements ()
	{
		return new HashSet<Class<? extends Element>> (MetaData.elements.keySet ());
	}

	/**
	 * Get the <code>Definition</code> for the specified <code>Element</code>
	 * interface.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>Definition</code>
	 */

	public static Definition<?> getDefinition (final Class<?> element)
	{
		assert element != null : "element is NULL";

		return MetaData.elements.get (element);
	}

	/**
	 * Create the <code>MetaData</code>.
	 *
	 * @param  element    The <code>Definition</code>, not null
	 * @param  impl       The <code>Element</code> implementation class, not
	 *                    null
	 * @param  create     Method reference to the no-argument constructor, not
	 *                    null
	 * @param  references <code>Property</code> to get/set method mapping, not
	 *                    null
	 */

	protected MetaData (final Definition<T> element, final Class<U> impl, final Supplier<U> create, final Map<Property<?>, PropertyReference<T, U, ?>> references)
	{
		assert element != null : "element is NULL";
		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";
		assert references != null : "references is NULL";

		this.log = LoggerFactory.getLogger (MetaData.class);

		this.element = element;
		this.impl = impl;

		this.create = create;

		this.references = references;

		assert (this.element.getProperties ()).equals (this.references.keySet ()) : "Mismatch between Property and reference definitions";

		this.properties = this.references.entrySet ()
			.stream ()
			.filter ((x) -> x.getValue ().isWritable ())
			.map ((x) -> x.getKey ())
			.collect (Collectors.toSet ());
	}

	/**
	 * Convenience method to retrieve the <code>Reference</code> associated
	 * with the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the value for the <code>Property</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Reference</code> associated with the
	 *                  <code>Property</code>
	 */

	@SuppressWarnings ("unchecked")
	private <V> PropertyReference<T, U, V> getReference (final Property<V> property)
	{
		assert property != null : "reference is NULL";
		assert this.references.containsKey (property) : "Property is not registered";

		return (PropertyReference<T, U, V>) this.references.get (property);
	}

	/**
	 * Get the Java type of the <code>Element</code> interface.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public Class<T> getElementType ()
	{
		return this.element.getElementType ();
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
	 * Get the <code>Definition</code>.
	 *
	 * @return The <code>Definition</code>
	 */

	public Definition<T> getDefinition ()
	{
		return this.element;
	}

	/**
	 * Get the <code>Set</code> of writable <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing the writable <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public Set<Property<?>> getProperties ()
	{
		return new HashSet<Property<?>> (this.properties);
	}

	/**
	 * Create a new instance of the <code>Element</code>.
	 *
	 * @return The new <code>Element</code> instance
	 */

	public U createElement ()
	{
		this.log.trace ("createElement:");

		return this.create.get ();
	}

	/**
	 * Get the value corresponding to the specified <code>Property</code> from
	 * the specified <code>Element</code> instance.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value corresponding to the <code>Property</code> in
	 *                  the <code>Element</code>
	 */

	public <V> V getValue (final Property<V> property, final T element)
	{
		this.log.trace ("getValue: property={}, element={}", property, element);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert this.references.containsKey (property) : "Property is not registered";

		return (this.getReference (property)).getValue (element);
	}

	/**
	 * Set the value corresponding to the specified <code>Property</code> in
	 * the specified <code>Element</code> instance to the specified value.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be set, may be null
	 */

	public <V> void setValue (final Property<V> property, final U element, final V value)
	{
		this.log.trace ("setValue: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert this.references.containsKey (property) : "Property is not registered";
		assert this.properties.contains (property) : "property can not be written";

		(this.getReference (property)).setValue (element, value);
	}

	/**
	 * Copy the value corresponding to the specified <code>Property</code> from
	 * the source <code>Element</code> to the destination <code>Element</code>
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  dest     The destination <code>Element</code>, not null
	 * @param  source   The source <code>Element</code>, not null
	 */

	public void copyValue (final Property<?> property, final U dest, final T source)
	{
		this.log.trace ("copyValue: property={}, dest={}, source={}", property, dest, source);

		assert dest != null : "dest is NULL";
		assert source != null : "source is NULL";
		assert property != null : "property is NULL";
		assert this.references.containsKey (property) : "Property is not registered";
		assert this.properties.contains (property) : "property can not be written";

		(this.references.get (property)).copyValue (dest, source);
	}
}
