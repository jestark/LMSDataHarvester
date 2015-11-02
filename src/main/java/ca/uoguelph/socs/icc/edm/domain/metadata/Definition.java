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

import java.util.function.Predicate;
import java.util.function.Supplier;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

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
	private final Definition<? super T> parent;

	/** The <code>Element</code> class represented by the <code>Definition</code> */
	private final Class<T> type;

	/** The <code>Property</code> instances associated with the interface */
	private final Set<Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Set<Selector> selectors;

	/** The <code>Relationship</code> instances for the interface */
	private final Map<Class<?>, Relationship<T, ?>> relationships;

	/** <code>Property</code> to <code>PropertyReference</code> instance mapping */
	private final Map<Property<?>, PropertyReference<T, ?>> prefs;

	/** <code>Property</code> to <code>RelationshipReference</code> instance mapping*/
	private final Map<Property<?>, RelationshipReference<?, ?>> rrefs;

	/**
	 * Get the <code>DefintionBuilder</code> for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <P>    The <code>Element</code> interface type of the parent
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  type   The <code>Element</code> interface class, not null
	 * @param  parent The parent <code>Element</code> class, not null
	 *
	 * @return        The <code>DefinitionBuilder</code> instance
	 */

	public static <P extends Element, T extends P> DefinitionBuilder<T> getBuilder (final Class<T> type, final Class<P> parent)
	{
		assert type != null : "type is NULL";
		assert ! ((type == Element.class) ^ (parent == null));
		assert (parent == null) || (parent.isAssignableFrom (type)) : "type is not derived from parent";

		Definition<P> pdef = (parent != null) ? (Definition<P>) Container.getInstance ().getMetaData (parent) : null;

		return new DefinitionBuilder<T> (type, pdef);
	}

	/**
	 * Create the <code>MetaData</code>.
	 *
	 * @param  type          The <code>Element</code> interface class, not null
	 * @param  parent        The parent </code>Element</code> class, not null
	 * @param  properties    The <code>Set</code> of <code>Property</code>
	 *                       instances, not null
	 * @param  selectors     The <code>Set</code> of <code>Selector</code>
	 *                       instances, not null
	 * @param  relationships The <code>Map</code> of <code>Relationship</code>
	 *                       instances, not null
	 * @param  prefs         The <code>Map</code> of
	 *                       <code>PropertyReference</code> instances, not null
	 * @param  rrefs         The <code>Map</code> of
	 *                       <code>RelationshipReference</code> instances, not
	 *                       null
	 */

	protected Definition (final Class<T> type,
			final Definition<? super T> parent,
			final Set<Property<?>> properties,
			final Set<Selector> selectors,
			final Map<Class<?>, Relationship<T, ?>> relationships,
			final Map<Property<?>, PropertyReference<T, ?>> prefs,
			final Map<Property<?>, RelationshipReference<T, ?>> rrefs)
	{
		assert type != null : "type is NULL";
		assert ! ((type == Element.class) ^ (parent == null));
		assert properties != null : "properties is NULL";
		assert selectors != null : "selectors is NULL";
		assert relationships != null : "relationships is NULL";
		assert prefs != null : "prefs is NULL";
		assert rrefs != null : "rrefs is NULL";

		this.log = LoggerFactory.getLogger (Definition.class);

		this.type = type;
		this.parent = parent;
		this.selectors = new HashSet<> (selectors);
		this.properties = new HashSet<> (properties);
		this.relationships = new HashMap<> (relationships);
		this.prefs = new HashMap<> (prefs);
		this.rrefs = new HashMap<> (rrefs);
	}

	/**
	 * Get the interface type of the <code>Element</code> represented by the
	 * <code>MetaData</code>.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	@Override
	public Class<T> getElementType ()
	{
		return this.type;
	}

	/**
	 * Get the implementation type of the <code>Element</code> represented by
	 * the <code>MetaData</code>.
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
		return (this.parent != null) ? this.parent.getElementType () : null;
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
		Set<Property<?>> properties = (this.parent != null) ? this.parent.getProperties () : new HashSet<> ();
		properties.addAll (this.properties);

		return properties;
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
	public <V extends Element> Relationship<? super T, V> getRelationship (final Class<V> type)
	{
		assert type != null : "type is NULL";

		Relationship<? super T, V> relationship = (Relationship<T, V>) this.relationships.get (type);

		if ((relationship == null) && (this.parent != null))
		{
			relationship = (Relationship<? super T, V>) this.parent.getRelationship (type);
		}

		return relationship;
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Selector</code> instances
	 */

	@Override
	public Set<Selector> getSelectors ()
	{
		Set<Selector> selectors = (this.parent != null) ? this.parent.getSelectors () : new HashSet<> ();
		selectors.addAll (this.selectors);

		return selectors;
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
	private <V> PropertyReference<? super T, V> getPropertyReference (final Property<V> property)
	{
		assert property != null : "property is NULL";

		PropertyReference<? super T, V> ref = (PropertyReference<T, V>) this.prefs.get (property);

		if ((ref == null) && (this.parent != null))
		{
			ref = (PropertyReference<? super T, V>) this.parent.getPropertyReference (property);
		}

		return ref;
	}

	/**
	 * Get a <code>Stream</code> containing the value(s) for the
	 * <code>Property</code> which are assigned to the <code>Element</code>.
	 * This method will return a <code>Stream</code> containing zero of more
	 * values.
	 *
	 * @param  <V>      The type of the value
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value(s) corresponding to the <code>Property</code>
	 *                  in the <code>Element</code>
	 */

	@Override
	public <V> Stream<V> getStream (final Property<V> property, final T element)
	{
		this.log.trace ("getStream: property={}, element={}", property, element);

		assert property != null : "property is NULL";
		assert element != null : "element is NULL";

		return (property.hasFlags (Property.Flags.MULTIVALUED))
				? this.getRelationshipReference (property).stream (element)
				: this.getPropertyReference (property).stream (element);
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the specified <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a singe value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @param  <V>      The type of the value
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code> containing the value, not null
	 * @param  value    The value to test, not null
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public <V> boolean hasValue (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("hasValue: property={}, element={}, value={}", property, element, value);

		assert property != null : "property is NULL";
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return (property.hasFlags (Property.Flags.MULTIVALUED))
				? this.getRelationshipReference (property).hasValue (element, value)
				: this.getPropertyReference (property).hasValue (element, value);
	}

	/**
	 * Get the value corresponding to the specified <code>Property</code> from
	 * the specified <code>Element</code> instance.
	 *
	 * @param  <V>      The type of the value
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

		PropertyReference<? super T, V> ref = this.getPropertyReference (property);

		assert ref != null : "Property is not registered";

		return ref.getValue (element);
	}

	/**
	 * Set the value corresponding to the specified <code>Property</code> in
	 * the specified <code>Element</code> instance to the specified value.
	 *
	 * @param  <V>      The type of the value
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

		PropertyReference<? super T, V> ref = this.getPropertyReference (property);

		assert ref != null : "Property is not registered";
		assert ref.isWritable () : "property can not be written";

		ref.setValue (element, value);
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
	private <V> RelationshipReference<? super T, V> getRelationshipReference (final Property<V> property)
	{
		assert property != null : "property is NULL";

		RelationshipReference<? super T, V> ref = (RelationshipReference<T, V>) this.rrefs.get (property);

		if ((ref == null) && (this.parent != null))
		{
			ref = (RelationshipReference<? super T, V>) this.parent.getRelationshipReference (property);
		}

		return ref;
	}

	/**
	 * Get a <code>Collection</code> containing the values that are associated
	 * with the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the value
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The <code>Collection</code> of values associated with
	 *                  the <code>Property</code>
	 */

	@Override
	public <V> Collection<V> getValues (final Property<V> property, final T element)
	{
		this.log.trace ("getCollection: property={}, element={}", property, element);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		RelationshipReference<? super T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.getValue (element);
	}

	/**
	 * Add a value to the <code>Collection</code> of values which are
	 * associated with the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the value
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to add, not null
	 *
	 * @return          <code>true</code> if the value was successfully added
	 *                  to the element, <code>false</code> otherwise
	 */

	@Override
	public <V> boolean addValue (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("add: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert value != null : "value is NULL";

		RelationshipReference<? super T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.addValue (element, value);
	}

	/**
	 * Remove a value from the <code>Collection</code> of values which are
	 * associated with the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the value
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to remove, not null
	 *
	 * @return          <code>true</code> if the value was successfully removed
	 *                  from the element, <code>false</code> otherwise
	 */

	@Override
	public <V> boolean removeValue (final Property<V> property, final T element, final V value)
	{
		this.log.trace ("remove: property={}, element={}, value={}", property, element, value);

		assert element != null : "element is NULL";
		assert property != null : "property is NULL";
		assert value != null : "value is NULL";

		RelationshipReference<? super T, V> ref = this.getRelationshipReference (property);

		assert ref != null : "Property is not registered";

		return ref.removeValue (element, value);
	}

	/**
	 * Perform the specified operation on all of the <code>Relationship</code>
	 * instances for the <code>Element</code>.  This method performs the
	 * specified operation on all of the <code>Relationship</code> instances
	 * stored in this <code>Definition</code> instance and the linked
	 * <code>Definition</code> instances for the ancestor classes.
	 *
	 * @param  operation The operation to perform, not null
	 *
	 * @return           <code>true</code> if operation completed successfully,
	 *                   <code>false</code> otherwise
	 */

	private boolean processRelationships (final Predicate<? super Relationship<? super T, ?>> operation)
	{
		assert operation != null : "predicate is NULL";

		boolean result = true;
		Definition<? super T> def = this;

		while ((result == true) && (def != null))
		{
			result = def.relationships.entrySet ()
				.stream ()
				.map (Map.Entry::getValue)
				.allMatch (operation);

			def = def.parent;
		}

		return result;
	}

	/**
	 * Determine if the relationships for the specified <code>Element</code>
	 * instance can be safely connected.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean canConnect (final DataStore datastore, final T element)
	{
		this.log.trace ("canConnect: datastore={}, element={}", datastore, element);

		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		return this.processRelationships (x -> x.canConnect (datastore, element));
	}

	/**
	 * Determine if the relationships for the specified <code>Element</code>
	 * instance can be safely disconnected.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean canDisconnect (final DataStore datastore, final T element)
	{
		this.log.trace ("canDisconnect: datastore={}, element={}", datastore, element);

		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		return this.processRelationships (x -> x.canDisconnect (datastore, element));
	}

	/**
	 * Connect the relationships for the specified <code>Element</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean connect (final DataStore datastore, final T element)
	{
		this.log.trace ("connect: datastore={}, element={}", datastore, element);

		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		return this.processRelationships (x -> x.connect (datastore, element));
	}

	/**
	 * Disconnect the relationships for the specified <code>Element</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	@Override
	public boolean disconnect (final DataStore datastore, final T element)
	{
		this.log.trace ("disconnect: datastore={}, element={}", datastore, element);

		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		return this.processRelationships (x -> x.disconnect (datastore, element));
	}
}
