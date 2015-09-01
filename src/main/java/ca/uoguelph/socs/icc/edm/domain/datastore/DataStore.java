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
import java.util.List;

import java.util.HashMap;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;

/**
 * Representation of a <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 * @see     Transaction
 */

public abstract class DataStore
{
	/** <code>DataStore</code> factories */
	private static Map<Class<? extends DataStore>, Function<Profile, DataStore>> factories;

	/** The logger */
	protected final Logger log;

	/** The profile */
	protected final Profile profile;

	/** Reference to the enclosing <code>DomainModel</code> */
	private DomainModel model;

	/**
	 * static initializer to create the factory
	 */

	static
	{
		factories = new HashMap<> ();
	}

	/**
	 * Register a <code>DataStore</code> implementation with the factory.
	 *
	 * @param  impl    The <code>DataStore</code> implementation class, not null
	 * @param  factory Method reference to the constructor, not null
	 */

	protected static final void registerDataStore (final Class<? extends DataStore> impl, final Function<Profile, DataStore> factory)
	{
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";
		assert ! DataStore.factories.containsKey (impl) : "DataStore is already registered";

		DataStore.factories.put (impl, factory);
	}

	/**
	 * Get an instance of the <code>DataStore</code>.
	 *
	 * @param  impl    The <code>DataStore</code> implementation class, not null
	 * @param  profile The <code>Profile</code>, not null
	 *
	 * @return         The <code>DataStore</code>
	 */

	public static final DataStore getInstance (final Class<? extends DataStore> impl, final Profile profile)
	{
		assert impl != null : "impl is NULL";
		assert profile != null : "profile is NULL";
		assert DataStore.factories.containsKey (impl) : "DataStore is not registered";

		return DataStore.factories.get (impl).apply (profile);
	}

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
	}

	/**
	 * Get a reference to the <code>Profile</code> data for the
	 * <code>DataStore</code>.
	 *
	 * @return The <code>Profile</code>
	 */

	public final Profile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Get the enclosing <code>DomainModel</code> instance.
	 *
	 * @return The <code>DomainModel</code>
	 */

	public final DomainModel getDomainModel ()
	{
		return this.model;
	}

	/**
	 * Set the reference to the enclosing <code>DomainModel</code>.  This
	 * method is intended to be used by the enclosing <code>DomainModel</code>
	 * to give the <code>DataStore</code> a reference to itself.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	public final void setDomainModel (final DomainModel model)
	{
		assert model != null : "model is NULL";
		assert this.model == null || this.model == model : "can not change the DomainModel once is has been set";

		this.model = model;
	}

	/**
	 * Retrieve a <code>List</code> of <code>Element</code> instances which
	 * match the specified <code>Filter</code>.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  <U>    The <code>Element</code> implementation type
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return        A <code>List</code> of <code>Element</code> instances
	 */

	protected abstract <T extends Element, U extends T> List<T> fetch (Class<U> type, Filter<T> filter);

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	public abstract List<Long> getAllIds (Class<? extends Element> element);

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	public abstract Transaction getTransaction ();

	/**
	 * Determine if the <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 */

	public abstract boolean isOpen ();

	/**
	 * Close the <code>DataStore</code>.  If there is an active transaction
	 * then the <code>DataStore</code> will be closed when the transaction
	 * completes.
	 */

	public abstract void close ();

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

	public abstract boolean contains (Element element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to insert, not null
	 */

	public abstract <T extends Element> void insert (MetaData<T> metadata, T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	public abstract <T extends Element> void remove (MetaData<T> metadata, T element);
}
