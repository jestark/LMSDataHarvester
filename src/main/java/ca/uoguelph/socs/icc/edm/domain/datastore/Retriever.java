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

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Retrieve an <code>Element</code> instance from the <code>DataStore</code>
 * which corresponds to the supplied <code>Element</code> instance.
 * Implementations of this interface aid in the translation of
 * <code>Element</code> instances between multiple <code>DataStore</code>
 * instances.
 */

public interface Retriever<T extends Element>
{
	/**
	 * Retrieve the <code>Element</code> instance from the
	 * <code>DataStore</code> corresponding specified <code>Element</code>
	 * instance.  If the specified <code>Element</code> instance is already in
	 * the <code>DataStore</code> then that will be returned.  Otherwise, this
	 * method will return the matching <code>Element</code> instance from the
	 * <code>DataStore</code> or <code>null</code> if there is no matching
	 * <code>Element</code> instance in the <code>DataStore</code>.
	 * <p>
	 * This method will ensure that the <code>Element</code> instance returned
	 * from the <code>DataStore</code> is identical to the supplied instance by
	 * comparing them using the <code>equalsAll</code> method.  If the
	 * <code>Element</code> instances are not identical, an
	 * <code>IllegalStateException</code> will be thrown.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 * @return         The <code>Element</code> instance in the
	 *                 <code>DataStore</code> or <code>null</code>
	 *
	 * @throws IllegalStateException if the <code>Element</code> instance in the
	 *                               <code>DataStore</code> is not identical to
	 *                               the supplied <code>Element</code> instance
	 */

	public abstract T fetch (final T element);
}
