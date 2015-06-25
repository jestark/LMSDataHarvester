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

import ca.uoguelph.socs.icc.edm.domain.element.metadata.MetaData;

/**
 * Representation of a <code>DataStore</code>.  This is an internal interface
 * used by The <code>DomainModel</code> and <code>ElementBuilder</code>
 * implementations to manipulate the stored data.
 *
 * @author  James E. Stark
 * @version 2.0
 * @see     Transaction
 */

public abstract class DataStore
{
	private static interface DataStoreData<T extends Element>
	{
	}

	private static final class DataStoreDataImpl<T extends Element, U extends T> implements DataStoreData<T>
	{
		private final MetaData<T, U> metadata;

		public DataStoreDataImpl (final MetaData<T, U> metadata)
		{
			this.metadata = metadata;
		}
	}

	private static final Map<Class<? extends Element>, DataStoreData<?>> metadata;

	/** The logger */
	protected final Logger log;

	/** The profile */
	protected final DataStoreProfile profile;

	static
	{
		metadata = new HashMap<Class<? extends Element>, DataStoreData<?>> ();
	}

	public static final <T extends Element, U extends T> void registerElement (final MetaData<T, U> metadata)
	{
		assert metadata != null : "metadata is NULL";

		DataStore.metadata.put (metadata.getElementClass (), new DataStoreDataImpl<T, U> (metadata));
	}

	protected DataStore (final DataStoreProfile profile)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());
		this.profile = profile;
	}

	/**
	 * Determine if the <code>DataStore</code> is mutable.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is mutable,
	 *         <code>false</code> otherwise
	 */

	public final boolean isMutable ()
	{
		return true;
	}

	/**
	 * Get the default implementation class used by the <code>DataStore</code>
	 * for the specified <code>Element</code> interface class.
	 *
	 * @param  element The <code>Element</code> interface class
	 *
	 * @return         The <code>Element</code> implementation class
	 */

	public final Class<? extends Element> getElementClass (final Class<? extends Element> element)
	{
		this.log.trace ("getElementClass: element={}", element);

		assert element != null : "element is NULL";

		return this.profile.getImplClass (element);
	}

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public abstract boolean isOpen ();

	/**
	 * Close the <code>DataStore</code>.
	 */

	public abstract void close ();

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract Transaction getTransaction ();

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

	public abstract <T extends Element> boolean contains (final T element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	public abstract <T extends Element> void insert (final T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element> void remove (final T element);
}
