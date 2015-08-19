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

import java.util.Set;
import java.util.Collection;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * <code>MetaData</code> definition for an <code>Element</code> implementation
 * class.  This class contains <code>MetaData</code> definition for an
 * <code>Element</code> implementation.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @param   <U> The <code>Element</code> implementation type
 */

public final class Implementation<T extends Element, U extends T> implements Creator<T>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Defintiion</code> */
	private final Definition<T> definition;

	/** The <code>Element</code> implementation class */
	private final Class<U> element;

	/** Method reference to the <code>Element</code> constructor */
	private final Supplier<U> create;

	/**
	 * Create the <code>MetaData</code> instance for an <code>Element</code>
	 * implementation class.
	 *
	 * @param  <T>        The <code>Element</code> interface type
	 * @param  <U>        The <code>Element</code> implementation type
	 * @param  definition The <code>Definition</code>, not null
	 * @param  element    The <code>Element</code> implementation class, not
	 *                    null
	 * @param  create     Method reference to the constructor, not null
	 *
	 * @return            The <code>MetaData</code> instance
	 */

	public static <T extends Element, U extends T> Implementation<T, U> getInstance (final Definition<T> definition, final Class<U> element, final Supplier<U> create)
	{
		assert definition != null : "definition is NULL";
		assert element != null : "element is NULL";
		assert create != null : "create is NULL";
		assert definition.getElementClass ().isAssignableFrom (element) : "element is not an implementation of the class defined by the definition";

		Implementation<T, U> impl = new Implementation<T, U> (definition, element, create);
		Container.getInstance ().registerCreator (impl);

		return impl;
	}

	/**
	 * Create the <code>MetaData</code> instance for an <code>Element</code>
	 * implementation class.
	 *
	 * @param  <T>        The <code>Element</code> interface type
	 * @param  <U>        The <code>Element</code> implementation type
	 * @param  type       The <code>Element</code> interface class, not null
	 * @param  element    The <code>Element</code> implementation class, not
	 *                    null
	 * @param  create     Method reference to the constructor, not null
	 *
	 * @return            The <code>MetaData</code> instance
	 */

	public static <T extends Element, U extends T> Implementation<T, U> getInstance (final Class<T> type, final Class<U> element, final Supplier<U> create)
	{
		assert type != null : "type is NULL";
		assert element != null : "element is NULL";
		assert create != null : "create is NULL";
		assert Container.getInstance ().containsMetaData (type) : "type does not have MetaData";
		assert Container.getInstance ().getMetaData (type) instanceof Definition : "Type is not a Definition";

		return Implementation.getInstance ((Definition<T>) Container.getInstance ().getMetaData (type), element, create);
	}

	/**
	 * Create the <code>MeteData</code> instance.
	 *
	 * @param  definition The <code>Definition</code>, not null
	 */

	protected Implementation (final Definition<T> definition, final Class<U> element, final Supplier<U> create)
	{
		assert definition != null : "definition is NULL";
		assert element != null : "element is NULL";
		assert create != null : "create is NULL";

		this.log = LoggerFactory.getLogger (Implementation.class);

		this.definition = definition;
		this.element = element;
		this.create = create;
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
		return this.definition.getElementType ();
	}

	/**
	 * Get the implementation type of the <code>Element</code>.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	@Override
	public Class<U> getElementClass ()
	{
		return this.element;
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
		return this.definition.getElementClass ();
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

		return this.definition.getProperty (name);
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
	public <V> Property<V> getProperty (final String name, final Class<V> type)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";

		return this.definition.getProperty (name, type);
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
		return this.definition.getProperties ();
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
	public <V extends Element> Relationship<T, V> getRelationship (final Class<V> type)
	{
		assert type != null : "type is NULL";

		return this.definition.getRelationship (type);
	}

	/**
	 * Get the <code>Relationship</code> for the specified
	 * <code>Property</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the relationship target
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Relationship</code> instance
	 */

	@Override
	public <V extends Element> Relationship<T, V> getRelationship (final Property<V> property)
	{
		assert property != null : "property is NULL";

		return this.definition.getRelationship (property);
	}

	/**
	 * Get the <code>Set</code> for <code>Relationship</code> instances for the
	 * <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Relationship</code> instances
	 */

	@Override
	public Set<Relationship<T, ?>> getRelationships ()
	{
		return this.definition.getRelationships ();
	}

	/**
	 * Get the <code>Selector</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Selector</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	@Override
	public Selector getSelector (final String name)
	{
		assert name != null : "name is NULL";

		return this.definition.getSelector (name);
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
		return this.definition.getSelectors ();
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

		return this.definition.getValue (property, element);
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

		this.definition.setValue (property, element, value);
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

		this.definition.copyValue (property, dest, source);
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

		return this.definition.getValues (property, element);
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

		return this.definition.addValue (property, element, value);
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

		return this.definition.removeValue (property, element, value);
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

		return this.definition.canConnect (datastore, element);
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

		return this.definition.canDisconnect (datastore, element);
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

		return this.definition.connect (datastore, element);
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

		return this.definition.disconnect (datastore, element);
	}

	/**
	 * Inject the <code>MetaData</code> instance into the
	 * <code>Receiver</code>.
	 *
	 * @param  <R>      The result type of the <code>Receiver</code>
	 * @param  receiver The <code>Receiver</code>, not null
	 *
	 * @return          The return value of the receiving method
	 */

	@Override
	public <R> R inject (final Receiver<T, R> receiver)
	{
		assert receiver != null : "receiver is NULL";

		return receiver.apply (this, this.element);
	}

	/**
	 * Get a new Instance of the <code>Element</code> implementation class.
	 *
	 * @return The new <code>Element</code> instance
	 */

	@Override
	public T create ()
	{
		return this.create.get ();
	}
}
