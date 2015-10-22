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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Filter <code>Element</code> instances based on the values of its associated
 * <code>Property</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> interface type
 */

public final class Filter<T extends Element>
{
	/** The <code>MetaData</code> for the <code>Element</code> implementation */
	private final MetaData<T> metadata;

	/** The <code>Selector</code> from which the <code>Filter</code> is created */
	private final Selector selector;

	/** <code>Filter</code> parameters */
	private final Map<Property<?>, Object> values;

	/**
	 * Create the <code>Filter</code> from the specified <code>MetaData</code>
	 * using the supplied <code>Selector</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  selector The <code>Selector</code>, not null
	 */

	public Filter (final MetaData<T> metadata, final Selector selector)
	{
		assert metadata != null : "metadata is NULL";
		assert selector != null : "selector is NULL";

		this.metadata = metadata;
		this.selector = selector;

		this.values = new HashMap<Property<?>, Object> ();

		selector.getProperties ()
			.stream ()
			.forEach ((x) -> this.values.put (x, null));
	}

	/**
	 * Compare two <code>Filter</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Filter</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>Filter</code> instances
	 *             are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Filter)
			&& Objects.equals (this.selector, ((Filter) obj).selector);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Selector</code> instance.
	 *
	 * @return The hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.selector);
	}

	/**
	 * Clear the <code>Filter</code>.
	 */

	public void clear ()
	{
		this.values.replaceAll ((k, v) -> null);
	}

	/**
	 * Get the <code>MetaData</code>, used to create the <code>Filter</code>
	 *
	 * @return The <code>MetaData</code>
	 */

	public MetaData<T> getMetaData ()
	{
		return this.metadata;
	}

	/**
	 * Get the <code>Selector</code>, used to create the <code>Filter</code>
	 *
	 * @return The <code>Selector</code>
	 */

	public Selector getSelector ()
	{
		return this.selector;
	}

	/**
	 * Get the value associated with the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the <code>Property</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The value associated with the <code>Property</code>
	 */

	public <V> V getValue (final Property<V> property)
	{
		assert property != null : "property is NULL";
		assert (this.values.containsKey (property)) : "invalid property";

		return (property.getPropertyType ()).cast (this.values.get (property));
	}

	/**
	 * Set the value for the specified <code>Property</code>.
	 *
	 * @param  <V>      The type of the <code>Property</code>
	 * @param  property The <code>Property</code>, not null
	 * @param  value    The value to be set, not null
	 */

	public <V> void setValue (final Property<V> property, final V value)
	{
		assert property != null : "property is NULL";
		assert value != null : "value is NULL";
		assert (this.values.containsKey (property)) : "invalid property";

		this.values.put (property, value);
	}

	/**
	 * Set all of the values in the <code>Filter</code> from the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code>, not null
	 */

	public void setAllValues (final T element)
	{
		assert element != null : "element is NULL";

		this.values.replaceAll ((k, v) -> this.metadata.getValue (k, element));
	}

	/**
	 * Test the specified <code>Element</code> instance against the values
	 * which are loaded into the <code>Filter</code>.
	 *
	 * @param  element The <code>Element</code>, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 matches all of the values in the <code>Filter</code>,
	 *                 <code>false</code> otherwise
	 */

	public boolean test (final T element)
	{
		assert element != null : "element is NULL";

		return this.values.keySet ()
			.stream ()
			.allMatch ((x) -> (x.hasFlags (Property.Flags.MULTIVALUED))
					? this.metadata.getValues (x, element).contains (this.values.get (x))
					: this.values.get (x).equals (this.metadata.getValue (x, element)));
	}

	/**
	 * Return the number of different values in the <code>Filter</code>.
	 *
	 * @return The number of values
	 */

	public int size ()
	{
		return this.values.size ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>Filter</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Filter</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("selector", this.selector)
			.add ("values", this.values)
			.toString ();
	}
}
