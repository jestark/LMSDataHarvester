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

import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.Container;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * Representation of a <code>DataStore</code>.  This class provides the
 * front-end interfaces to the <code>DataStore</code>.  It uses a back-end to
 * actually store the <code>Element</code> instances.
 *
 * @author  James E. Stark
 * @version 2.0
 * @see     Transaction
 */

public final class DataStore
{
	/**
	 * Factory to produce <code>Query</code> instances.
	 *
	 * @param <T> The <code>Element</code> interface type of the <code>Query</code>
	 */

	private static final class QueryFactory<T extends Element> implements Container.Receiver<T, Query<T>>
	{
		/** The Backend to be used by the <code>Query</code> */
		private final Backend backend;

		/** The <code>Selector</code> which defines the <code>Query</code> */
		private final Selector selector;

		/**
		 * Create the <code>QueryFactory</code>, for the specified
		 * <code>Selector</code> on the specified <code>Backend</code>.
		 *
		 * @param  backend The <code>Backend</code>
		 * @param  selector The <code>Selector</code>
		 */

		public QueryFactory (final Backend backend, final Selector selector)
		{
			assert backend != null : "backend is NULL";
			assert selector != null : "selector is NULL";

			this.backend = backend;
			this.selector = selector;
		}

		/**
		 * Create the <code>Query</code> using the supplied
		 * <code>MetaData</code>.
		 *
		 * @param  metadata The <code>MetaData</code>, not null
		 *
		 * @return          The <code>Query</code>
		 */

		@Override
		public <U extends T> Query<T> apply (final MetaData<T, U> metadata)
		{
			assert metadata != null : "metadata is NULL";

			return new QueryImpl<T, U> (metadata, selector, backend);
		}
	}

	/** <code>MetaData</code> for the <code>Element</code> implementations */
	private static Container metadata;

	/** The logger */
	private final Logger log;

	/** The implementation specific <code>Backend</code> */
	private final Backend backend;

	/** The profile */
	private final Profile profile;

	/** The <code>Transaction</code> */
	private final Transaction transaction;

	/** Flag indicating if the <code>DataStore</code> is accepting operations */
	private boolean open;

	/**
	 * Create the <code>DataStore</code>.
	 *
	 * @param  profile The <code>Profile</code> data, not null
	 */

	protected DataStore (final Profile profile)
	{
		assert profile != null : "profile is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());
		this.profile = profile;

		this.open = true;
		this.backend = null;
		this.transaction = Transaction.getInstance (this);
	}

	/**
	 * Get the <code>Container</code> for the <code>MetaData</code>.
	 *
	 * @return The meta-data <code>Container</code>
	 */

	public Container getMetaDataContainer ()
	{
		return DataStore.metadata;
	}

	/**
	 * Get the <code>Query</code> for the specified <code>Selector</code> and
	 * <code>Element</code> implementation class.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 * @param  element  The <code>Element</code> implementation class, not null
	 *
	 * @return          The <code>Query</code>
	 */

	public <T extends Element> Query<T> getQuery (final Selector selector, final Class<? extends Element> element)
	{
		assert selector != null : "selector is NULL";
		assert element != null : "element is NULL";

		return DataStore.metadata.inject (element, new QueryFactory<T> (this.backend, selector));
	}

	/**
	 * Get a reference to the <code>Profile</code> data for the
	 * <code>DataStore</code>.
	 *
	 * @return The <code>Profile</code>
	 */

	public Profile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public Transaction getTransaction ()
	{
		return this.transaction;
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public boolean isOpen ()
	{
		return this.open || this.transaction.isActive ();
	}

	/**
	 * Close the <code>DataStore</code>.  If there is an active transaction
	 * then the <code>DataStore</code> will be closed when the transaction
	 * completes.
	 */

	public void close ()
	{
		this.open = false;

		if (! this.transaction.isActive ())
		{
			this.backend.close ();
		}
	}

	/**
	 * Determine if the specified <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	public <T extends Element, U extends T> boolean contains (final U element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.backend.contains (element);
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	public <T extends Element, U extends T> void insert (final U element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No active transactions";

		// Set Id

		this.backend.insert (element);

		// insert relationships
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	public <T extends Element, U extends T> void remove (final U element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No active transactions";

		// Remove relationships

		this.backend.remove (element);
	}
}
