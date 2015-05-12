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

import java.util.Map;

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
	 * Get a <code>Map</code> initialized with the names of the parameters for
	 * the <code>Query</code>.  This method will create a <code>Map</code>
	 * suitable for use with the <code>query</code> method.  The keys of the
	 * <code>Map</code> will be initialized to the names of the
	 * <code>Query</code> parameters, and the values will be initialized to
	 * <code>null</code>.
	 *
	 * @return A <code>Map</code> with the keys initialized to the names of the
	 *         parameters for the <code>Query</code>.
	 */

	public abstract Map<String, Object> getParameterMap ();
}
