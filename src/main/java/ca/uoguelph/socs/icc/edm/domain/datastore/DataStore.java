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

import ca.uoguelph.socs.icc.edm.domain.DomainModelFactory;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Representation of a <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Transaction
 */

public interface DataStore
{
	/**
	 * Common interface to represent the Dagger component used to create
	 * <code>DomainModel</code>/<code>DataStore</code> instances.  This
	 * interface exists to give a common root to the Dagger components across
	 * all of the <code>DataStore</code> implementations.  It is not a Dagger
	 * component itself.
	 */

	public interface DataStoreComponent
	{
		/**
		 * Get a reference to the <code>DomainModelFactory</code>.
		 *
		 * @return The <code>DomainModelFactory</code>
		 */

		public abstract DomainModelFactory getDomainModelFactory ();
	}

	/**
	 * Common interface for the <code>AutoFactory</code> generated
	 * <code>DataStore</code> factories.  This interface exists to be injected
	 * into a new <code>DomainModel</code> instance, which can then use it to
	 * create a new <code>DataStore</code> instance.
	 */

	public interface DataStoreFactory
	{
		/**
		 * Get a new instance of the <code>DataStore</code> using the specified
		 * <code>Profile</code>.
		 *
		 * @param  profile The <code>Profile</code>, not null
		 *
		 * @return         The <code>DataStore</code>
		 */

		public abstract DataStore getDataStore (Profile profile);
	}

	/**
	 * Retrieve a <code>List</code> of <code>Element</code> instances which
	 * match the specified <code>Filter</code>.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return        A <code>List</code> of <code>Element</code> instances
	 */

	public abstract <T extends Element> List<T> fetch (Class<? extends T> type, Filter<T> filter);

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	public abstract List<Long> getAllIds (Class<? extends Element> element);

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract Transaction getTransaction ();

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public abstract boolean isOpen ();

	/**
	 * Close the <code>DataStore</code>.  If there is an active transaction
	 * then the <code>DataStore</code> will be closed when the transaction
	 * completes.
	 */

	public abstract void close ();

	/**
	 * Determine if the specified <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	public abstract <T extends Element> boolean contains (T element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to insert, not null
	 *
	 * @return          A reference to the <code>Element</code>
	 */

	public abstract <T extends Element> T insert (T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element> void remove (T element);
}
