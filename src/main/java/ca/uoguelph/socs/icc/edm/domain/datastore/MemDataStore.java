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
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.stream.Collectors;

import com.google.auto.value.AutoValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Memory based implementation of the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class MemDataStore implements DataStore
{
	/**
	 *
	 */

	@AutoValue
	protected static abstract class Key
	{
		/**
		 *
		 */

		public static Key create (final Selector selector, final List<Object> keys)
		{
			return new AutoValue_MemDataStore_Key (selector, keys);
		}

		/**
		 *
		 */

		public abstract Selector getSelector ();

		/**
		 *
		 */

		public abstract List<Object> getKeys ();
	}

	/** The logger */
	private final Logger log;

	/** The <code>Set</code> of stored <code>Element</code> instances */
	private final Set<Wrapper<? extends Element>> elements;

	/** Indexed <code>Element</code> instances */
	private final Map<Key, Element> index;

	/** The transaction manager for the <code>DataStore</code> */
	private Transaction transaction;

	/** Indication if the <code>DataStore</code> is open */
	private boolean open;

	/**
	 * Create the <code>MemDataStore</code>.
	 */

	protected MemDataStore ()
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.open = true;
		this.transaction = null;

		this.elements = new HashSet<> ();
		this.index = new HashMap<> ();
	}

	/**
	 * Build a key for the index <code>Map</code> from the supplied
	 * <code>Filter</code> instance.
	 *
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return The key for the index <code>Map</code>
	 */

	private <T extends Element> Key buildIndex (final Filter<T> filter)
	{
		assert filter != null : "filter is NULL";

		return Key.create (filter.getSelector (), filter.getSelector ()
			.getProperties ()
			.stream ()
			.map ((x) -> filter.getValue (x))
			.collect (Collectors.toList ()));
	}

	/**
	 * Build a key for the index <code>Map</code> from the supplied
	 * <code>Element</code> instance, using the specified <code>Selector</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 *
	 * @return          The key for the index <code>Map</code>
	 */

	private <T extends Element> Key buildIndex (final Selector selector, final T element)
	{
		this.log.trace ("buildIndex: selector={}, element={}", selector, element);

		assert selector != null : "selector is NULL";
		assert element != null : "element is NULL";

		return Key.create (selector, selector.getProperties ()
			.stream ()
			.flatMap ((x) -> element.stream (x))
			.collect (Collectors.toList ()));
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
			this.elements.clear ();
			this.index.clear ();
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
	public <T extends Element> boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.elements.contains (new Wrapper<T> (element));
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

		List<T> result = null;

		if (filter.getSelector ().isUnique () && filter.getSelector ().isConstant ())
		{
			result = new ArrayList<T> ();

			T element = (T) this.index.get (this.buildIndex (filter));

			if (element != null)
			{
				result.add (element);
			}
		}
		else
		{
			result = (List<T>) this.elements.parallelStream ()
				.map (Wrapper::unwrap)
				.filter (x -> type.isInstance (x) && filter.test ((T) x))
				.collect (Collectors.toList ());
		}

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
	public List<Long> getAllIds (final Class<? extends Element> element)
	{
		this.log.trace ("getAllIds: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.elements.parallelStream ()
			.map (Wrapper::unwrap)
			.filter (x -> element.isInstance (x))
			.map (Element::getId)
			.collect (Collectors.toList ());
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
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
		assert ! this.contains (element) : "element is already in the DataStore";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("Inserting the Element");
		this.elements.add (new Wrapper<T> (element));

		this.log.debug ("building indexes");
		element.selectors ().stream ()
			.filter (x -> x.isUnique () && x.isConstant ())
			.forEach (x -> this.index.put (this.buildIndex (x, element), element));

		return element;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.contains (element) : "element is not in the DataStore";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("removing element");
		this.elements.remove (new Wrapper<T> (element));

		this.log.debug ("removing indexes");
		element.selectors ().stream ()
			.filter (x -> x.isUnique () && x.isConstant ())
			.forEach ((x) -> this.index.remove (this.buildIndex (x, element)));
	}
}
