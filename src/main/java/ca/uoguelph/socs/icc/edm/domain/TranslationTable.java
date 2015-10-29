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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Map;
import java.util.Set;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Translate <code>Element</code> instances between <code>DomainModel</code> /
 * <code>DataStore</code> instances.  This class maintains a set of symmetric,
 * transitive, relationships between <code>Element</code> instances across
 * multiple <code>DataStore</code> instances.  It is primarily intended to be
 * used to provide a mapping for translation <code>Element</code> references
 * between <code>DataStore</code> instances when an <code>Element</code>
 * instance can not be uniquely identified based on its required fields.
 * <p>
 * The <code>TranslationTable</code> is implemented as a Map of maps.  However,
 * due to the transitive nature of the storage of the relationships, each set
 * of equivalent <code>Element</code> instances will share a nested map.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class TranslationTable
{
	/** The log */
	private final Logger log;

	/** The table */
	private final Map<Element, Map<DataStore, Element>> table;

	/**
	 * Create the <code>TranslationTable</code>.
	 */

	protected TranslationTable ()
	{
		this.log = LoggerFactory.getLogger (TranslationTable.class);

		this.table = new IdentityHashMap<> ();
	}

	/**
	 * Determine if the specified <code>Element</code> instance is in the has
	 * been entered into the <code>TranslationTable</code>, for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  element   The <code>Element</code> instance
	 * @param  datastore The <code>DataStore</code>
	 *
	 * @return           <code>true</code> if the <code>Element</code> instance
	 *                   is in the <code>TranslationTable</code>,
	 *                   <code>false</code> otherwise
	 */

	public boolean contains (final Element element, final DataStore datastore)
	{
		this.log.trace ("contains: element={}, datastore={}", element, datastore);

		boolean result = false;

		if (this.table.containsKey (element))
		{
			result = this.table.get (element).containsKey (datastore);
		}

		return result;
	}

	/**
	 * Get a <code>Set</code> containing all of the <code>Element</code>
	 * instances in the <code>TranslationTable</code>.
	 *
	 * @return A <code>Set</code> containing all of the <code>Element</code>
	 *         instances in the <code>TranslationTable</code>
	 */

	public Set<Element> elements ()
	{
		return new HashSet<> (this.table.keySet ());
	}

	/**
	 * Get the <code>Element</code> instance in the specified
	 * <code>DataStore</code> which corresponds to the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element   The <code>Element</code> instance
	 * @param  datastore The <code>DataStore</code>
	 *
	 * @return           The corresponding <code>Element</code> instance, or
	 *                   <code>null</code> if it does not exist
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> T get (final T element, final DataStore datastore)
	{
		this.log.trace ("get: element={}, datastore={}", element, datastore);

		assert element != null : "element is NULL";
		assert datastore != null : "datastore is NULL";
		assert element.getDataStore () != datastore : "The specified element is in the specified datastore";

		return (this.table.containsKey (element)) ? (T) this.table.get (element).get (datastore) : null;
	}

	/**
	 * Create a translation mapping between the specified <code>Element</code>
	 * instances.
	 *
	 * @param  left  The first <code>Element</code> in the mapping, not null
	 * @param  right The second <code>Element</code> in the mapping, not null
	 */

	public <T extends Element> void put (final T left, final T right)
	{
		this.log.trace ("put: left={}, right={}", left, right);

		assert left != null : "left is NULL";
		assert right != null : "right is NULL";
		assert left.getDataStore () != right.getDataStore () : "The elements can't both be from the same datastore";

		if (this.table.containsKey (left) && this.table.containsKey (right))
		{
			if (this.table.get (left) != this.table.get (right))
			{
				if (Collections.disjoint (this.table.get (left).keySet (), this.table.get (right).keySet ()))
				{
					this.log.debug ("Merging disjoint Translation table entries for {} and {}", left, right);

					Map<DataStore, Element> e = this.table.get (left);
					e.putAll (this.table.get (right));

					e.forEach ((k, v) -> this.table.put (v, e));
				}
				else
				{
					this.log.error ("Attempting to replace an existing mapping");
					throw new IllegalArgumentException ("Can't replace a mapping");
				}
			}
		}
		else if (this.table.containsKey (left) && (! this.table.containsKey (right)))
		{
			this.log.debug ("Adding {} to the existing translation table mapping for {}", right, left);

			Map<DataStore, Element> e = this.table.get (left);
			e.put (right.getDataStore (), right);
			this.table.put (right, e);
		}
		else if ((! this.table.containsKey (left)) && this.table.containsKey (right))
		{
			this.log.debug ("Adding {} to the existing translation table mapping for {}", left, right);

			Map<DataStore, Element> e = this.table.get (right);
			e.put (left.getDataStore (), left);
			this.table.put (left, e);
		}
		else
		{
			this.log.debug ("Creating new translation table entry for {} <-> {}", left, right);

			Map<DataStore, Element> e = new HashMap<DataStore, Element> ();
			e.put (left.getDataStore (), left);
			e.put (right.getDataStore (), right);

			this.table.put (left, e);
			this.table.put (right, e);
		}
	}

	/**
	 * Remove the specified <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code> instance to remove
	 */

	public void remove (final Element element)
	{
		this.log.trace ("remove: element=", element);

		if (this.table.containsKey (element))
		{
			this.table.get (element).remove (element.getDataStore ());
			this.table.remove (element);
		}
	}

	/**
	 * Remove all of the <code>Element</code> instances which are associated
	 * with the specified <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>
	 */

	public void removeAll (final DataStore datastore)
	{
		this.log.trace ("removeAll: datastore={}", datastore);

		Iterator<Map.Entry<Element, Map<DataStore, Element>>> i = this.table.entrySet ().iterator ();

		while (i.hasNext ())
		{
			Map.Entry<Element,Map<DataStore,Element>> key = i.next ();

			if (key.getKey ().getDataStore () == datastore)
			{
				key.getValue ().remove (datastore);
				i.remove ();
			}
		}
	}
}
