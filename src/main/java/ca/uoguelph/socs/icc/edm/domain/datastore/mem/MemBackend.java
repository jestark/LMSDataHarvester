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

package ca.uoguelph.socs.icc.edm.domain.datastore.mem;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.Backend;
import ca.uoguelph.socs.icc.edm.domain.datastore.Filter;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;

/**
 *
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class MemBackend implements Backend
{
	/** */
	private final Logger log;

	/**  */
	private final Map<Class<?>, ElementStore<?, ?>> elements;

	/**
	 *
	 */

	public MemBackend (final Profile profile)
	{
		this.log = LoggerFactory.getLogger (MemBackend.class);

		this.elements = new HashMap<> ();
	}

	private <T extends Element, U extends T> ElementStore<T, U> createElementStore (final MetaData<T, U> metadata)
	{
		assert metadata != null : "metadata is NULL";

		ElementStore<T, U> result = new ElementStore<T, U> (metadata);
		this.elements.put (metadata.getElementClass (), result);

		return result;
	}

	@SuppressWarnings ("unchecked")
	private <T extends Element, U extends T> ElementStore<T, U> getElementStore (final Class<?> element)
	{
		assert element != null : "element is NULL";
		assert this.elements.containsKey (element) : "no elements of that type";

		return (ElementStore<T, U>) this.elements.get (element);
	}

	@Override
	public void close ()
	{
		this.log.trace ("close:");

//		this.elements.forEach ((k, v) -> v.close ());
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
		boolean result = false;

		if (this.elements.containsKey (element.getClass ()))
		{
			result = this.getElementStore (element.getClass ())
				.contains (element);
		}

		return result;
	}

	@Override
	public <T extends Element, U extends T> List<T> fetch (final Filter<T, U> filter)
	{
		assert filter != null : "filter is NULL";

		return null;
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

		this.getElementStore (element.getClass ())
			.insert (element);
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element, U extends T> void remove (final U element)
	{
		this.log.trace ("remove: element={}", element);

		if (this.elements.containsKey (element.getClass ()))
		{
			this.getElementStore (element.getClass ())
				.remove (element);
		}
		else
		{
			this.log.warn ("Datastore does not contain any elements of type: {}", (element.getClass ()).getSimpleName ());
		}
	}
}
