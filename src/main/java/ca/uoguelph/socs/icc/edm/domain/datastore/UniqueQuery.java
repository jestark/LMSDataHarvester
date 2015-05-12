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

import java.util.Map;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Retrieve a single unique <code>Element</code> instance from the
 * <code>DataStore</code>.  This interface defines the <code>query</code>
 * method for retrieving <code>Element</code> instances from the
 * <code>DataStore</code> based on unique attributes.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> returned by the query
 */

public interface UniqueQuery<T extends Element>
{
	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches the specified query parameters.
	 * <p>
	 * The parameter <code>Map</code> must not be <code>null</code>, but may be
	 * empty.  All of the parameters to the query (if any) must have a value
	 * assigned in the parameter <code>Map</code>.  
	 * <p>
	 * The <code>getParameters</code> method will return a <code>Map</code>
	 * initialized with the names of the parameters.
	 *
	 * @param  parameters               A <code>Map</code> of parameter names
	 *                                  and their corresponding values for this
	 *                                  query.
	 *
	 * @return                          The <code>Element</code> instance which
	 *                                  matches the specified query, null if no
	 *                                  <code>Element</code> instances exist in
	 *                                  the <code>DataStore</code> that match
	 *                                  the <code>Query</code>
	 * @throws IllegalArgumentException if a parameter is missing
	 * @see    #getParameterMap
	 */

	public abstract T query (Map<String, Object> parameters);
}
