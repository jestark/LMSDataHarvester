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

import java.util.stream.Stream;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a reference to a value contained in an <code>Element</code>
 * which is described by a <code>Property</code>.  The interface defines methods
 * which are common to all of the values contained in an <code>Element</code>
 * regardless of their type.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code>
 * @param   <V> The type of the value
 * @see     MetaData
 * @see     Property
 */

public interface Reference<T extends Element, V>
{
	/**
	 * Get the <code>Element</code> interface class upon which this
	 * <code>Reference</code> operates.
	 *
	 * @return The <code>Element</code> interface class
	 */

	public abstract Class<T> getElementClass ();

	/**
	 * Get the <code>Property</code> representing the value which this
	 * <code>Reference</code> accesses.
	 *
	 * @return the <code>Property</code>
	 */

	public abstract Property<T, V> getProperty ();

	/**
	 * Determine if the value contained in the <code>Element</code> has the
	 * specified value.  If the <code>Reference</code> is a singe value, then
	 * this method is equivalent to calling the <code>equals</code> method on
	 * the value.  This method is equivalent to calling the
	 * <code>contains</code> method on the associated <code>Collection</code>
	 * for mult-valued <code>Reference</code> instances.
	 *
	 * @param  element  The <code>Element</code> containing the value, not null
	 * @param  value    The value to test, not null
	 *
	 * @return <code>true</code> if the <code>Element</code> contains the
	 *         specified value, <code>false</code> otherwise.
	 */

	public abstract boolean hasValue (T element, V value);

	/**
	 * Get a <code>Stream</code> containing all of the values from the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          A <code>Stream</code> containing all of the values from
	 *                  the <code>Element</code>
	 */

	public abstract Stream<V> stream (T element);
}
