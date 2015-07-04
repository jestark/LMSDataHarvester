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

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.Backend;
import ca.uoguelph.socs.icc.edm.domain.datastore.Filter;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

public final class NullBackend implements Backend
{
	private final Logger log;

	public NullBackend (final Profile profile)
	{
		this.log = LoggerFactory.getLogger (NullBackend.class);
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	@Override
	public void close ()
	{
		this.log.trace ("close:");
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
	public <T extends Element, U extends T> boolean contains (final U element)
	{
		return true;
	}

	@Override
	public <T extends Element, U extends T> List<T> fetch (final Filter<T, U> filter)
	{
		return new ArrayList<T> ();
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	@Override
	public <T extends Element, U extends T> void insert (final U element)
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
	public  <T extends Element, U extends T> void remove (final U element)
	{
		this.log.trace ("remove: element={}", element);
	}
}
