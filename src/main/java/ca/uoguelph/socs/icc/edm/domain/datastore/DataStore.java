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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Representation of a <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @see     Transaction
 */

public abstract class DataStore
{
	/** The logger */
	protected final Logger log;

	/** The profile */
	protected final Profile profile;

	/**
	 * Create the <code>DataStore</code>.
	 *
	 * @param  profile The <code>Profile</code> data, not null
	 */

	public DataStore (final Profile profile)
	{
		assert profile != null : "profile is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());
		this.profile = profile;
	}

	/**
	 * Get a reference to the <code>Profile</code> data for the
	 * <code>DataStore</code>.
	 *
	 * @return The <code>Profile</code>
	 */

	public final Profile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Retrieve a <code>List</code> of <code>Element</code> instances which
	 * match the specified <code>Filter</code>.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  <U>    The <code>Element</code> implementation type
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return        A <code>List</code> of <code>Element</code> instances
	 */

	protected abstract <T extends Element, U extends T> List<T> fetch (Class<U> type, Filter<T> filter);

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	protected abstract List<Long> getAllIds (Class<? extends Element> element);

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

	public abstract boolean contains (Element element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to insert, not null
	 */

	public abstract <T extends Element> void insert (MetaData<T> metadata, T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element> void remove (MetaData<T> metadata, T element);
}
