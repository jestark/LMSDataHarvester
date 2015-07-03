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

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * References to the methods for a <code>Property</code> in an
 * <code>Element</code> implementation.  This is a helper class to contain the
 * method references for the <code>Element</code> implementation classes.
 * Since a <code>Property</code> can be read-only or read-write, this class
 * contains an indicator to determine if the <code>Property</code> is writable
 * in the implementation class.  A write is attempted on a read-only
 * <code>Property</code> the result will be a
 * <code>NullPointerException</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param  <T> The implementation type of the <code>Element</code>
 * @param  <V> The type of the value stored in the <code>Element</code>
 */

final class PropertyReference<T extends Element, U extends T, V>
{
	/** Method reference to getting values */
	final Function<T, V> get;

	/** Method reference for setting values */
	final BiConsumer<U, V> set;

	/**
	 * Create the <code>Reference</code>.
	 *
	 * @param  get Method reference to get the value, not null
	 * @param  set Method reference to set the value, may be null
	 */

	public PropertyReference (final Function<T, V> get, final BiConsumer<U, V> set)
	{
		assert get != null : "get method reference is NULL";

		this.get = get;
		this.set = set;
	}

	/**
	 * Determine if the value for the reference can be written.
	 *
	 * @return <code>true</code> if the value can be written, <code>false</code>
	 *         otherwise
	 */

	public boolean isWritable ()
	{
		return this.set != null;
	}

	/**
	 * Get the value from the specified <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The value from the <code>Element</code>
	 */

	public V getValue (final T element)
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

	public void setValue (final U element, final V value)
	{
		assert element != null : "element is NULL";
		assert this.isWritable () : "element is Read-Only";

		this.set.accept (element, value);
	}

	/**
	 * Copy a value from the source <code>Element</code> to the destination
	 * <code>Element</code>
	 *
	 * @param  dest     The destination <code>Element</code>, not null
	 * @param  source   The source <code>Element</code>, not null
	 */

	public void copyValue (final U dest, final T source)
	{
		assert dest != null : "dest is NULL";
		assert source != null : "source is NULL";
		assert this.isWritable () : "element is Read-Only";

		this.set.accept (dest, this.get.apply (source));
	}
}
