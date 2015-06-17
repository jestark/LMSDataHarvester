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

package ca.uoguelph.socs.icc.edm.domain.datastore.nul;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.AbstractDataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreProfile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

public class NullDataStore extends AbstractDataStore
{
	public NullDataStore (final DataStoreProfile profile)
	{
		super (profile);
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public boolean isOpen ()
	{
		return true;
	}

	public void close ()
	{
		this.log.trace ("close:");
	}

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

	public <T extends Element> Query<T> getQuery (final String name, final Class<?> element)
	{
		return null;
	}

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public Transaction getTransaction ()
	{
		return null;
	}

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

	@Override
	public <T extends Element> boolean contains (final T element)
	{
		return true;
	}

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

	@Override
	public <T extends Element> T fetch (final Class<T> type, final Long id)
	{
		return null;
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	@Override
	public <T extends Element> void insert (final T element)
	{
		this.log.trace ("insert: element={}", element);
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	@Override
	public  <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);
	}
}
