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

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Abstract representation of the inverse relationship.  All relationships
 * are modelled bi-directionally, with instances of the
 * <code>Relationship</code> interface modelling the owning side (the side
 * from which an operation is initiated).  Implementations of this interface
 * model the other side, which does the actual work of adding or breaking
 * the relationship.  This is an internal interface, who's instances should
 * only be called from instances of the <code>Relationship</code> interface.
 *
 * @version 1.0
 * @param   <T> The type of the owning <code>Element</code>
 * @param   <V> The type of the associated <code>Element</code>
 */

public interface InverseRelationship<T extends Element, V extends Element>
{
	/**
	 * Insert the specified value into the specified <code>Element</code> to
	 * create the relationship.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be inserted, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 inserted, <code>false</code> otherwise
	 */

	public abstract boolean insert (final T element, final V value);

	/**
	 * Remove the specified value from the specified <code>Element</code> to
	 * break the relationship.
	 *
	 * @param  element The <code>Element</code> to operate on, not null
	 * @param  value   The <code>Element</code> to be removed, not null
	 *
	 * @return         <code>true</code> if the value was successfully
	 *                 removed, <code>false</code> otherwise
	 */

	public abstract boolean remove (final T element, final V value);
}
