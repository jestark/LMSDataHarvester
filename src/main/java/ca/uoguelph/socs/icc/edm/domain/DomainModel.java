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
import java.util.function.Consumer;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.datastore.TranslationTable;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Access and manipulate <code>Element</code> instances contained within the
 * encapsulated <code>DataStore</code>.  This class provides a High-level
 * interface to the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@AutoFactory
public final class DomainModel
{
	/** The log */
	private final Logger log;

	/** The profile */
	private final Profile profile;

	/** The data store which contains all of the data */
	private final DataStore datastore;

	/** The <code>TranslationTable</code> */
	private final TranslationTable ttable;

	/**
	 * Create the <code>DomainModel</code>.
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the
	 *                   data represented by this <code>DomainModel</code>,
	 *                   not null
	 */

	public DomainModel (
			final Profile profile,
			final @Provided DataStore.DataStoreFactory factory,
			final @Provided TranslationTable ttable)
	{
		this.log = LoggerFactory.getLogger (DomainModel.class);

		this.profile = profile;
		this.ttable = ttable;
		this.datastore = factory.getDataStore (profile);
	}

	/**
	 * Insert a new <code>Element</code> instance into the
	 * <code>DataStore</code>.  This method inserts the <code>Element</code>
	 * instance specified by <code>netElement</code> into the
	 * <code>DataStore</code>, connects its relationships and updates the
	 * <code>TranslationTable</code> creating an association with the
	 * <code>Element</code> instance specified by <code>oldElement</code>
	 *
	 * @param  oldElement The <code>Element</code> to be associated with the
	 *                    inserted <code>Element</code>, may be null
	 * @param  newElement The <code>Element</code> to insert, not null
	 * @return            A reference to the <code>Element</code> instance in
	 *                    the <code>DataStore</code>
	 *
	 * @throws IllegalStateException If any of the relationships for the
	 *                               <code>Element</code> to be inserted fails
	 *                               to connect
	 */

	protected <T extends Element> T insert (final @Nullable T oldElement, final T newElement)
	{
		this.log.trace ("insert: oldElement={}, newElement={}", oldElement, newElement);

		assert newElement != null : "newElement is NULL";

		Preconditions.checkState (this.datastore.getTransaction ().isActive (), "transaction required");

		this.log.debug ("inserting element into the DataStore: {}", newElement);
		T result = this.datastore.insert (newElement);

		this.log.debug ("connecting relationships");
		if (! result.connect ())
		{
			this.log.error ("Failed to connect relationships");
			throw new IllegalStateException ("Failed to connect relationships");
		}

		if (result.equals (oldElement))
		{
			this.log.debug ("Inserting the Element mapping into the TranslationTable");
			this.ttable.put (oldElement, result);
		}

		return result;
	}

	/**
	 * Get a reference to the <code>Profile</code> data for the
	 * <code>DataStore</code>.
	 *
	 * @return The <code>Profile</code>
	 */

	protected Profile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.profile.isMutable ();
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
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the default implementation class defined in the <code>Profile</code>.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Selector<T> selector)
	{
		this.log.trace ("getQuery: selector={}", selector);

		Preconditions.checkNotNull (selector, "selector");

		Preconditions.checkArgument (this.profile.hasElementClass (selector.getElementClass ()),
				"no implementation for element: %s", selector.getElementClass ().getSimpleName ());

		return this.getQuery (selector, (Class<? extends T>) this.profile.getElementClass (selector.getElementClass ()));
	}

	/**
	 * Get a <code>Query</code> for the specified <code>Element</code> using
	 * the specified implementation class.
	 *
	 * @param  <T>      The type of the <code>Element</code> returned by the
	 *                  <code>Query</code>
	 * @param  selector The <code>Selector</code>, not null
	 * @param  impl     The <code>Element</code> implementation class, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Selector<T> selector, final Class<? extends T> impl)
	{
		this.log.trace ("getQuery: selector={}, impl={}", selector, impl);

		Preconditions.checkNotNull (selector, "selector");
		Preconditions.checkNotNull (impl, "impl");

		return this.datastore.createQuery (selector, impl, this, T::setDomainModel);
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

		InsertProcessor processor = null; // new InsertProcessor (this.datastore, DomainModel.ttable);

//		T result = processor.insert (element);
//		processor.processDeferred ();

		return null; // result;
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

		InsertProcessor processor = null; // new InsertProcessor (this.datastore, DomainModel.ttable);

//		Collection<T> results = processor.insert (elements);
//		processor.processDeferred ();

		return null; // results;
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

		Preconditions.checkNotNull (element, "element");
		Preconditions.checkArgument (this.datastore.contains (element), "element is no in the datastore");
		Preconditions.checkState (this.datastore.getTransaction ().isActive (), "transaction required");

		this.log.debug ("Disconnecting relationships");
		if (! element.disconnect ())
		{
			this.log.error ("Can not safely remove the element: {}", element);
			throw new IllegalStateException ("Can not break the relationships for the Element");
		}

		this.log.debug ("removing Element from the DataStore");
		this.datastore.remove (element);
	}
}
