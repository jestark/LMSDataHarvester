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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

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

public abstract class AbstractManager<T extends Element> implements ElementManager<T>
{
	/** The <code>DomainModel</code> instance which owns this manager. */
	protected final DomainModel model;

	private AbstractManagerFactory<T, ?, ?, ?> factory; 

	/** The logger */
	private final Log log;

	/**
	 * Create the <code>AbstractManager</code>.
	 *
	 * @param  model The <code>DomainModel</code> instance for this manager, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected AbstractManager (DomainModel model)
	{
		this.model = model;

		this.log = LogFactory.getLog (AbstractManager.class);
	}

	final void setFactory (AbstractManagerFactory<T, ?, ?, ?> factory)
	{
		this.factory = factory;
	}

	protected final DataStoreQuery<T> fetchQuery ()
	{
		return this.factory.createQuery (this.model);
	}

	protected final ElementBuilder<T> fetchBuilder ()
	{
		return null;
	}

	/**
	 * Get the <code>DomainModel</code> which is associated with this
	 * <code>AbstractManager</code> instance.
	 *
	 * @return A reference to the associated <code>DomainModel</code>
	 */

	public DomainModel getDomainModel ()
	{
		return this.model;
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	public T fetchById (Long id)
	{
		return (this.fetchQuery ()).query (id);
	}

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	public List<T> fetchAll ()
	{
		return (this.fetchQuery ()).queryAll ();
	}

	/**
	 * Insert an entity into the domain model and the underlying data store. This
	 * method is a convenience method which performs a non-recursive insert of the
	 * given entity into the domain model and underlying data store.
	 *
	 * @param  entity The entity to insert into the domain model, not null
	 * @return        A reference to the inserted entity
	 * @see    #insert(Element, Boolean) insert(T, Boolean)
	 */

	public final T insert (T entity)
	{
		return this.insert (entity, new Boolean (false));
	}

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to insert into the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   inserted, <code>false</code> otherwise, not null
	 * @return           A reference to the inserted entity
	 */

	public T insert (T entity, Boolean recursive)
	{
		return null;
	}

	/**
	 * Remove an entity from the domain model and the underlying data store. This
	 * is a convenience method that performs a non-recursive removal of the
	 * given entity from the domain model and underlying data store.
	 *
	 * @param  entity The entity to remove from the domain model, not null
	 * @see    #remove(Element, Boolean) remove(T, Boolean)
	 */

	public final void remove (T entity)
	{
		this.remove (entity, new Boolean (false));
	}

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to remove from the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   removed, <code>false</code> otherwise, not null
	 */

	public void remove (T entity, Boolean recursive)
	{
	}
}
