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
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	public abstract T fetchById (Long id);

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	public abstract List<T> fetchAll ();

	/**
	 * Insert an entity into the domain model and the underlying data store. This
	 * method is a convenience method which performs a non-recursive insert of the
	 * given entity into the domain model and underlying data store.
	 *
	 * @param  entity The entity to insert into the domain model, not null
	 * @return        A reference to the inserted entity
	 * @see    #insert(Element, Boolean) insert(T, Boolean)
	 */

	public abstract T insert (T entity);

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to insert into the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   inserted, <code>false</code> otherwise, not null
	 * @return           A reference to the inserted entity
	 */

	public abstract T insert (T entity, Boolean recursive);

	/**
	 * Remove an entity from the domain model and the underlying data store. This
	 * is a convenience method that performs a non-recursive removal of the
	 * given entity from the domain model and underlying data store.
	 *
	 * @param  entity The entity to remove from the domain model, not null
	 * @see    #remove(Element, Boolean) remove(T, Boolean)
	 */

	public abstract void remove (T entity);

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to remove from the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   removed, <code>false</code> otherwise, not null
	 */

	public abstract void remove (T entity, Boolean recursive);
}
