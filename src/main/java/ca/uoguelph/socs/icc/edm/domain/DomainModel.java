/* Copyright (C) 2014, 2015 James E. Stark
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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Access and manipulate <code>Element</code> instances contained within the
 * encapsulated <code>DataStore</code>.  This class provides a High-level
 * interface to the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public final class DomainModel
{
	/** The translation table for imports */
	private static final TranslationTable ttable;

	/** The log */
	private final Logger log;

	/** The data store which contains all of the data */
	private final DataStore datastore;

	/**
	 * Static initializer to create the <code>TranslationTable</code>
	 */

	static
	{
		ttable = new TranslationTable ();
	}

	/**
	 * Get a reference to the <code>TranslationTable</code>.
	 *
	 * @return The <code>TranslationTable</code>
	 */

	protected static TranslationTable getTranslationTable ()
	{
		return DomainModel.ttable;
	}

	/**
	 * Create the <code>DomainModel</code>.
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the
	 *                   data represented by this <code>DomainModel</code>,
	 *                   not null
	 */

	public DomainModel (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (DomainModel.class);

		this.datastore = datastore;
		this.datastore.setDomainModel (this);
	}

	/**
	 * Get a reference to the <code>DataStore</code>.
	 *
	 * @return The <code>DataStore</code>
	 */

	protected DataStore getDataStore ()
	{
		return this.datastore;
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.datastore.getProfile ().isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>True</code> if the underlying <code>DataStore</code> is
	 *         open, <code>False</code> otherwise
	 */

	public boolean isOpen ()
	{
		return this.datastore.isOpen ();
	}

	/**
	 * Close the <code>DataStore</code>.  Closing the <code>DataStore</code>
	 * causes the <code>DataStore</code> to flush is caches and release all of
	 * its resources.
	 * <p>
	 * If the <code>DataStore</code> has an active <code>Transaction</code>
	 * then the transaction will be allowed to complete before the
	 * <code>DataStore</code> is closed.  However, all of the entries to the
	 * current <code>DataStore</code> will be immediately removed from the
	 * <code>TranslationTable</code>.  So operations executed as part of the
	 * <code>Transaction</code> after the <code>DomainModel</code> has been
	 * closed, which require the <code>TranslationTable</code> will probably
	 * fail.
	 */

	public void close ()
	{
		this.datastore.close ();

		DomainModel.ttable.removeAll (this.datastore);
	}

	/**
	 * Get a reference to the <code>Transaction</code> instance for the
	 * <code>DataStore</code>.  A <code>Transaction</code> can only be returned
	 * for a mutable <code>DataStore</code>.
	 *
	 * @return                      A reference to the <code>Transaction</code>
	 * @throws IllegalStateExcption If the <code>DataStore</code> is immutable
	 */

	public Transaction getTransaction ()
	{
		if (! this.isMutable ())
		{
			this.log.error ("Attempting to get a Transaction for a immutable DataStore");
			throw new IllegalStateException ("DataStore is immutable");
		}

		return this.datastore.getTransaction ();
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the using the definition, but no implementation.  Queries based on the
	 * definition cover all of the implementations existing in the
	 * <code>DataStore</code>, at the cost of some performance.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  element  The <code>Element</code> interface class, not null
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getDefinitionQuery (final Class<T> element, final Selector selector)
	{
		return this.datastore.getQuery (this.datastore.getProfile ()
				.getMetaData (element),
				selector);
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the default implementation class defined in the <code>Profile</code>.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  element  The <code>Element</code> interface class, not null
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Class<T> element, final Selector selector)
	{
		return this.datastore.getQuery (element, selector);
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the specified implementation class.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  type     The <code>Element</code> interface class, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Class<T> type, final Class<? extends T> impl, final Selector selector)
	{
		return this.datastore.getQuery (type, impl, selector);
	}

	public InsertProcessor getProcessor ()
	{
		return new InsertProcessor (this.datastore, DomainModel.ttable);
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.  The exact behaviour of this method is
	 * determined by the implementation of the <code>DataStore</code>.
	 * <p>
	 * If the <code>Element</code> instance was created by the current instance
	 * of the <code>DataStore</code> then this method, should return
	 * <code>True</code>.  Otherwise, this method should return
	 * <code>False</code>, even if an identical <code>Element</code> instance
	 * exists in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

	public boolean contains (final Element element)
	{
		this.log.trace ("contains: element={}", element);

		if (element == null)
		{
			this.log.error ("Testing if the DataStore Contains a NULL Element");
			throw new NullPointerException ();
		}

		return this.datastore.contains (element);
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.  This method will insert a copy of the specified
	 * <code>Element</code> into the <code>DataStore</code> and return a
	 * reference to the <code>Element</code> instance which was inserted.
	 * <p>
	 * If the specified <code>Element</code> instance already exists in the
	 * <code>DataStore</code> then the returned instance will be a reference to
	 * the specified <code>Element</code> instance.
	 * <p>
	 * To insert an <code>Element</code> into the <code>DataStore</code>, an
	 * active <code>Transaction</code> is required.
	 *
	 * @param  element               The <code>Element</code> to insert, not
	 *                               null
	 *
	 * @return                       A reference to the <code>Element</code> in
	 *                               the <code>DataStore</code>
	 * @throws IllegalStateException If there is not an active
	 *                               <code>Transaction</code>
	 */

	public <T extends Element> T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to insert a NULL element");
			throw new NullPointerException ();
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to insert an Element without an Active Transaction");
			throw new IllegalStateException ("Active Transaction required");
		}

		InsertProcessor processor = new InsertProcessor (this.datastore, DomainModel.ttable);

		T result = processor.processElement (element);
		processor.processQueue ();

		return result;
	}

	/**
	 * Insert all of the <code>Element</code> instances in the specified
	 * <code>Collection</code> into the <code>DataStore</code>.  This method
	 * will insert a copy of the specified <code>Element</code> instances into
	 * the <code>DataStore</code> and return <code>Collection</code> of
	 * references to the <code>Element</code> instances which were inserted.
	 * <p>
	 * If any of the <code>Element</code> instances in the specified
	 * <code>Collection</code> already exists in the <code>DataStore</code>
	 * then the returned instances will be the same as the input instance.
	 * <p>
	 * To insert an <code>Element</code> into the <code>DataStore</code>, an
	 * active <code>Transaction</code> is required.
	 * <p>
	 * The <code>Collection</code> of <code>Element</code> instances returned
	 * by this method will have the same order as the input
	 * <code>Collection</code>.
	 *
	 * @param  element               The <code>Element</code> to insert, not
	 *                               null
	 *
	 * @return                       A reference to the <code>Element</code> in
	 *                               the <code>DataStore</code>
	 * @throws IllegalStateException If there is not an active
	 *                               <code>Transaction</code>
	 */

	public <T extends Element> Collection<T> insert (final Collection<T> elements)
	{
		this.log.trace ("insert: elements={}", elements);

		if (elements == null)
		{
			this.log.error ("Attempting to insert a NULL element");
			throw new NullPointerException ();
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to insert an Element without an Active Transaction");
			throw new IllegalStateException ("Active Transaction required");
		}

		InsertProcessor processor = new InsertProcessor (this.datastore, DomainModel.ttable);

		Collection<T> results = processor.processElements (elements);
		processor.processQueue ();

		return results;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.  To remove the specified <code>Element</code>,
	 * the <code>Element</code> must exist in the <code>DataStore</code> and
	 * the <code>DataStore</code> must have an active <code>Transaction</code>.
	 *
	 * @param  element                  The <code>Element</code> to remove, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the <code>Element</code> does not
	 *                                  exist in the <code>DataStore</code>
	 * @throws IllegalStateException    If there is not an active
	 *                                  <code>Transaction</code>
	 */

	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		if (element == null)
		{
			this.log.error ("Attempting to remove a NULL element");
			throw new NullPointerException ();
		}

		if (! this.datastore.getTransaction ().isActive ())
		{
			this.log.error ("Attempting to insert an Element without an Active Transaction");
			throw new IllegalStateException ("Active Transaction required");
		}

		if (! this.contains (element))
		{
			this.log.error ("Attempting to remove an Element which does not exist in the DataStore");
			throw new IllegalArgumentException ("Element does not exist in the DataStore");
		}

		@SuppressWarnings ("unchecked")
		MetaData<T> metadata = (MetaData<T>) element.metadata ();

		if (! metadata.canDisconnect (this.datastore, element))
		{
			this.log.error ("Can not safely remove the element: {}", element);
			throw new IllegalStateException ("Can not break the relationships for the Element");
		}

		this.datastore.remove (metadata, element);
		DomainModel.ttable.remove (element);
	}
}