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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

/**
 * Implementation of the <code>DataStoreProxy</code> using a
 * <code>Query</code> and the <code>TranslationTable</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> on which we are operating
 */

final class HybridProxy<T extends Element> extends DataStoreProxy<T>
{
	/** The <code>Query</code> used to fetch <code>Element</code> instances */
	private final Query<T> query;

	/** The <code>TranslationTable</code> */
	private final TranslationTable ttable;

	/**
	 * Create the <code>HybridProxy</code>.
	 *
	 * @param  metadata  The <code>MetaData</code>, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected HybridProxy (final Creator<T> creator, final Query<T> query, final DataStore datastore)
	{
		super (creator, datastore);

		this.query = query;

		this.ttable = DomainModel.getTranslationTable ();
	}

	/**
	 * Retrieve the <code>Element</code> instance from the
	 * <code>DataStore</code> corresponding specified <code>Element</code>
	 * instance.  This method is used to implement the transparent substitution
	 * of <code>Element</code> instances.  If the specified
	 * <code>Element</code> instance is already in the <code>DataStore</code>
	 * then that will be returned.  Otherwise This method will retrieve the
	 * matching <code>Element</code> instance from the <code>DataStore</code>
	 * or <code>null</code> if there is no matching <code>Element</code>
	 * instance in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance, not null
	 *
	 * @return         The <code>Element</code> instance in the
	 *                 <code>DataStore</code> or <code>null</code>
	 */

	@Override
	public T fetch (final T element)
	{
		this.log.trace ("fetch: element={}", element);

		assert element != null : "element is NULL";

		if (! this.datastore.isOpen ())
		{
			this.log.error ("datastore is closed");
			throw new IllegalStateException ("datastore is closed");
		}

		T result = null;

		if (this.datastore.contains (element))
		{
			result = element;
		}
		else if (this.ttable.contains (element, this.datastore))
		{
			result = this.ttable.get (element, this.datastore);
		}
		else
		{
			result = this.query.setAllValues (element).query ();
		}

		return result;
	}

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to store, not null
	 *
	 * @return         The <code>Element</code> stored in the
	 *                 <code>DataStore</code>
	 */

	@Override
	public T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";

		if (! this.isComplete (element))
		{
			this.log.error ("Required fields are missing from the Element (Wrong Builder)");
			throw new IllegalStateException ("Element is missing required fields");
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to build an Action without an active transaction");
			throw new IllegalStateException ("no active transaction");
		}

		T result = this.query.setAllValues (element).query ();

		if (result == null)
		{
			this.log.debug ("Unconditionally inserting the element");
			result = this.datastore.insert (this.creator, element);
		}

		return result;
	}

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.  The <code>QueryProxy</code> ignores the
	 * reference <code>Element</code> since there is not association to create.
	 * As such, this method is the same as <code>insert (T element)</code>.
	 *
	 * @param  oldElement The reference <code>Element</code>
	 * @param  newElement The <code>Element</code> to store, not null
	 *
	 * @return            The <code>Element</code> stored in the
	 *                    <code>DataStore</code>
	 */

	@Override
	public T insert (final T oldElement, final T newElement)
	{
		this.log.trace ("insert: oldElement={}, newElement={}", oldElement, newElement);

		assert newElement != null : "element is NULL";

		if (! this.isComplete (newElement))
		{
			this.log.error ("Required fields are missing from the Element (Wrong Builder)");
			throw new IllegalStateException ("Element is missing required fields");
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to build an Element without an active transaction");
			throw new IllegalStateException ("no active transaction");
		}

		T result = null;

		if (newElement.equals (oldElement))
		{
			result = this.ttable.get (oldElement, this.datastore);

			if (result == null)
			{
				this.log.debug ("inserting the element and updating the translation table");
				result = this.insert (newElement);
				this.ttable.put (oldElement, result);
			}
			else
			{
				this.log.debug ("Using pre-existing element instance from the translation table");
			}
		}
		else
		{
			result = this.insert (newElement);
		}

		return result;
	}
}
