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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.PassThruIdGenerator;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Helper class containing the common error checks and <code>DataStore</code>
 * operations for the <code>Builder</code> implementations.  Specifically
 * instances of this class will be responsible for adding <code>Element</code>
 * instances to and retrieving <code>Element</code> instances from the
 * <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type to query
 */

abstract class DataStoreProxy<T extends Element>
{
	/** The Logger */
	protected final Logger log;

	/** The <code>DataStore</code> */
	protected final DataStore datastore;

	/** The meta-data <code>Creator</code> */
	protected final Creator<T> creator;

	/**
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>DataStoreProxy</code> instance
	 */

	protected static <T extends Element> DataStoreProxy<T> getInstance (final Class<T> type, final Selector selector, final DataStore datastore)
	{
		assert type != null : "type is NULL";
		assert selector != null : "selector is NULL";
		assert datastore != null : "datastore is NULL";

		if (! datastore.getProfile ().hasElementClass (type))
		{
			throw new IllegalStateException ("Element is not available for this datastore");
		}

		Creator<T> creator = datastore.getProfile ().getCreator (type);
		DataStoreProxy<T> result = null;

		if ((selector != Element.SELECTOR_ID)
				|| (datastore.getProfile ().getGenerator (creator.getElementType ()) == PassThruIdGenerator.class))
		{
			result = new QueryProxy<T> (creator, selector, datastore);
		}
		else
		{
			result = new TableProxy<T> (creator, datastore);
		}

		return result;
	}

	/**
	 *
	 * @param  type      The <code>Element</code> interface class, not null
	 * @param  impl      The <code>Element</code> implementation class, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>DataStoreProxy</code> instance
	 */

	protected static <T extends Element> DataStoreProxy<T> getInstance (final Class<T> type, Class<? extends T> impl, final Selector selector, final DataStore datastore)
	{
		assert type != null : "type is NULL";
		assert impl != null : "type is NULL";
		assert selector != null : "selector is NULL";
		assert datastore != null : "datastore is NULL";

		Creator<T> creator = datastore.getProfile ().getCreator (type, impl);
		DataStoreProxy<T> result = null;

		if ((selector != Element.SELECTOR_ID)
				|| (datastore.getProfile ().getGenerator (creator.getElementType ()) == PassThruIdGenerator.class))
		{
			result = new QueryProxy<T> (creator, selector, datastore);
		}
		else
		{
			result = new TableProxy<T> (creator, datastore);
		}

		return result;
	}

	/**
	 * Create the <code>DataStoreProxy</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected DataStoreProxy (final Creator<T> creator, final DataStore datastore)
	{
		assert creator != null : "creator is NULL";
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		if (! datastore.isOpen ())
		{
			this.log.error ("DataStore is Closed");
			throw new IllegalStateException ("Datastore is Closed");
		}

		if (! datastore.getProfile ().isMutable ())
		{
			this.log.error ("DataStore is immutable");
			throw new IllegalStateException ("Datastore is immutable");
		}

		this.creator = creator;
		this.datastore = datastore;
	}

	/**
	 * Determine if all of the required fields for the specified
	 * <code>Element</code> instance are filled in.
	 *
	 * @param  element The <code>Element</code> to check, not null
	 *
	 * @return         <code>true</code> if all of the required fields are not
	 *                 null, <code>false</code> otherwise
	 */

	protected final boolean isComplete (final T element)
	{
		this.log.trace ("isComplete: element={}", element);

		return this.creator.getProperties ()
			.stream ()
			.filter (Property::isRequired)
			.map (x -> this.creator.getValue (x, element))
			.allMatch (x -> x != null);
	}

	/**
	 * Determine if the <code>DataStore</code> contains the specified
	 * <code>Element</code> instance.
	 *
	 * @param  element The <code>Element</code> to test, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> is in the
	 *                 <code>DataStore</code>, <code>false</code> otherwise
	 */

	public final boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";

		return this.datastore.contains (element);
	}

	/**
	 * Create a new instance of the <code>Element</code>.
	 *
	 * @return  The new <code>Element</code> instance
	 * @see     ca.uoguelph.socs.icc.edm.domain.metadata.Creator#create
	 */

	public final T create ()
	{
		this.log.trace ("create:");

		T element = this.creator.create ();
		element.setDomainModel (this.datastore.getDomainModel ());

		return element;
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

	public abstract T fetch (final T element);

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to store, not null
	 *
	 * @return         The <code>Element</code> stored in the
	 *                 <code>DataStore</code>
	 */

	public abstract T insert (final T element);

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  oldElement The reference <code>Element</code>
	 * @param  newElement The <code>Element</code> to store, not null
	 *
	 * @return            The <code>Element</code> stored in the
	 *                    <code>DataStore</code>
	 */

	public abstract T insert (final T oldElement, final T newElement);
}

/**
 * Implementation of the <code>DataStoreProxy</code> using a
 * <code>Query</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> on which we are operating
 */

final class QueryProxy<T extends Element> extends DataStoreProxy<T>
{
	/** The <code>Query</code> used to fetch <code>Element</code> instances */
	private final Query<T> query;

	/**
	 * Create the <code>QueryProxy</code>.
	 *
	 * @param  metadata  The <code>MetaData</code>, not null
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected QueryProxy (final Creator<T> creator, final Selector selector, final DataStore datastore)
	{
		super (creator, datastore);

		this.query = datastore.getQuery (creator, selector);
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

		return (this.datastore.contains (element)) ? element
			: this.query.setAllValues (element).query ();
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
			this.datastore.insert (this.creator, element);
			result = element;
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

		return this.insert (newElement);
	}
}

/**
 * Implementation of the <code>DataStoreProxy</code> using a
 * <code>TranslationTable</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of the <code>Element</code> on which we are operating
 */

final class TableProxy<T extends Element> extends DataStoreProxy<T>
{
	/** The <code>TranslationTable</code> */
	private final TranslationTable ttable;

	/**
	 * Create the <code>TableProxy</code>.
	 *
	 * @param  creator   The <code>Creator</code>, not null
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected TableProxy (final Creator<T> creator, final DataStore datastore)
	{
		super (creator, datastore);

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

		return (this.datastore.contains (element)) ? element
			: this.ttable.get (element, this.datastore);
	}

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  oldElement The reference <code>Element</code>
	 * @param  newElement The <code>Element</code> to store, not null
	 *
	 * @return            The <code>Element</code> stored in the
	 *                    <code>DataStore</code>
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

		this.datastore.insert (this.creator, element);

		return element;
	}

	/**
	 * Insert the <code>Element</code> instance into the
	 * <code>DataStore</code>.
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

		if ((oldElement != null) && (oldElement.equals (newElement)))
		{
			result = this.ttable.get (oldElement, this.datastore);

			if (result == null)
			{
				this.datastore.insert (this.creator, newElement);
				this.ttable.put (oldElement, newElement);

				result = newElement;
			}
		}
		else
		{
			this.datastore.insert (this.creator, newElement);
			result = newElement;
		}

		return result;
	}
}
