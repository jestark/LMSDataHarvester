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

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Wrapper class to override the <code>equals</code> and <code>hashcode</code>
 * methods of the stored <code>Element</code> instances.  We need the stored
 * <code>Element</code> instances to be accessed in terms of their references,
 * rather than their data.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The implementation type of the <code>Element</code>
 */

final class Wrapper<T extends Element>
{
	/** The <code>Element</code> instance which is being wrapped */
	private final T element;

	/**
	 * Create the <code>Wrapper</code>.
	 *
	 * @param  element The <code>Element</code>, not null
	 */

	public Wrapper (final T element)
	{
		this.element = element;
	}

	/**
	 * Compare the wrapped <code>Element</code> reference to the supplied
	 * <code>Object</code>.
	 *
	 * @param  obj The <code>Object</code>
	 *
	 * @return <code>true</code> is the references are the same,
	 *         <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return ((obj == this) || ((obj instanceof Wrapper) && (this.element == ((Wrapper) obj).unwrap ())));
	}

	/**
	 * Generate the hash code for the wrapped <code>Element</code> instance.
	 *
	 * @return The hash code.
	 */

	@Override
	public int hashCode ()
	{
		return System.identityHashCode (this.element);
	}

	/**
	 * Get the wrapped <code>Element</code> instance.
	 *
	 * @return The <code>Element</code> instance
	 */

	public T unwrap ()
	{
		return this.element;
	}
}
