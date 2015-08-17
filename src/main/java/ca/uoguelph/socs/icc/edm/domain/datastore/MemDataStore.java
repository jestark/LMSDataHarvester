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
import java.util.Map;

import java.util.HashMap;

import java.util.stream.Collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;

/**
 * Memory based implementation of the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class MemDataStore extends DataStore
{
	/** The storage for each <code>Element</code> implementation class  */
	private final Map<Class<?>, ElementStore<?>> elements;

	/** The transaction manager for the <code>DataStore</code> */
	private Transaction transaction;

	/** Indication if the <code>DataStore</code> is open */
	private boolean open;

	/**
	 * Create the <code>MemDataStore</code>.
	 *
	 * @param  profile The <code>Profile</code> data, not null
	 */

	public MemDataStore (final Profile profile)
	{
		super (profile);

		this.open = true;
		this.elements = new HashMap<> ();
		this.transaction = null;
	}

	@SuppressWarnings ("unchecked")
	private <T extends Element> ElementStore<T> getElementStore (final Class<?> element)
	{
		assert element != null : "element is NULL";
		assert this.elements.containsKey (element) : "no elements of that type";

		return (ElementStore<T>) this.elements.get (element);
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

	@Override
	protected <T extends Element, U extends T> List<T> fetch (final Class<U> type, final Filter<T> filter)
	{
		this.log.trace ("fetch: type={}, filter={}", type, filter);

		assert type != null : "type is NULL";
		assert filter != null : "filter is NULL";

		List<T> result = null;

		if (this.elements.containsKey (type))
		{
			result = ((ElementStore<T>) this.elements.get (type)).fetch (filter);
		}
/*		else
		{
			result = this.elements.keySet ()
				.stream ()
				.filter (x -> type.isAssignableFrom (x))
				.flatmap (x -> ((ElementStore<T>) this.elements.get (x)).fetch (filter).stream ())
				.collect (Collector::toList);
		}
*/
		return result;
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
	protected List<Long> getAllIds (final Class<? extends Element> element)
	{
		this.log.trace ("getAllIds: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is clased";

		return null;
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
	 * Close the <code>DataStore</code>.  If there is an active transaction
	 * then the <code>DataStore</code> will be closed when the transaction
	 * completes.
	 */

	@Override
	public void close ()
	{
		this.log.trace ("close:");

		this.open = false;

		if (! this.transaction.isActive ())
		{
			this.elements.forEach ((k, v) -> v.clear ());
			this.elements.clear ();
		}
	}

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

	@Override
	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		boolean result = false;

		if (this.elements.containsKey (element.getClass ()))
		{
			result = this.getElementStore (element.getClass ())
				.contains (element);
		}

		return result;
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to insert, not null
	 */

	@Override
	public <T extends Element> void insert (final MetaData<T> metadata, final T element)
	{
		this.log.trace ("insert: metadata={}, element={}", metadata, element);

		assert metadata != null : "metadata is NULL";
		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

		if (! this.elements.containsKey (element.getClass ()))
		{
			this.elements.put (element.getClass (), new ElementStore<T> (metadata));
		}
		else
		{
			this.getElementStore (element.getClass ())
				.insert (element);
		}
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element> void remove (final MetaData<T> metadata, final T element)
	{
		this.log.trace ("remove: metadata={}, element={}", metadata, element);

		assert metadata != null : "metadata is NULL";
		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

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
