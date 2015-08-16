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

import java.util.Collection;
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
 * <code>MetaData</code> definition for an <code>Element</code> interface
 * class.  This class contains <code>MetaData</code> definition for an
 * <code>Element</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @see     MetaData
 * @see     Property
 * @see     Selector
 */

public class Definition<T extends Element> implements MetaData<T>
{
	/** The Logger */
	private final Logger log;

	/** The parent <code>Element</code> class */
	private final Class<? extends Element> parent;

	/** The <code>Element</code> class represented by the <code>Definition</code> */
	private final Class<T> type;

	/** The <code>Property</code> instances associated with the interface */
	private final Map<String, Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Map<String, Selector<T>> selectors;

	/**  */
	private final Map<Class<?>, Relationship<T, ?>> relationships;

	/** <code>Property</code> to <code>PropertyReference</code> instance mapping */
	private final Map<Property<?>, PropertyReference<T, ?>> prefs;

	/** <code>Property</code> to <code>RelationshipReference</code> instance mapping*/
	private final Map<Property<?>, RelationshipReference<?, ?>> rrefs;

	/**
	 * Get the <code>DefintionBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  type   The <code>Element</code> interface class, not null
	 * @param  parent The parent <code>Element</code> class, not null
	 *
	 * @return        The <code>DefinitionBuilder</code> instance
	 */

	public static <T extends Element> DefinitionBuilder<T> getBuilder (final Class<T> type, final Class<? extends Element> parent)
	{
		assert type != null : "type is NULL";
		assert parent != null : "parent is NULL";
		assert parent.isAssignableFrom (type) : "type is not derived from parent";

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

	protected Definition (final Class<T> type,
			final Class<? extends Element> parent,
			final Map<String, Property<?>> properties,
			final Map<Class<?>, Relationship<T, ?>> relationships,
			final Map<String, Selector<T>> selectors,
			final Map<Property<?>, PropertyReference<T, ?>> prefs,
			final Map<Property<?>, RelationshipReference<T, ?>> rrefs)
	{
		assert type != null : "type is NULL";
		assert parent != null : "parent is NULL";
		assert parent.isAssignableFrom (type) : "type is not derived from parent";
		assert properties != null : "properties is NULL";
		assert selectors != null : "selectors is NULL";
		assert relationships != null : "relationships is NULL";
		assert prefs != null : "prefs is NULL";
		assert rrefs != null : "rrefs is NULL";

		this.log = LoggerFactory.getLogger (MetaData.class);

		this.type = type;
		this.parent = parent;
		this.selectors = new HashMap<> (selectors);
		this.properties = new HashMap<> (properties);
		this.relationships = new HashMap<> (relationships);
		this.prefs = new HashMap<> (prefs);
		this.rrefs = new HashMap<> (rrefs);
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
	private <V> PropertyReference<T, V> getPropertyReference (final Property<V> property)
	{
		assert property != null : "property is NULL";

		return (PropertyReference<T, V>) this.prefs.get (property);
	}

	@SuppressWarnings ("unchecked")
	private <V> RelationshipReference<T, V> getRelationshipReference (final Property<V> property)
	{
		assert property != null : "property is NULL";

		return (RelationshipReference<T, V>) this.rrefs.get (property);
	}

	/**
	 * Get the Java type of the <code>Element</code> represented by the
	 * <code>MetaData</code>.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	@Override
	public Class<T> getElementClass ()
	{
		return this.type;
	}

	/**
	 * Get the Java type of the parent <code>Element</code> for the class
	 * represented by the <code>MetaData</code>.
	 *
	 * @return The <code>Class</code> representing the parent interface type
	 */

	@Override
	public Class<? extends Element> getParentClass ()
	{
		return this.parent;
	}

	/**
	 * Get the <code>Property</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Property</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	@Override
	public Property<?> getProperty (final String name)
	{
		assert name != null : "name is NULL";

		return this.properties.get (name);
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

	@Override
	@SuppressWarnings ("unchecked")
	public <V> Property<V> getProperty (final String name, final Class<V> type)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";

		Property<?> result = this.properties.get (name);

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

	@Override
	public Set<Property<?>> getProperties ()
	{
		return new HashSet<Property<?>> (this.properties.values ());
	}

	/**
	 * Get the <code>Relationship</code> instance for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  type The <code>Element</code> interface class of the
	 *              relationship target, not null
	 *
	 * @return      The <code>Relationship</code>, may be null
	 */

	@Override
	@SuppressWarnings ("unchecked")
	public <V extends Element> Relationship<T, V> getRelationship (final Class<V> type)
	{
		assert type != null : "type is NULL";

		return (Relationship<T, V>) this.relationships.get (type);
	}

	/**
	 * Get the <code>Selector</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Selector</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	@Override
	public Selector<T> getSelector (final String name)
	{
		assert name != null : "name is NULL";

		return this.selectors.get (name);
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Selector</code> instances
	 */

	@Override
	public Set<Selector<T>> getSelectors ()
	{
		return new HashSet<Selector<T>> (this.selectors.values ());
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

	@Override
	public <V> V getValue (final Property<V> property, final T element)
	{
		this.log.trace ("getValue: property={}, element={}", property, element);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		PropertyReference<T, V> ref = this.getPropertyReference (property);

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

	@Override
	public <V> void setValue (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("setValue: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		PropertyReference<T, V> ref = this.getPropertyReference (property);

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

	@Override
	public void copyValue (final Property<?> property, final T dest, final T source)
	{
		this.log.trace ("copyValue: property={}, dest={}, source={}", property, dest, source);

		assert dest != null : "dest is NULL";
		assert source != null : "source is NULL";
		assert property != null : "property is NULL";

		PropertyReference<T, ?> ref = this.getPropertyReference (property);

		assert ref != null : "Property is not registered";
		assert ref.isWritable () : "property can not be written";

		ref.copyValue (dest, source);
	}

	public <V> Collection<V> getCollection (final Property<V> property, final T element)
	{
		this.log.trace ("getCollection: property={}, element={}", property, element);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		RelationshipReference<T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.getValue (element);
	}

	public <V> boolean add (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("add: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert value != null : "value is NULL";

		RelationshipReference<T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.addValue (element, value);
	}

	public <V> boolean remove (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("remove: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert value != null : "value is NULL";

		RelationshipReference<T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.removeValue (element, value);
	}
}
