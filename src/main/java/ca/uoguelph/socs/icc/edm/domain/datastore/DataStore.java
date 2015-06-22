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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a <code>DataStore</code>.  This is an internal interface
 * used by The <code>DomainModel</code> and <code>ElementBuilder</code>
 * implementations to manipulate the stored data.
 *
 * @author  James E. Stark
 * @version 2.0
 * @see     Transaction
 */

public interface DataStore
{
	/**
	 * Determine if the <code>DataStore</code> is mutable.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is mutable,
	 *         <code>false</code> otherwise
	 */

	public abstract boolean isMutable ();

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public abstract boolean isOpen ();

	/**
	 * Close the <code>DataStore</code>.
	 */

	public abstract void close ();

	/**
	 * Get the default implementation class used by the <code>DataStore</code>
	 * for the specified <code>Element</code> interface class.
	 *
	 * @param  element The <code>Element</code> interface class
	 *
	 * @return         The <code>Element</code> implementation class
	 */

	public abstract Class<? extends Element> getElementClass (Class<? extends Element> element);

	/**
	 * Get the <code>Query</code> with the specified name, returning
	 * <code>Element</code> instances of the specified type.
	 *
	 * @param  <T>     The <code>Element</code> interface type of the
	 *                 <code>Query</code>
	 * @param  name    The name of the <code>Query</code> to return
	 * @param  element The <code>Element</code> implementation class to be
	 *                 returned by the </code>Query</code>
	 *
	 * @return         The <code>Query</code>
	 */

	public abstract <T extends Element> Query<T> getQuery (String name, Class<?> element);

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract Transaction getTransaction ();

	/**
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  entity  The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	public abstract <T extends Element> boolean contains (T element);

	/**
	 * Fetch the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  <T>  The type of <code>Element</code> to be retrieved
	 * @param  type The implementation class of the <code>Element</code>
	 *              to retrieve
	 * @param  id   The <code>DataStore</code> Id of the <code>Element</code>
	 *              to retrieve
	 *
	 * @return      The requested <code>Element</code> instance
	 */

	public abstract <T extends Element> T fetch (Class<T> type, Long id);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	public abstract <T extends Element> void insert (T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element> void remove (T element);
}
