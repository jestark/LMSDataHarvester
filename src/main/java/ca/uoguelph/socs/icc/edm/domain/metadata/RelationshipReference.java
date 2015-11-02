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

import java.util.function.Function;
import java.util.function.BiPredicate;

import java.util.stream.Stream;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * References to the methods manipulate a <code>Collection</code> of values,
 * represented by a <code>Property</code>, in an <code>Element</code>
 * implementation.  This is a helper class to contain the method references for
 * the <code>Element</code> implementation classes.  Since a
 * <code>Property</code> can be read-only or read-write, this class contains an
 * indicator to determine if the <code>Property</code> is writable in the
 * implementation class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param  <T> The implementation type of the <code>Element</code>
 * @param  <V> The type of the value stored in the <code>Element</code>
 */

final class RelationshipReference<T extends Element, V>
{
	/** Method reference to getting values */
	final Function<T, Collection<V>> get;

	/** Method reference for adding values */
	final BiPredicate<T, V> add;

	/** Method reference for removing values */
	final BiPredicate<T, V> remove;

	/**
	 * Create the <code>RelationshipReference</code>.
	 *
	 * @param  get    Method reference to get the values, not null
	 * @param  add    Method reference to add a value, not null
	 * @param  remove Method reference to remove a value, not null
	 */

	public RelationshipReference (final Function<T, Collection<V>> get, final BiPredicate<T, V> add, final BiPredicate<T, V> remove)
	{
		assert get != null : "get method reference is NULL";
		assert add != null : "add method reference is NULL";
		assert remove != null : "remove method reference is NULL";

		this.get = get;
		this.add = add;
		this.remove = remove;
	}

	/**
	 * Get the value from the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value from the <code>Element</code>
	 */

	public Collection<V> getValue (final T element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element);
	}

	/**
	 * Add the specified value to the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be added, not null
	 */

	public boolean addValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert this.add != null : "element is Read-Only";

		return this.add.test (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be removed, not null
	 */

	public boolean removeValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert this.remove != null : "element is Read-Only";

		return this.remove.test (element, value);
	}

	/**
	 * Determine if the value contained in the <code>Element</code> has the
	 * specified value.
	 * <p>
	 * This method is equivalent to calling the <code>contains</code> method on
	 * the value.
	 *
	 * @param  element  The <code>Element</code> containing the value, not null
	 * @param  value    The value to test, not null
	 *
	 * @return <code>true</code> if the <code>Element</code> contains the
	 *         specified value, <code>false</code> otherwise.
	 */

	public boolean hasValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return this.get.apply (element).contains (value);
	}

	/**
	 * Get the a <code>Stream</code> containing values from the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          A <code>Stream</code> containing the values from the
	 *                  <code>Element</code>
	 */

	public Stream<V> stream (final T element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element).stream ();
	}
}
