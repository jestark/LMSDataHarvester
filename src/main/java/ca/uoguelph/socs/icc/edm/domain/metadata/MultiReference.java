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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

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
 * @param  <T> The type of the <code>Element</code>
 * @param  <V> The type of the value stored in the <code>Element</code>
 */

public final class MultiReference<T extends Element, V extends Element> implements Reference<T, V>
{
	/** Method reference to getting values */
	final Function<T, Collection<V>> get;

	/** Method reference for adding values */
	final BiPredicate<T, V> add;

	/** Method reference for removing values */
	final BiPredicate<T, V> remove;

	/**
	 * Create the <code>MultiReference</code> for the specified values.
	 *
	 * @param  get      Method reference to get the value, not null
	 * @param  add      Method reference to add a value, not null
	 * @param  remove   Method reference to remove a value, not null
	 */

	public static <T extends Element, V extends Element> MultiReference<T, V> of (
			final Function<T, Collection<V>> get,
			final BiPredicate<T, V> add,
			final BiPredicate<T, V> remove)
	{
		assert get != null : "get is NULL";
		assert add != null : "add is NULL";
		assert remove != null : "remove is NULL";

		return new MultiReference<T, V> (get, add, remove);
	}

	/**
	 * Create the <code>MultiReference</code>.
	 *
	 * @param  get      Method reference to get the values, not null
	 * @param  add      Method reference to add a value, not null
	 * @param  remove   Method reference to remove a value, not null
	 */

	private MultiReference (
			final Function<T, Collection<V>> get,
			final BiPredicate<T, V> add,
			final BiPredicate<T, V> remove)
	{
		this.get = get;
		this.add = add;
		this.remove = remove;
	}

	/**
	 * Compare two <code>MultiReference</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>MultiReference</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>MultiReference</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof MultiReference)
				&& Objects.equals (this.get, ((MultiReference) obj).get)
				&& Objects.equals (this.add, ((MultiReference) obj).add)
				&& Objects.equals (this.remove, ((MultiReference) obj).remove);
	}

	/**
	 * Compute a hashCode for the <code>MultiReference</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.get, this.add, this.remove);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>MultiReference</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>MultiReference</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("get", this.get)
			.add ("add", this.add)
			.add ("remove", this.remove)
			.toString ();
	}

	/**
	 * Determine if this reference can write to the specified value.  For a
	 * <code>MultiReference</code> this will always return <code>true</code>.
	 *
	 * @return <code>true</code> is the value is read-write, <code>false</code>
	 *         if the value is read-only
	 */

	@Override
	public boolean isWritable ()
	{
		return true;
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

	@Override
	public boolean hasValue (final T element, final @Nullable Object value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";

		return this.get.apply (element).contains (value);
	}

	/**
	 * Get the <code>Stream</code> containing values from the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          A <code>Stream</code> containing the values from the
	 *                  <code>Element</code>
	 */

	@Override
	public Stream<V> stream (final T element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element).stream ();
	}

	/**
	 * Add the specified value to the <code>Element</code>.  This method adds
	 * the specified value to the <code>Collection</code> associated with the
	 * <code>Property</code> instance.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be written into the <code>Element</code>,
	 *                 not null
	 *
	 * @return         <code>true</code> if the value was written into the
	 *                 <code>Element</code>, <code>false</code> otherwise
	 */

	@Override
	public boolean setValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert this.add != null : "element is Read-Only";

		return this.add.test (element, value);
	}

	/**
	 * Remove the specified value from the specified <code>Element</code>
	 * instance.  This method removes the specified value to the
	 * <code>Collection</code> associated with the <code>Property</code>
	 * instance.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @param  value   The value to be removed from the <code>Element</code>,
	 *                 not null
	 *
	 * @return         <code>true</code> if the value was removed from the
	 *                 <code>Element</code>, <code>false</code> otherwise
	 */

	@Override
	public boolean clearValue (final T element, final V value)
	{
		assert element != null : "element is NULL";
		assert value != null : "value is NULL";
		assert this.remove != null : "element is Read-Only";

		return this.remove.test (element, value);
	}
}
