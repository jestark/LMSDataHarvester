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

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * References to the methods for a <code>Property</code> in an
 * <code>Element</code> implementation.  This is a helper class to contain the
 * method references for the <code>Element</code> implementation classes.  A
 * write is attempted on a read-only <code>Property</code> the result will be a
 * <code>IllegalStateException</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code>
 * @param   <V> The type of the value stored in the <code>Element</code>
 */

final class SingleReference<T extends Element, V> implements Reference<T, V>
{
	/** Method reference to getting values */
	final Function<T, V> get;

	/** Method reference for setting values */
	final @Nullable BiConsumer<T, V> set;

	/**
	 * Create the <code>Single</code> reference for the specified values.
	 *
	 * @param  get     Method reference to get the value, not null
	 */

	public static <T extends Element, V> SingleReference<T, V> of (final Function<T, V> get)
	{
		assert get != null : "get is NULL";

		return new SingleReference<T, V> (get, null);
	}

	/**
	 * Create the <code>Single</code> reference for the specified values.
	 *
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, not null
	 */

	public static <T extends Element, V> SingleReference<T, V> of (final Function<T, V> get, final BiConsumer<T, V> set)
	{
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";

		return new SingleReference<T, V> (get, set);
	}

	/**
	 * Create the <code>SingleReference</code>.
	 *
	 * @param  get Method reference to get the value, not null
	 * @param  set Method reference to set the value, may be null
	 */

	private SingleReference (final Function<T, V> get, final @Nullable BiConsumer<T, V> set)
	{
		this.get = get;
		this.set = set;
	}

	/**
	 * Compare two <code>SingleReference</code> instances to determine if
	 * they are equal.
	 *
	 * @param  obj The <code>SingleReference</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>SingleReference</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof SingleReference)
				&& Objects.equals (this.get, ((SingleReference) obj).get)
				&& Objects.equals (this.set, ((SingleReference) obj).set);
	}

	/**
	 * Compute a hashCode for the <code>SingleReference</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.get, this.set);
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>SingleReference</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>SingleReference</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("get", this.get)
			.add ("set", this.set)
			.toString ();
	}

	/**
	 * Determine if value is read-only or if it is read-write.
	 *
	 * @return <code>true</code> is the value is read-write, <code>false</code>
	 *         if the value is read-only
	 */

	@Override
	public boolean isWritable ()
	{
		return this.set != null;
	}

	/**
	 * Determine if the value contained in the <code>Element</code> has the
	 * specified value.
	 * <p>
	 * This method is equivalent to calling the <code>equals</code> method on
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

		return this.get.apply (element).equals (value);
	}

	/**
	 * Get a <code>Stream</code> containing the value from the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          A <code>Stream</code> containing the value from the
	 *                  <code>Element</code>
	 */

	@Override
	public Stream<V> stream (final T element)
	{
		assert element != null : "element is NULL";

		V value = this.get.apply (element);

		return (value != null) ? Stream.of (value) : Stream.empty ();
	}

	/**
	 * Get the value from the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value from the <code>Element</code>
	 */

	@CheckReturnValue
	public V getValue (final T element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element);
	}

	/**
	 * Add the specified value to the <code>Element</code>.  This method
	 * unconditionally replaces the value stored in the specified
	 * <code>Element</code> with the specified value.
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
		assert this.set != null : "value is Read-Only";

		this.set.accept (element, value);

		return true;
	}

	/**
	 * Remove the specified value from the specified <code>Element</code>
	 * instance.  This method sets the corresponding value to <code>null</code>
	 * if it is currently equal to the specified value.
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
		assert this.set != null : "value is Read-Only";

		boolean result = false;

		if (this.get.apply (element).equals (value))
		{
			this.set.accept (element, null);
			result = true;
		}

		return result;
	}
}
