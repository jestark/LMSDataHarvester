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
 * Meta-data definition for an <code>Element</code> class.  Though an instance
 * of this class the referenced <code>Element</code> implementation class can
 * be created and its data can be manipulated.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @see     MetaData
 * @see     Property
 * @see     Selector
 */

public class Definition<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Definition</code> for the super class */
	private final Definition<? super T> parent;

	/** The <code>Element</code> class represented by the <code>Definition</code> */
	private final Class<T> type;

	/** The <code>Property</code> instances associated with the interface */
	private final Map<String, Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Map<String, Selector> selectors;

	/** <code>Property</code> to <code>Reference</code> instance mapping */
	private final Map<Property<?>, PropertyReference<T, ?>> references;

	/**
	 * Get the <code>DefintionBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  type   The <code>Element</code> interface class, not null
	 * @param  parent The <code>Definition</code> for the parent
	 *                <code>Element</code> interface class, must be null for
	 *                <code>Element</code>, not null otherwise
	 *
	 * @return        The <code>DefinitionBuilder</code> instance
	 */

	public static <T extends Element> DefinitionBuilder<T> getBuilder (final Class<T> type, final Definition<? super T> parent)
	{
		assert type != null : "type is NULL";
		assert (type == Element.class && parent == null) : "Element must have a null parent";
		assert (type != Element.class && parent != null) : "Parent can not be null unless type is Element";

		return new DefinitionBuilder<T> (type, parent);
	}

	/**
	 * Create the <code>MetaData</code>.
	 *
	 * @param  type
	 * @param  parent
	 * @param  properties
	 * @param  references <code>Property</code> to get/set method mapping, not
	 *                    null
	 * @param  selectors
	 */

	protected Definition (final Class<T> type, final Definition<? super T> parent, final Map<String, Property<?>> properties, final Map<Property<?>, PropertyReference<T, ?>> references, final Map<String, Selector> selectors)
	{
		assert type != null : "type is NULL";
		assert properties != null : "properties is NULL";
		assert selectors != null : "selectors is NULL";
		assert references != null : "references is NULL";

		this.log = LoggerFactory.getLogger (MetaData.class);

		this.type = type;
		this.parent = parent;
		this.selectors = selectors;
		this.properties = properties;
		this.references = references;
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
	private <V> PropertyReference<T, V> getReference (final Property<V> property)
	{
		assert property != null : "reference is NULL";

		PropertyReference<?, ?> result = (PropertyReference<T, V>) this.references.get (property);

		if ((result == null) && (this.parent != null))
		{
			result = this.parent.getReference (property);
		}

		return (PropertyReference<T, V>) result;
	}

	/**
	 * Get the Java type of the <code>Element</code> interface.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public Class<T> getElementType ()
	{
		return this.type;
	}

	/**
	 * Get the <code>Property</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Property</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public Property<?> getProperty (final String name)
	{
		assert name != null : "name is NULL";

		Property<?> result = this.properties.get (name);

		if ((result == null) && (this.parent != null))
		{
			result = this.parent.getProperty (name);
		}

		return result;
	}

	/**
	 * Get the <code>Property</code> instance with the specified name and type.
	 *
	 * @param  name                     The name, not null
	 * @param  type                     The type, not null
	 *
	 * @return                          The <code>Property</code>, may be null
	 * @throws IllegalArgumentException if the type specified does not match
	 *                                  the type of the <code>Property</code>
	 */

	@SuppressWarnings ("unchecked")
	public <V> Property<V> getProperty (final String name, final Class<V> type)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";

		Property<?> result = this.getProperty (name);

		if ((result != null) && (type != result.getPropertyType ()))
		{
			throw new IllegalArgumentException ("The type specified and the Type of the property do not match");
		}

		return (Property<V>) result;
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Property</code> instances
	 */

	public Set<Property<?>> getProperties ()
	{
		Set<Property<?>> result = new HashSet<Property<?>> (this.properties.values ());

		if (this.parent != null)
		{
			result.addAll (this.parent.getProperties ());
		}

		return result;
	}

	/**
	 * Get the <code>Selector</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Selector</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public Selector getSelector (final String name)
	{
		assert name != null : "name is NULL";

		Selector result = this.selectors.get (name);

		if ((result == null) && (this.parent != null))
		{
			result = this.parent.getSelector (name);
		}

		return result;
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Selector</code> instances
	 */

	public Set<Selector> getSelectors ()
	{
		Set<Selector> result = new HashSet<Selector> (this.selectors.values ());

		if (this.parent != null)
		{
			result.addAll (this.parent.getSelectors ());
		}

		return result;
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

		PropertyReference<T, V> ref = this.getReference (property);

		assert ref != null : "Property is not registered";

		return ref.getValue (element);
	}

	/**
	 * Set the value corresponding to the specified <code>Property</code> in
	 * the specified <code>Element</code> instance to the specified value.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be set, may be null
	 */

	public <V> void setValue (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("setValue: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		PropertyReference<T, V> ref = this.getReference (property);

		assert ref != null : "Property is not registered";
		assert ref.isWritable () : "property can not be written";

		ref.setValue (element, value);
	}

	/**
	 * Copy the value corresponding to the specified <code>Property</code> from
	 * the source <code>Element</code> to the destination <code>Element</code>
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  dest     The destination <code>Element</code>, not null
	 * @param  source   The source <code>Element</code>, not null
	 */

	public void copyValue (final Property<?> property, final T dest, final T source)
	{
		this.log.trace ("copyValue: property={}, dest={}, source={}", property, dest, source);

		assert dest != null : "dest is NULL";
		assert source != null : "source is NULL";
		assert property != null : "property is NULL";

		PropertyReference<T, ?> ref = this.getReference (property);

		assert ref != null : "Property is not registered";
		assert ref.isWritable () : "property can not be written";

		ref.copyValue (dest, source);
	}
}
