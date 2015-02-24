/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;

/**
 * Top level interface for all operations involving the domain model and the
 * underlying data-store.  This class and its subclasses are responsible for
 * all operations related to adding, retrieving or removing elements from the
 * domain model and its underlying data-store.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to be processed
 */

public interface ElementManager<T extends Element>
{
	/**
	 * Retrieve a <code>Element</code> instance from the <code>DataStore</code>
	 * based upon is <code>DataStore</code> identifier.  
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> instance to retrieve, not null
	 *
	 * @return    The requested <code>Element</code> instance
	 */

	public abstract T fetchById (Long id);

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code> instances
	 * represented by the <code>ElementManager</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public abstract List<T> fetchAll ();

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>. This method is a convenience method which performs
	 * a non-recursive insert of the specified <code>Element</code> instance into
	 * the </code>DomainModel</code> and the underlying </code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to insert, not null
	 *
	 * @return         A reference to the <code>Element</code> instance in the
	 *                 <code>DataStore</code>
	 * @see    #insert(Element, Boolean) insert(T, Boolean)
	 */

	public abstract T insert (T element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element   The <code>Element</code> to insert, not null
	 * @param  recursive <code>true</code> if dependent <code>Element</code>
	 *                   instances should also be inserted, <code>false</code>
	 *                   otherwise, not null
	 *
	 * @return           A reference to the <code>Element</code> instance in the
	 *                   <code>DataStore</code>
	 */

	public abstract T insert (T entity, Boolean recursive);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>. This is a convenience method that performs a
	 * non-recursive removal of the specified <code>Element</code> instance from
	 * the <code>DomainModel</code> and the underlying <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to remove, not null
	 *
	 * @see    #remove(Element, Boolean) remove(T, Boolean)
	 */

	public abstract void remove (T entity);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element   The <code>Element</code> to remove, not null
	 *
	 * @param  recursive <code>true</code> if dependent <code>Element</code>
	 *                   instances should also be removed, <code>false</code>
	 *                   otherwise, not null
	 */

	public abstract void remove (T entity, Boolean recursive);
}
