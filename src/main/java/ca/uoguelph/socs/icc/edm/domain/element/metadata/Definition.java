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

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Meta-data definition for an <code>Element</code>.  This class is a meta-data
 * representation of an <code>Element</code>.  It uses a collection of
 * <code>Property</code> instances with their associated method references to
 * create and modify <code>Element</code> instances.  The
 * <code>DataStore</code> and <code>ElementBuilder</code> implementation use
 * this class indirectly.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The interface type of the <code>Element</code>
 * @param   <U> The implementation type of the <code>Element</code>
 */

public final class Definition<T extends Element, U extends T>
{
	/**
	 *
	 * @param  <T> The implementation type of the <code>Element</code>
	 * @param  <V> The type of the value stored in the <code>Element</code>
	 */

	protected static final class Reference<T extends Element, V>
	{
		/** Method reference to getting values */
		final Function<T, V> get;

		/** Method reference for setting values */
		final BiConsumer<T, V> set;

		/**
		 * Create the <code>Reference</code>.
		 *
		 * @param  get Method reference to get the value, not null
		 * @param  set Method reference to set the value, not null
		 */

		public Reference (final Function<T, V> get, final BiConsumer<T, V> set)
		{
			assert get != null : "get method reference is NULL";
			assert set != null : "set method reference is NULL";

			this.get = get;
			this.set = set;
		}

		/**
		 * Get the value from the specified <code>Element</code> instance.
		 *
		 * @param  element  The <code>Element</code>, not null
		 *
		 * @return          The value from the <code>Element</code>
		 */

		public V get (final T element)
		{
			assert element != null : "element is NULL";

			return this.get.apply (element);
		}

		/**
		 * Enter the specified value into the specified <code>Element</code>
		 * instance.
		 *
		 * @param  element  The <code>Element</code>, not null
		 * @param  value    The value to be set, may be null
		 */

		public void set (final T element, final V value)
		{
			assert element != null : "element is NULL";

			this.set.accept (element, value);
		}

		/**
		 * Copy a value from the source <code>Element</code> to the destination
		 * <code>Element</code>
		 *
		 * @param  dest     The destination <code>Element</code>, not null
		 * @param  source   The source <code>Element</code>, not null
		 */

		public void copy (final T dest, final T source)
		{
			assert dest != null : "dest is NULL";
			assert source != null : "source is NULL";

			this.set.accept (dest, this.get.apply (source));
		}
	}

	/** The interface class for the <code>Element</code> */
	private final Class<T> type;

	/** The implementation class for the <code>Element</code> */
	private final Class<U> impl;

	/** Method reference for creating new instances */
	private final Supplier<U> create;

	/** <code>Property</code> to <code>Reference</code> instance mapping */
	private final Map<Property<?>, Reference<U, ?>> references;

	/**
	 * Create the Meta-data <code>Definition</code>.
	 *
	 * @param  type       <code>Element</code> interface class, not null
	 * @param  impl       <code>Element</code> implementation class, not null
	 * @param  create     Method reference for creating new instances, not null
	 * @param  references <code>Property</code> to <code>Reference</code> map,
	 *                    not null
	 */

	protected Definition (final Class<T> type, final Class<U> impl, final Supplier<U> create, final Map<Property<?>, Reference<U, ?>> references)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";
		assert references != null : "references is NULL";

		this.type = type;
		this.impl = impl;
		this.create = create;
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
	private <V> Reference<U, V> getReference (final Property<V> property)
	{
		assert property != null : "reference is NULL";
		assert this.references.containsKey (property) : "Property is not registered";

		return (Reference<U, V>) this.references.get (property);
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
	 * Get the Java type of the <code>Element</code> implementation.
	 *
	 * @return The <code>Class</code> representing the implementation type
	 */

	public Class<U> getElementClass ()
	{
		return this.impl;
	}

	/**
	 * Create a new instance of the <code>Element</code>.
	 *
	 * @return The new <code>Element</code> instance
	 */

	public U createElement ()
	{
		return this.create.get ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances
	 * corresponding to the <code>Element</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Property</code>
	 *         instances for the <code>Element</code>
	 */

	public Set<Property<?>> getProperties ()
	{
		return new HashSet<Property<?>> (this.references.keySet ());
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

	public <V> V getValue (final Property<V> property, final U element)
	{
		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		return (this.getReference (property)).get (element);
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
		assert element != null : "element is NULL";
		assert property != null : "property is NULL";

		(this.getReference (property)).set (element, value);
	}

	/**
	 * Copy the value corresponding to the specified <code>Property</code> from
	 * the source <code>Element</code> to the destination <code>Element</code>
	 *
	 * @param  property The <code>Property</code>, not null
	 * @param  dest     The destination <code>Element</code>, not null
	 * @param  source   The source <code>Element</code>, not null
	 */

	public void copyValue (final Property<?> property, final U dest, final U source)
	{
		assert dest != null : "dest is NULL";
		assert source != null : "source is NULL";
		assert property != null : "property is NULL";

		(this.references.get (property)).copy (dest, source);
	}
}
