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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Back-end interface for <code>DataStore</code> implementations.
 * Implementations of this interface are responsible for the final execution
 * of all <code>DataStore</code> operations.  The <code>DataStore</code> class
 * handles all of the common parts of the operations, and the
 * <code>Backend</code> implementations handle the implementation specific
 * components.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     DataStore
 */

public interface Backend
{
	/**
	 * Close the <code>DataStoreBackend</code>, flushing all cached data.
	 */

	public abstract void close ();

	/**
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	public abstract <T extends Element, U extends T> boolean contains (U element);

	/**
	 * Retrieve a <code>List</code> of <code>Element</code> instances which
	 * match the specified <code>Filter</code>.
	 *
	 * @param  <T>   The <code>Element</code> interface type
	 * @param  <U>   The <code>Element</code> implementation type
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return       A <code>List</code> of <code>Element</code> instances
	 */

	public abstract <T extends Element, U extends T> List<T> fetch (Filter<T, U> filter);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  <T>     The <code>Element</code> interface type
	 * @param  <U>     The <code>Element</code> implementation type
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	public abstract <T extends Element, U extends T> void insert (U element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  <T>     The <code>Element</code> interface type
	 * @param  <U>     The <code>Element</code> implementation type
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element, U extends T> void remove (U element);
}
