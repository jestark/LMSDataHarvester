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
 *
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     DataStoreQuery
 * @see     DataStoreTransaction
 */

public interface DataStore
{
	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public abstract Boolean isOpen ();

	/**
	 * Close the <code>DataStore</code>.
	 */

	public abstract void close ();

	/**
	 * Get the relevant <code>DataStoreQuery</code> for the provided interface
	 * and implementation classes.
	 *
	 * @param <T>  The interface type of the query object
	 * @param <X>  The implementation type of the query object
	 * @param type Interface type class, not null
	 * @param impl Implementation type class, not null
	 * @return     Query object for the specified interface and implementation
	 */

	public abstract <T extends Element, X extends T> DataStoreQuery<T> createQuery (Class<T> type, Class<X> impl);

	/**
	 * Get the profile data for the <code>DataStore</code>.
	 *
	 * @return A copy of the profile data
	 */

	public abstract DataStoreProfile getProfile ();

	/**
	 * Get the transaction manager for the <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract DataStoreTransaction getTransaction ();
}
