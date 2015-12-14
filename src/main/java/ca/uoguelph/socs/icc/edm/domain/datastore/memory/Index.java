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

package ca.uoguelph.socs.icc.edm.domain.datastore.memory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * An <code>Index</code> used as the key for mapping unique <code>Element</code>
 * instances in the <code>MemDataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class Index<T extends Element>
{
	/** The <code>Selector</code> */
	private final Selector<T> selector;

	/** The <code>Element</code> implementation class */
	private final Class<? extends T> impl;

	/** The indexed values */
	private final List<?> keys;

	/**
	 * Create the <code>Index</code> from the supplied <code>Element</code>
	 * instance, using the specified <code>Selector</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 * @param  element  The <code>Element</code>, not null
	 * @return          The <code>Index</code>
	 */

	public static <T extends Element> Index<T> create (
			final Selector<T> selector,
			final Class<? extends T> impl,
			final T element)
	{
		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";
		assert element != null : "element is NULL";

		return new Index<T> (selector, impl, selector.getProperties ()
			.stream ()
			.flatMap (x -> x.stream (element))
			.collect (Collectors.collectingAndThen (Collectors.toList (), Collections::unmodifiableList)));
	}

	/**
	 * Create the index <code>Map</code> from the supplied <code>Map</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 * @param  values   The <code>Map</code> of index values, not null
	 * @return The <code>Index</code>
	 */

	public static <T extends Element> Index<T> create (
			final Selector<T> selector,
			final Class<? extends T> impl,
			final Map<Property<T, ?>, ?> values)
	{
		assert selector != null : "selector is NULL";
		assert impl != null : "impl is NULL";
		assert values != null : "values is NULL";

		return new Index<T> (selector, impl, values.entrySet ()
				.stream ()
				.map (x -> x.getValue ())
				.collect (Collectors.collectingAndThen (Collectors.toList (), Collections::unmodifiableList)));
	}

	/**
	 * Create the <code>Index</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 * @param  keys     The <code>List</code> of index values, not null
	 */

	private Index (
			final Selector<T> selector,
			final Class<? extends T> impl,
			final List<?> keys)
	{
		this.selector = selector;
		this.impl = impl;
		this.keys = keys;
	}

	/**
	 * Compare two <code>Index</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Index</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>true</code> if the two <code>Index</code> instances
	 *             are equal, <code>false</code> otherwise
	 */

	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Index)
			&& Objects.equals (this.selector, ((Index) obj).selector)
			&& Objects.equals (this.impl, ((Index) obj).impl)
			&& Objects.equals (this.keys, ((Index) obj).keys);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Index</code>.
	 *
	 * @return The hash code
	 */

	public int hashCode ()
	{
		return Objects.hash (this.selector, this.impl, this.keys);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Index</code>.
	 *
	 * @return A <code>String</code> representation of the <code>Index</code>
	 */

	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("selector", this.selector)
			.add ("impl", this.impl)
			.add ("keys", this.keys)
			.toString ();
	}

	/**
	 * Get the <code>Selector</code>, used to create the <code>Index</code>.
	 *
	 * @return The <code>Selector</code>
	 */

	public Selector<T> getSelector ()
	{
		return this.selector;
	}

	/**
	 * Get the implementation class for the indexed <code>Element</code>.
	 *
	 * @return The <code>Element</code> implementation class
	 */

	public Class<? extends T> getElementClass ()
	{
		return this.impl;
	}

	/**
	 * Get the <code>List</code> of values composing the <code>Index</code>.
	 *
	 * @return The <code>List</code> of index values
	 */

	public List<?> getKeys ()
	{
		return this.keys;
	}
}
