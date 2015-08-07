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

import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Meta-data definition for an <code>Element</code> class.  This class contains
 * the meta-data for an <code>Element</code> Though an instance of this class
 * the referenced <code>Element</code> implementation class can be created and
 * its data can be manipulated.
 * <p>
 * In addition to containing the meta-data for an <code>Element</code>, this
 * class acts as a work-around for the lack of Generic type reification in
 * Java.  All of the operations on the <code>DomainModel</code> are expressed
 * in terms of the <code>Element</code> interfaces.  Out of necessity, the
 * operations in the <code>DataStore</code> are performed in terms of the
 * <code>Element</code> implementations.  It is necessary to bridge between the
 * interface and implementation in terms of type parameters, to avoid a
 * proliferation of <code>Element</code >implementation specific loader/query
 * classes and/or unchecked casts (both of which are less than ideal).
 * <p>
 * The <code>MetaData</code> instances, by their nature, know the details of
 * both the <code>Element</code> interfaces and implementations.  To bridge
 * between the <code>Element</code> interface and implementation, an interface
 * for the operation is defined in terms of the <code>Element</code> interface,
 * taking the interface type as its type parameter.  The interface is then
 * implemented by a class that will know the implementation details of the
 * <code>Element</code>, usually via a second type parameter.  As a result the
 * implementation details of the <code>Element</code> only need to be known
 * when an instance of the operation class is created.
 * <p>
 * When on operation is created, the <code>MetaData</code> is used to provide
 * necessary information about the <code>Element</code> implementation,
 * including the necessary type parameter, which is not known to the caller.
 * The type parameter is supplied via a call from the <code>Container</code> to
 * class which implements the <code>Receiver</code> interface.  Generally,
 * classes that implement the <code>Receiver</code> interface will a builder
 * and will call the <code>inject</code> method as final build operation.
 * <p>
 * Ideally a functional interface would be used instead of requiring a class
 * for callback operation, however the functional interfaces do not work with
 * the unknown type parameters.
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
	 * Get the Java type of the <code>Element</code> represented by the
	 * <code>MetaData</code>.
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
	 * Inject the <code>MetaData</code> instance into the
	 * <code>Receiver</code>.
	 *
	 * @param  <R>      The result type of the <code>Receiver</code>
	 * @param  receiver The <code>Receiver</code>, not null
	 *
	 * @return          The return value of the receiving method
	 */

	public abstract <R> R inject (Receiver<T, R> receiver);

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
}
