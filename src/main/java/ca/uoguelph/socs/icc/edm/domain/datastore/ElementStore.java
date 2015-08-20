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
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections4.keyvalue.MultiKey;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * In-Memory storage for <code>Element</code> instances for a given
 * implementation class.  This class stores and indexes one type of
 * <code>Element</code> instance.  It is intended to be used in the
 * <code>MemDataStore</code> to provide a complete <code>DataStore</code>
 * implementation.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type
 */

final class ElementStore<T extends Element>
{
	/** The logger */
	private final Logger log;

	/** The <code>IdGenerator</code> */
	private final IdGenerator generator;

	/** The <code>MetaData</code> for the <code>Element</code> being stored */
	private final MetaData<T> metadata;

	/** The set of <code>Selector</code> instances used to index the <code>Element</code> */
	private final Set<Selector> selectors;

	/** The <code>Set</code> of stored <code>Element</code> instances */
	private final Set<Wrapper<T>> elements;

	/** Indexed <code>Element</code> instances */
	private final Map<MultiKey<Object>, T> index;

	/**
	 * Create the <code>ElementStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code> for the <code>Element</code>
	 *                  which is being stored, not null
	 */

	public ElementStore (final MetaData<T> metadata, final DataStore datastore)
	{
		assert metadata != null : "metadata is NULL";
		assert datastore != null : "datastore is null";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.elements = new HashSet<Wrapper<T>> ();
		this.index = new HashMap<MultiKey<Object>, T> ();

		this.metadata = metadata;
		this.selectors = this.metadata.getSelectors ()
			.stream ()
			.filter ((x) -> x.isUnique ())
			.collect (Collectors.toSet ());

		this.generator = IdGenerator.getInstance (datastore, metadata.getElementClass ());
	}

	/**
	 * Build a key for the index <code>Map</code> from the supplied
	 * <code>Filter</code> instance.
	 *
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return The key for the index <code>Map</code>
	 */

	private MultiKey<Object> buildIndex (final Filter<T> filter)
	{
		assert filter != null : "filter is NULL";

		final List<Object> indexdata = filter.getSelector ()
			.getProperties ()
			.stream ()
			.map ((x) -> filter.getValue (x))
			.collect (Collectors.toList ());

		indexdata.add (filter.getSelector ());

		return new MultiKey<Object> (indexdata.toArray (), false);
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

	private MultiKey<Object> buildIndex (final Selector selector, final T element)
	{
		assert selector != null : "selector is NULL";
		assert element != null : "element is NULL";

		final List<Object> indexdata = selector.getProperties ()
			.stream ()
			.map ((x) -> this.metadata.getValue (x, element))
			.collect (Collectors.toList ());

		indexdata.add (selector);

		return new MultiKey<Object> (indexdata.toArray (), false);
	}

	/**
	 * Remove all of the stored <code>Element</code> instances.
	 */

	public void clear ()
	{
		this.log.trace ("close:");

		this.elements.clear ();
		this.index.clear ();
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

	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";

		return this.elements.contains (element);
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances which match the specified <code>Filter</code>.
	 *
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return        A <code>List</code> of <code>Element</code> instances,
	 *                may be empty
	 */

	public List<T> fetch (final Filter<T> filter)
	{
		this.log.trace ("fetchAll: filter={}", filter);

		assert filter != null : "filter is NULL";

		List<T> result = null;

		if (this.selectors.contains (filter.getSelector ()))
		{
			result = new ArrayList<T> ();
			result.add (this.index.get (this.buildIndex (filter)));
		}
		else
		{
			result = this.elements.parallelStream ()
				.map (Wrapper::unwrap)
				.filter ((x) -> filter.test (x))
				.collect (Collectors.toList ());
		}

		return result;
	}

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @return A <code>List</code> of ID numbers, may be empty
	 */

	public List<Long> getAllIds ()
	{
		return this.elements.parallelStream ()
			.map (Wrapper::unwrap)
			.map (Element::getId)
			.collect (Collectors.toList ());
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	public void insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";
		assert ! this.contains (element) : "element is already in the DataStore";

		this.generator.setId (this.metadata, element);

		this.elements.add (new Wrapper<T> (element));

		this.selectors.forEach ((x) -> this.index.put (this.buildIndex (x, element), element));
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	public void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.contains (element) : "element is not in the DataStore";

		this.elements.remove (new Wrapper<T> (element));

		this.selectors.forEach ((x) -> this.index.remove (this.buildIndex (x, element), element));
	}
}
