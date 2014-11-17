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
 *
 * @param <T>
 *
 * @author James E. Stark
 * @version 1.0
 */

public abstract class Manager<T extends DomainModelElement>
{
	/**
	 * The DomainModel instance which owns this manager.
	 */

	protected final DomainModel model;

	/**
	 * The domain model type for this manager.  Used to fetch the implementation
	 * class.
	 */

	private final DomainModelType type;

	/**
	 * Create the Abstract Domain Model Manager.
	 *
	 * @param model The DomainModel instance which owns this manager.
	 * @param type The DomainModel Type for this manager.
	 */
	
	protected Manager (DomainModel model, DomainModelType type)
	{
		this.model = model;
		this.type = type;
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param id The value of the primary key of the object to retrieve.
	 * @return The requested object.
	 */

	public T fetchById (Long id)
	{
		return null;
	}
	
	/**
	 * Retrieve a list of all of the enties from the underlying data store.
	 *
	 * @return A list of objects.
	 */
	
	public List<T> fetchAll ()
	{
		return null;
	}

	/**
	 * Insert an entity into the domain model and the underlying data store. This
	 * method is a convience method which performs a non-recursive insert of the
	 * given entity into the domain model and underlying data store.
	 *
	 * @see AbstractManager#insert(T entity, Boolean recursive) 
	 * @param entity The entity to insert into the domain model.
	 * @return A reference to the inserted entity.
	 */
	
	public final T insert (T entity)
	{
		return this.insert (entity, new Boolean (false));
	}

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param entity The entity to insert into the domain model.
	 * @param recursive <code>true</code> if dependent entities should also be
	 * inserted, <code>false</code> otherwise.
	 * @return A reference to the inserted entity.
	 */
	
	public T insert (T entity, Boolean recursive)
	{
		return null;
	}

	/**
	 * Remove an entity from the domain model and the underlying data store. This
	 * is a convienience method that performs a non-recursive removal of the 
	 * given entity from the domain model and underlying data store.
	 *
	 * @see AbstractManager#remove(T entity, Boolean recursive) 
	 * @param entity The entity to remove from the domain model.
	 */
	
	public final void remove (T entity)
	{
		this.remove (entity, new Boolean (false));
	}

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param entity The entity to remove from the domain model.
	 * @param recursive <code>true</code> if dependent entities should also be
	 * removed, <code>false</code> otherwise.
	 */
	
	public void remove (T entity, Boolean recursive)
	{
	}
}
