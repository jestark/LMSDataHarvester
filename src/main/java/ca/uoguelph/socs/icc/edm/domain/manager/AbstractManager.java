/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.QueryFactory;

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
	/** The manager factory */
	private static final ElementManagerFactory FACTORY;

	/** The logger */
	private final Logger log;

	/** The type of <code>Element</code> which is being managed */
	private final Class<T> type;

	/** The <code>DomainModel</code> instance which owns this manager. */
	protected final DataStore datastore;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORY = new ElementManagerFactory ();
	}

	public static final <T extends ElementManager<U>, U extends Element> T getInstance (final Class<U> element, final Class<T> manager, final DataStore datastore)
	{
		return FACTORY.create (element, manager, datastore);
	}

	protected static final <T extends ElementManager<? extends Element>> void registerManager (final Class<T> manager, Class<? extends T> impl, ManagerFactory<T> factory)
	{
		FACTORY.registerFactory (manager, impl, factory);
	}

	/**
	 * Create the <code>AbstractManager</code>.
	 *
	 * @param  type      The type of <code>Element</code> which is being managed,
	 *                   not null
	 * @param  datastore The <code>DataStore</code> upon which this
	 *                   <code>ElementManager</code> will operate, not null
	 */

	public AbstractManager (final Class<T> type, final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (AbstractManager.class);
		
		this.type = type;
		this.datastore = datastore;
	}

	protected final DataStoreQuery<T> fetchQuery ()
	{
		this.log.trace ("Getting query object from factory");

		return null;
	}

	protected final DataStoreQuery<T> fetchQuery (final Class<? extends T> impl)
	{
		this.log.trace ("Get query object for class: {}", impl);

		return (QueryFactory.getInstance ()).create (this.type, impl, this.datastore);
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference to
	 * that <code>Element</code> instance exists in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code> instance
	 *                  contains a reference to the <code>Element</code>,
	 *                  <code>False</code> otherwise
	 */

	public final boolean contains (final T element)
	{
		this.log.trace ("Determine if the element is owned by me {}", element);

		return (this.fetchQuery ()).contains (element);
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	public T fetchById (final Long id)
	{
		this.log.trace ("Fetching entity from DataStore with ID: {}", id);

		return (this.fetchQuery ()).query (id);
	}

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	public List<T> fetchAll ()
	{
		this.log.trace ("Fetching all entities from DataStore");

		return (this.fetchQuery ()).queryAll ();
	}

	/**
	 * Insert an entity into the domain model and the underlying data store. This
	 * method is a convenience method which performs a non-recursive insert of the
	 * given entity into the domain model and underlying data store.
	 *
	 * @param  entity The entity to insert into the domain model, not null
	 * @return        A reference to the inserted entity
	 */

	public final T insert (final T element)
	{
		this.log.trace ("Inserting element into the DataStore: {}", element);

		if (element == null)
		{
			this.log.error ("Attempting to insert a NULL element into the datastore");
			throw new NullPointerException ();
		}

		if (! ((this.datastore.getProfile ()).isMutable ()))
		{
			this.log.error ("Attempting to insert an element into an immutable datastore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		return null;
	}

	/**
	 * Remove an entity from the domain model and the underlying data store. This
	 * is a convenience method that performs a non-recursive removal of the
	 * given entity from the domain model and underlying data store.
	 *
	 * @param  entity The entity to remove from the domain model, not null
	 */

	public final void remove (final T entity)
	{
	}
}
