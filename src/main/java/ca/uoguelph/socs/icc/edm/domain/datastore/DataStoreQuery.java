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
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;

/**
 *
 *
 * @author James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>DomainModelElement</code> to return from
 *              query methods.
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.DomainModelElement
 */

public interface DataStoreQuery<T extends DomainModelElement>
{
	/**
	 * Get the set of parameter names for the specified query.
	 *
	 * @param  name                 The name of the query, not null
	 * @return                      A set containing the names of all of the
	 *                              parameters
	 */

	public abstract Set<String> getParameters (String name);

	/**
	 * Retrieve an object from the <code>DataStore</code> based on the value of
	 * its primary key.  Note that the value of the primary key must not be less
	 * than zero.
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
	 * The parameter map may be null, however all of the parameters to the query
	 * (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param name                      The name of the query to execute, not null
	 * @param parameters                A Map of parameter names and their
	 *                                  corresponding values for this query.  The
	 *                                  Map may be null, the values must not be
	 *                                  null.
	 * @return                          The object which matches the specified
	 *                                  query, null if that object does not exist
	 *                                  in the <code>DataStore</code>.
	 * @throws IllegalArgumentException if a parameter is missing, or the query
	 *                                  does not exist
	 * @see    #getParameters
	 */

	public abstract T query (String name, Map<String, Object> parameters);

	/**
	 * Retrieve a List of all of the objects in the <code>DataStore</code> of the
	 * type which corresponds to this <code>DataStoreQuery</code> instance.  If
	 * there are no object of the corresponding type in the <code>DataStore</code>
	 * then the list will be empty.
	 *
	 * @return A (possibly empty) list of objects.

	 */

	public abstract List<T> queryAll ();

	/**
	 * Fetch a list of objects from the <code>DataStore</code> which match the
	 * specified query, with the specified parameters.
	 * <p>
	 * The parameter map may be null, however all of the parameters to the query
	 * (if any) must have a value.  All of the parameters included in the
	 * parameter map must have non-null values.
	 * <p>
	 * The <code>getParameters</code> method will return a list of all of the
	 * query parameters.
	 *
	 * @param  name                     The name of the query to execute, not null
	 * @param  parameters               A Map of parameter names and their
	 *                                  corresponding values for this query.  The
	 *                                  Map may be null, the values must not be
	 *                                  null.
	 * @return                          The list of object which match the
	 *                                  specified query.  If no objects match then
	 *                                  the List will be empty.
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
	 */

	public abstract void insert (T entity);

	/**
	 * Remove the specified entity from the <code>DataStore</code>.
	 *
	 * @param  entity The entity to remove, not null
	 */

	public abstract void remove (T entity);
}
