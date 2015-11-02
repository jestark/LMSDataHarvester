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
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * A <code>DataStore</code> implementation that does nothing.  This
 * <code>DataStore</code> implementation is highly efficient in terms of memory
 * and CPU usage.  However, this efficiency comes at the expense of not
 * actually storing any data.  All of the data which is inserted into this
 * <code>DataStore</code> is immediately discarded.
 * <p>
 * This <code>DataStore</code> implementation is intended to be used for
 * testing, but it is also suitable to use it in other situations where a
 * <code>DataStore</code> is required, but there is no need for it to store any
 * actual data.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class NullDataStore implements DataStore
{
	/** The logger */
	private final Logger log;

	/** The transaction manager for the <code>NullDataDtore</code> */
	private Transaction transaction;

	/** Indication if the <code>DataStore</code> is open */
	private boolean open;

	/**
	 * Create the <code>NullDataStore</code>.
	 *
	 * @param  profile The <code>Profile</code> data, not null
	 */

	protected NullDataStore (final Profile profile)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.open = true;
		this.transaction = null;
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

	@Override
	public <T extends Element> List<T> fetch (final Class<? extends T> type, final Filter<T> filter)
	{
		this.log.trace ("fetch: type={}, filter={}", type, filter);

		assert type != null : "type is NULL";
		assert filter != null : "filter is NULL";
		assert this.isOpen () : "datastore is closed";

		return new ArrayList<T> ();
	}

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	@Override
	public List<Long> getAllIds (final Class<? extends Element> element)
	{
		this.log.trace ("getAllIds: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return new ArrayList<Long> ();
	}

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	@Override
	public Transaction getTransaction ()
	{
		if (this.transaction == null)
		{
			this.transaction = new BasicTransaction (this);
		}

		return this.transaction;
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	@Override
	public boolean isOpen ()
	{
		return this.open || this.transaction.isActive ();
	}

	/**
	 * Close the <code>NullDataStore</code>.
	 */

	@Override
	public void close ()
	{
		this.log.trace ("close:");

		this.open = false;
	}

	/**
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.  For the <code>NullDataStore</code> this will
	 * always return true (even though it would actually be false).
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	@Override
	public  <T extends Element> boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return true;
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.  For the <code>NullDataStore</code> this does
	 * nothing as there is no actual storage.
	 *
	 * @param  element  The <code>Element</code> instance to insert, not null
	 *
	 * @return          A reference to the <code>Element</code>
	 */

	@Override
	public <T extends Element> T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

		return element;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.  For the <code>NullDataStore</code> this does
	 * nothing as there is no actual storage.
	 *
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";
	}
}
