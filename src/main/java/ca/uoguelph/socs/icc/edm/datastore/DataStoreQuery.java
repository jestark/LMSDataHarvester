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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.List;
import java.util.Map;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;

/**
 *
 * @param <T>
 *
 * @author James E. Stark
 * @version 1.0
 */

public interface DataStoreQuery<T extends DomainModelElement>
{
	/**
	 *  Retrieve a single object from the datastore based on its identifier.
	 *
	 * @param  id 
	 * @return
	 */

	public abstract T query (Long id);
	
	/**
	 * Retrieve a single object from the datastore based on the value of the
	 * spcified uniqe query.
	 *
	 * @param  name
	 * @param  parameters
	 * @return
	 */

	public abstract T query (String name, Map<String, Object> parameters);

	/**
	 * Retrieve all objects of the specified type from the datastore.
	 *
	 * @return
	 */

	public abstract List<T> queryAll ();

	/**
	 * Retrieve all objects from the datastore for a given type which match the named query.
	 *
	 * @param  name The name of the query to execute, not null
	 * @param  parameters 
	 * @return
	 */

	public abstract List<T> queryAll (String name, Map<String, Object> parameters);

	/**
	 * 
	 *
	 * @param  entity  The entity to check, not null
	 * @return <code>true</code>
	 *         <code>false</code>
	 */

	public abstract Boolean contains (T entity);

	/**
	 * Insert the specified entity into the data store.
	 *
	 * @param entity The entity to insert, not null
	 */

	public abstract void insert (T entity);

	/**
	 * Remove the specified entity from the data store.
	 *
	 * @param entity The entity to remove, noit null
	 */

	public abstract void remove (T entity);
}
