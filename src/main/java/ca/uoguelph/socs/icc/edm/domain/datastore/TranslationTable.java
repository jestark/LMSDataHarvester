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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Translate <code>Element</code> instances between <code>DomainModel</code>
 * instances.  This class maintains a set of symmetric, transitive,
 * relationships between <code>Element</code> instances across multiple
 * <code>DomainModel</code> instances.  It is primarily intended to be used to
 * provide a mapping for translation <code>Element</code> references between
 * <code>DomainModel</code> instances when an <code>Element</code> instance can
 * not be uniquely identified based on its required fields.
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
	/** Singleton Instance */
	private static final TranslationTable INSTANCE;

	/** The log */
	private final Logger log;

	/** The table */
	private final Map<Element, Map<DomainModel, Element>> table;

	/**
	 * Static initializer to create the Singleton instance.
	 */

	static
	{
		INSTANCE = new TranslationTable ();
	}

	/**
	 * Get an instance of the <code>TranslationTable</code>.
	 *
	 * @return The <code>TranslationTable</code>
	 */

	public static TranslationTable getInstance ()
	{
		return TranslationTable.INSTANCE;
	}

	/**
	 * Create the <code>TranslationTable</code>.
	 */

	private TranslationTable ()
	{
		this.log = LoggerFactory.getLogger (TranslationTable.class);

		this.table = new IdentityHashMap<> ();
	}

	/**
	 * Determine if the specified <code>Element</code> instance is in the has
	 * been entered into the <code>TranslationTable</code>, for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  element   The <code>Element</code> instance
	 * @param  model     The <code>DomainModel</code>
	 *
	 * @return           <code>true</code> if the <code>Element</code> instance
	 *                   is in the <code>TranslationTable</code>,
	 *                   <code>false</code> otherwise
	 */

	public boolean contains (final @Nullable Element element, final @Nullable DomainModel model)
	{
		this.log.trace ("contains: element={}, model={}", element, model);

		boolean result = false;

		if (this.table.containsKey (element))
		{
			result = this.table.get (element).containsKey (model);
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
	 * <code>DomainModel</code> which corresponds to the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element   The <code>Element</code> instance
	 * @param  model     The <code>DomainModel</code>
	 *
	 * @return           The corresponding <code>Element</code> instance, or
	 *                   <code>null</code> if it does not exist
	 */

	@SuppressWarnings ("unchecked")
	@CheckReturnValue
	public <T extends Element> T get (final T element, final DomainModel model)
	{
		this.log.trace ("get: element={}, model={}", element, model);

		assert element != null : "element is NULL";
		assert model != null : "model is NULL";
		assert element.getDomainModel () != model : "The specified element is in the specified model";

		return (this.table.containsKey (element)) ? (T) this.table.get (element).get (model) : null;
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
		assert left.getDomainModel () != right.getDomainModel () : "The elements can't both be from the same DomainModel";

		if (this.table.containsKey (left) && this.table.containsKey (right))
		{
			if (this.table.get (left) != this.table.get (right))
			{
				if (Collections.disjoint (this.table.get (left).keySet (), this.table.get (right).keySet ()))
				{
					this.log.debug ("Merging disjoint Translation table entries for {} and {}", left, right);

					Map<DomainModel, Element> e = this.table.get (left);
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

			Map<DomainModel, Element> e = this.table.get (left);
			e.put (right.getDomainModel (), right);
			this.table.put (right, e);
		}
		else if ((! this.table.containsKey (left)) && this.table.containsKey (right))
		{
			this.log.debug ("Adding {} to the existing translation table mapping for {}", left, right);

			Map<DomainModel, Element> e = this.table.get (right);
			e.put (left.getDomainModel (), left);
			this.table.put (left, e);
		}
		else
		{
			this.log.debug ("Creating new translation table entry for {} <-> {}", left, right);

			Map<DomainModel, Element> e = new HashMap<DomainModel, Element> ();
			e.put (left.getDomainModel (), left);
			e.put (right.getDomainModel (), right);

			this.table.put (left, e);
			this.table.put (right, e);
		}
	}

	/**
	 * Remove the specified <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code> instance to remove
	 */

	public void remove (final @Nullable Element element)
	{
		this.log.trace ("remove: element=", element);

		if (this.table.containsKey (element))
		{
			this.table.get (element).remove (element.getDomainModel ());
			this.table.remove (element);
		}
	}

	/**
	 * Remove all of the <code>Element</code> instances which are associated
	 * with the specified <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>
	 */

	public void removeAll (final @Nullable DomainModel model)
	{
		this.log.trace ("removeAll: model={}", model);

		Iterator<Map.Entry<Element, Map<DomainModel, Element>>> i = this.table.entrySet ().iterator ();

		while (i.hasNext ())
		{
			Map.Entry<Element, Map<DomainModel, Element>> entry = i.next ();

			if (entry.getKey ().getDomainModel () == model)
			{
				entry.getValue ().remove (model);
				i.remove ();
			}
		}
	}
}
