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
 * Meta-data definition for an <code>Element</code> class.  This class contains
 * the meta-data for an <code>Element</code> Though an instance of this class
 * the referenced <code>Element</code> implementation class can be created and
 * its data can be manipulated.
 * <p>
 * In addition to containing the mata-data for an <code>Element</code>, this
 * class acts as a work-around for the lack of Generic type reification in
 * Java.  All of the operations on the <code>DomainModel</code> are expressed
 * in terms of the <code>Element</code> interfaces.  Out of necessity, the
 * operations in the builders and the <code>DataStore</code> are performed in
 * terms of the <code>Element</code> implementations.  It is necessary to
 * bridge between the interface and implementation in terms of type parameters,
 * to avoid a proliferation of <code>Element</code >implementation specific
 * builder/loader/query classes and/or unchecked casts (both of which are less
 * than ideal).
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
 * @param   <U> The <code>Element</code> implementation type
 * @see     Property
 * @see     Selector
 */

public class MetaData<T extends Element>
{
	/**
	 * Container for a single <code>MetaData</code> instance.  This interface
	 * acts to bury the <code>Element</code> implementation class, so that the
	 * callers can operate based on the interface type only.
	 *
	 * @param <T> The <code>Element</code> interface type
	 */

	private static interface Cell<T extends Element>
	{
		/**
		 * Inject the <code>MetaData</code> instance into the
		 * <code>Receiver</code>.
		 *
		 * @param  <R>      The result type of the <code>Receiver</code>
		 * @param  metadata The <code>MetaData</code> to inject, not null
		 * @param  receiver The <code>Receiver</code>, not null
		 *
		 * @return          The return value of the receiving method
		 */

		public abstract <R> R inject (MetaData<T> metadata, Receiver<T, R> reciever);

		/**
		 * Get a new Instance of the <code>Element</code> implementation class.
		 *
		 * @return The new <code>Element</code> instance
		 */

		public abstract T newInstance ();
	}

	/**
	 * Implementation of the <code>Cell</code> interface with the
	 * <code>Element</code> implementation class specified.
	 *
	 * @param <T> The <code>Element</code> interface type
	 * @param <U> The <code>Element</code> implementation type
	 */

	private static final class CellImpl<T extends Element, U extends T> implements Cell<T>
	{
		/** The <code>Element</code> implementation class */
		private final Class<U> element;

		/** Method reference to the <code>Element</code> constructor */
		private final Supplier<U> create;

		/**
		 *
		 * @param  element The <code>Element</code> implementation class, not
		 *                 null
		 * @param  create  Method reference to the <code>Element</code>
		 *                 constructor, not null
		 */

		public CellImpl (final Class<U> element, final Supplier<U> create)
		{
			assert element != null : "element is NULL";
			assert create != null : "create is NULL";

			this.element = element;
			this.create = create;
		}

		/**
		 * Inject the <code>MetaData</code> instance into the
		 * <code>Receiver</code>.
		 *
		 * @param  <R>      The result type of the <code>Receiver</code>
		 * @param  reciever The <code>Receiver</code>, not null
		 *
		 * @return          The return value of the receiving method
		 */

		public <R> R inject (final MetaData<T> metadata, final Receiver<T, R> receiver)
		{
			assert metadata != null : "metadata is NULL";
			assert receiver != null : "receiver is NULL";

			return receiver.apply (metadata, this.element);
		}

		/**
		 * Get a new Instance of the <code>Element</code> implementation class.
		 *
		 * @return The new <code>Element</code> instance
		 */

		public T newInstance ()
		{
			return this.create.get ();
		}
	}

	/** The Logger */
	private final Logger log;

	/** The <code>MetaData</code> for the super class */
	private final MetaData<? super T> parent;

	/** The <code>Element</code> class represented by the <code>MetaData</code> */
	private final Class<T> type;

	/** The <code>Property</code> instances associated with the interface */
	private final Map<String, Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Map<String, Selector> selectors;

	/** <code>Property</code> to <code>Reference</code> instance mapping */
	private final Map<Property<?>, PropertyReference<T, ?>> references;

	/** Implementation classes*/
	private final Map<Class<?>, Cell<T>> implementations;

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

	protected MetaData (final Class<T> type, final MetaData<? super T> parent, final Map<String, Property<?>> properties, final Map<Property<?>, PropertyReference<T, ?>> references, final Map<String, Selector> selectors)
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

		this.implementations = new HashMap<> ();
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
	 * Add an <code>Element</code> implementation class to the
	 * <code>MetaData</code>.
	 *
	 * @param  <U>    The <code>Element</code> implementation type
	 * @param  impl   The <code>Element</code> implementation class, not null
	 * @param  create Method reference to the implementation constructor, not
	 *                null
	 */

	public <U extends T> void addImplementation (final Class<U> impl, final Supplier<U> create)
	{
		this.log.trace ("addImplementation: impl={}, create={}", impl, create);

		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";
		assert ! this.implementations.containsKey (impl) : "Implementation class is already registered";

		this.implementations.put (impl, new CellImpl<T, U> (impl, create));
	}

	/**
	 * Create a new instance of the <code>Element</code>.
	 *
	 * @return The new <code>Element</code> instance
	 */

	public T createElement (final Class<? extends T> element)
	{
		this.log.trace ("createElement: element={}", element);

		assert element != null : "element is NULL";
		assert this.implementations.containsKey (element) : "element implementation class is not registered";

		return this.implementations.get (element).newInstance ();
	}

	/**
	 * Inject a <code>MetaData</code> instance into the <code>Receiver</code>.
	 * The <code>MetaData</code> instance will correspond to the specified
	 * <code>Element</code> implementation class, and must exist in the
	 * <code>Container</code>.
	 *
	 * @param  <R>      The result type of the <code>Receiver</code>
	 * @param  element  The <code>Element</code> implementation class, not null
	 * @param  receiver The <code>Receiver</code>, not null
	 *
	 * @return          The return value of the receiving method
	 */

	public <R> R inject (final Class<?> element, final Receiver<T, R> receiver)
	{
		this.log.trace ("inject: element={}, reciever={}", element, receiver);

		assert element != null : "element is NULL";
		assert receiver != null : "Receiver is NULL";
		assert this.implementations.containsKey (element) : "element not registered";

		return this.implementations.get (element).inject (this, receiver);
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
