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

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;

/**
 *
 *
 * @author James E. Stark
 * @version 1.0
 */

public interface DataStore
{
	/**
	 *
	 * @return 
	 */

	public abstract Boolean isOpen ();

	/**
	 *
	 */

	public abstract void close ();

	/**
	 *
	 * @param <T>
	 * @param <X>
	 * @param type
	 * @param impl  
	 * @return
	 */

	public abstract <T extends DomainModelElement, X extends T> DataStoreQuery<T> getQuery (Class<T> type, Class<X> impl);

	/**
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract DataStoreTransaction getTransaction ();
}
