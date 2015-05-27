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
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Interface for retrieving <code>Element</code> instances from the
 * <code>DataStore</code>.  This interface defines the common methods required
 * for retrieving <code>Element</code> instances from the
 * <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @param   <T> The type of <code>Element</code> returned by the query
 * @see     DataStore
 * @see     ca.uoguelph.socs.icc.edm.domain.Element
 */

public interface Query<T extends Element>
{
	/**
	 * Get the interface <code>Class</code> of the <code>Element</code>
	 * returned by this <code>Query</code>.
	 *
	 * @return The <code>Class</code> object representing the type of
	 *         <code>Element</code> returned by this <code>Query</code>.
	 */

	public abstract Class<T> getInterfaceClass ();

	/**
	 * Get the iplementation <code>Class</code> of the <code>Element</code>
	 * returned by this <code>Query</code>.
	 *
	 * @return The <code>Class</code> object representing the implementation
	 *         type of <code>Element</code> returned by this
	 *         <code>Query</code>.
	 */

	public abstract Class<?> getImplementationClass ();

	/**
	 * Get the name of the <code>Query</code>.
	 *
	 * @return A <code>String</code> which identifies the <code>Query</code>.
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Set</code> of the parameter names for the
	 * <code>Query</code>.
	 *
	 * @return A <code>Set</code> containing the names of the parameters
	 */

	public abstract Set<String> getParameters ();

	/**
	 * Set a value for a specific parameter.
	 *
	 * @param  name                     The name of the parameter
	 * @param  value                    The value to be queried for the
	 *                                  parameter
	 *
	 * @return                          This <code>Query</code>
	 * @throws IllegalArgumentException if the specified parameter does not
	 *                                  exist
	 * @see    #getParameters
	 */

	public abstract Query<T> setParameter (String name, Object value);

	/**
	 * Fetch the <code>Element</code> instance from the <code>DataStore</code>
	 * which matches the parameters set on the <code>Query</code>.  Values must
	 * be specified for all of the parameters for the <code>Query</code>.
	 *
	 * @return The <code>Element</code> instance which matches the parameters
	 *         specified for the query, null if no <code>Element</code>
	 *         instances exist in the <code>DataStore</code> that match the
	 *         <code>Query</code>
	 */

	public abstract T query ();

	/**
	 * Fetch a <code>List</code> of <code>Element</code> instances from the
	 * <code>DataStore</code> which match the specified query parameters.
	 * Values must be specified for all of the parameters for the
	 * <code>Query</code>.
	 *
	 * @return The <code>List</code> of <code>Element</code> instances which
	 *         match the parameters specified for the query.  The
	 *         <code>List</code> will be empty if no <code>Element</code>
	 *         instances match the <code>Query</code>.
	 */

	public abstract List<T> queryAll ();
}
