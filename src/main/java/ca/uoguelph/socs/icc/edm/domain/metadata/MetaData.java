/* Copyright (C) 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General public abstract License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General public abstract License for more details.
 *
 * You should have received a copy of the GNU General public abstract License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Collection;
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Meta-data definition for an <code>Element</code> class.  This interface
 * contains the meta-data for an <code>Element</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 * @see     Property
 * @see     Selector
 */

public interface MetaData<T extends Element>
{
	/**
	 * Get the interface type of the <code>Element</code> represented by the
	 * <code>MetaData</code>.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public abstract Class<T> getElementType ();

	/**
	 * Get the implementation type of the <code>Element</code> represented by
	 * the <code>MetaData</code> instance.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public abstract Class<? extends T> getElementClass ();

	/**
	 * Get the Java type of the parent <code>Element</code> for the class
	 * represented by the <code>MetaData</code>.
	 *
	 * @return The <code>Class</code> representing the parent interface type
	 */

	public abstract Class<? extends Element> getParentClass ();

	/**
	 * Get the <code>Property</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Property</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public abstract Property<?> getProperty (String name);

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

	public abstract <V> Property<V> getProperty (String name, Class<V> type);

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Property</code> instances
	 */

	public abstract Set<Property<?>> getProperties ();

	/**
	 * Get the <code>Relationship</code> instance for the specified
	 * <code>Element</code> interface class.
	 *
	 * @param  <V>  The <code>Element</code> type of the relationship target
	 * @param  type The <code>Element</code> interface class of the
	 *              relationship target, not null
	 *
	 * @return      The <code>Relationship</code>, may be null
	 */

	public abstract <V extends Element> Relationship<? super T, V> getRelationship (Class<V> type);

	/**
	 * Get the <code>Selector</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Selector</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public abstract Selector getSelector (String name);

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Selector</code> instances
	 */

	public abstract Set<Selector> getSelectors ();

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

	public abstract <V> V getValue (Property<V> property, T element);

	/**
	 * Set the value corresponding to the specified <code>Property</code> in
	 * the specified <code>Element</code> instance to the specified value.
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be set, may be null
	 */

	public abstract <V> void setValue (Property<V> property, T element, V value);

	/**
	 * Copy the value corresponding to the specified <code>Property</code> from
	 * the source <code>Element</code> to the destination <code>Element</code>
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  dest     The destination <code>Element</code>, not null
	 * @param  source   The source <code>Element</code>, not null
	 */

	public abstract void copyValue (Property<?> property, T dest, T source);

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

	public abstract <V> Collection<V> getValues (Property<V> property, T element);

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

	public abstract <V> boolean addValue (Property<V> property, T element, V value);

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

	public abstract <V> boolean removeValue (Property<V> property, T element, V value);

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

	public abstract boolean canConnect (DataStore datastore, T element);

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

	public abstract boolean canDisconnect (DataStore datastore, T element);

	/**
	 * Connect the relationships for the specified <code>Element</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	public abstract boolean connect (DataStore datastore, T element);

	/**
	 * Disconnect the relationships for the specified <code>Element</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> to process, not null
	 *
	 * @return           <code>true</code> if the relationship can be created,
	 *                   <code>false</code> otherwise
	 */

	public abstract boolean disconnect (DataStore datastore, T element);
}
