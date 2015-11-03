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

import java.util.function.BiConsumer;
import java.util.function.Function;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

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
 * @param  <T> The implementation type of the <code>Element</code>
 * @param  <V> The type of the value stored in the <code>Element</code>
 */

final class SingleReference<T extends Element, V> implements Accessor<T, V>
{
	/** Method reference to getting values */
	final Function<T, V> get;

	/** Method reference for setting values */
	final BiConsumer<T, V> set;

	/**
	 * Create the <code>Reference</code>.
	 *
	 * @param  get Method reference to get the value, not null
	 * @param  set Method reference to set the value, may be null
	 */

	public SingleReference (final Function<T, V> get, final BiConsumer<T, V> set)
	{
		assert get != null : "get method reference is NULL";

		this.get = get;
		this.set = set;
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
	public boolean hasValue (final T element, final V value)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (value, "value");

		return this.get.apply (element).equals (value);
	}

	/**
	 * Get the a <code>Stream</code> containing value from the specified
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
		Preconditions.checkNotNull (element, "element");

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

	@Override
	@CheckReturnValue
	public V getValue (final T element)
	{
		Preconditions.checkNotNull (element, "element");

		return this.get.apply (element);
	}

	/**
	 * Enter the specified value into the specified <code>Element</code>
	 * instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 * @param  value    The value to be set, may be null
	 */

	@Override
	public void setValue (final T element, final @Nullable V value)
	{
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkState (this.set != null, "value is Read-Only");

		this.set.accept (element, value);
	}
}
