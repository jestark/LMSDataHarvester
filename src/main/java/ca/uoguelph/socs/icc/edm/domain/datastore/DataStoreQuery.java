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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 *
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> to return from
 *              query methods.
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public interface DataStoreQuery<T extends Element>
{
	/**
	 * Get a reference to the <code>DataStore</code> upon which the
	 * <code>DataStoreQuery</code> operates.
	 *
	 * @return A reference to the <code>DataStore</code>
	 */

	public abstract DataStore getDataStore ();

	/**
	 * Get the next available DataStore ID number.  The number will be chosen
	 * by the IdGenerator algorithm set in the <code>DataStoreProfile</code>
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();

	/**
	 * Get the set of parameter names for the specified query.
	 *
	 * @param  name The name of the query, not null
	 * @return      A set containing the names of all of the parameters
	 */

	public abstract Set<String> getParameters (String name);

	/**
	 * Get all of the ID numbers for the class represented by this
	 * <code>DataStoreQuery</code> in the <code>DataStore</code>.
	 *
	 * @return A (possibly empty) list of <code>Long</code> integers
	 */

	public abstract List<Long> queryAllIds ();

	/**
	 * Get the largest ID number for the class represented by this
	 * <code>dataStoreQuery</code> in the <code>DataStore</code>.
	 *
	 * @return A <code>Long</code> integer
	 */

	public abstract Long queryMaxId ();

	/**
	 * Retrieve an object from the <code>DataStore</code> based on the value of
	 * its primary key.  Note that the value of the primary key must not be
	 * less than zero.
	 *
	 * @param  id The id (primary key) of the object to retrieve, not null
	 * @return    The object with an ID equal to the specified value or null if
	 *            that object does not exist in the <code>DataStore</code>.
	 */

	public abstract T query (Long id);

	/**
	 * Fetch the object from the <code>DataStore</code> which matches the
	 * specified query, with the specified parameters.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the
	 * query (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param name                      The name of the query to execute, not
	 *                                  null
	 * @param parameters                A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query. The <code>Map</code> may be
	 *                                  null, the values must not be null.
	 * @return                          The object which matches the specified
	 *                                  query, null if that object does not
	 *                                  exist in the <code>DataStore</code>.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @see    #getParameters
	 */

	public abstract T query (String name, Map<String, Object> parameters);

	/**
	 * Retrieve a List of all of the objects in the <code>DataStore</code> of
	 * the type which corresponds to this <code>DataStoreQuery</code> instance.
	 * If there are no object of the corresponding type in the
	 * <code>DataStore</code> then the list will be empty.
	 *
	 * @return A (possibly empty) <code>List</code> of objects.

	 */

	public abstract List<T> queryAll ();

	/**
	 * Fetch a list of objects from the <code>DataStore</code> which match the
	 * specified query, with the specified parameters.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the
	 * query (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to execute, not
	 *                                  null
	 * @param  parameters               A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query.  The <code>Map</code> may be
	 *                                  null, the values must not be null.
	 * @return                          The <code>List</code> of objects which
	 *                                  match the specified query.  If no
	 *                                  objects match then the
	 *                                  <code>List</code> will be empty.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @throws NullPointerException     if a value to a parameter is null
	 * @see    #getParameters

	 */

	public abstract List<T> queryAll (String name, Map<String, Object> parameters);

	/**
	 * Determine if an entity exists within the <code>DataStore</code>.
	 *
	 * @param  entity  The entity to check, not null
	 * @return <code>true</code> if a copy of the entity exists in the
	 *         <code>DataStore</code>, <code>false</code> otherwise
	 */

	public abstract Boolean contains (T entity);

	/**
	 * Insert the specified entity into the <code>DataStore</code>.
	 *
	 * @param  entity The entity to insert, not null
	 *
	 * @return        A reference to the entity in the <code>DataStore</code>
	 */

	public abstract T insert (T entity);

	/**
	 * Remove the specified entity from the <code>DataStore</code>.
	 *
	 * @param  entity The entity to remove, not null
	 */

	public abstract void remove (T entity);
}
