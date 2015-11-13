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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.auto.factory.AutoFactory;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.DomainModelFactory;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Implementation of the <code>DataStore</code> using the Java Persistence API.
 * This class implements the <code>DataStore</code> using a relational database
 * though JPA.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     JPATransaction
 */

@AutoFactory (implementing = { DataStore.DataStoreFactory.class })
public final class JPADataStore implements DataStore
{
	/**
	 * Dagger component used to create the <code>DomainModel</code> and
	 * <code>JPADataStore</code> instances.
	 */

	@Component (modules = { JPADataStoreModule.class })
	@Singleton
	public static interface JPADataStoreComponent extends DataStore.DataStoreComponent
	{
		/**
		 * Get a reference to the <code>DomainModelFactory</code>.
		 *
		 * @return The <code>DomainModelFactory</code>
		 */

		@Override
		public abstract DomainModelFactory getDomainModelFactory ();
	}

	/**
	 * Dagger module to get <code>DataStoreFactory</code> instances.  This
	 * module exists to translate the <code>JPADataStoreFactory</code> instance
	 * created by <code>AutoFactory</code> to a
	 * <code>DataStore.DataStoreFactory</code> instance usable by the
	 * <code>DomainModel</code>.  This is usually automatic but Dagger needs it
	 * to be done explicitly.
	 */

	@Module
	static final class JPADataStoreModule
	{
		/**
		 * Get a reference to the <code>DomainStoreFactory</code>.
		 *
		 * @return The <code>DataStoreFactory</code>
		 */

		@Provides
		@Singleton
		DataStore.DataStoreFactory getFactory (final JPADataStoreFactory factory)
		{
			return factory;
		}
	}

	/** The component for creating instances of the <code>DataStore</code>*/
	private static final DataStore.DataStoreComponent COMPONENT;

	/** The logger */
	private final Logger log;

	/** The JPA Entity manager factory for this database */
	private final EntityManagerFactory emf;

	/** The JPA entity manager for access to the database. */
	private final EntityManager em;

	/** The <code>Transaction</code> instance */
	private final Transaction transaction;

	/**
	 * Static initializer to create a constance instance of the Dagger
	 * component.
	 */

	static
	{
		COMPONENT = DaggerJPADataStore_JPADataStoreComponent.create ();
	}

	/**
	 * Create a new <code>JPADataStore</code> instance and return it
	 * encapsulated in a new <code>DomainModel</code> instance.
	 *
	 * @param  profile The <code>Profile</code>, not null
	 *
	 * @return         The <code>DomainModel</code>
	 */

	public static DomainModel create (final Profile profile)
	{
		Preconditions.checkNotNull (profile, "profile");

		return JPADataStore.COMPONENT.getDomainModelFactory ()
			.create (profile);
	}

	/**
	 * Get the instance of the <code>DataStoreComponent</code> which is used to
	 * create <code>JPADataStore</code> instances.
	 *
	 * @return The <code>DataStoreComponent</code>
	 */

	public static DataStore.DataStoreComponent getComponent ()
	{
		return JPADataStore.COMPONENT;
	}

	/**
	 * Create the <code>JPADataStore</code>.
	 *
	 * @param  unitName   The JPA unit name, not null
	 * @param  parameters parameters for the JPA <code>EntityManager</code>,
	 *                    not null
	 */

	protected JPADataStore (final Profile profile)
	{

		this.log = LoggerFactory.getLogger (this.getClass ());

		try
		{
			this.log.debug ("Creating the JPA EntityManagerFactory");
			this.emf = Persistence.createEntityManagerFactory (profile.getName ());

			this.log.debug ("Creating the JPA EntityManager");
			this.em = this.emf.createEntityManager ();

			this.transaction = new JPATransaction (this.em.getTransaction ());
		}
		catch (Exception ex)
		{
			this.log.debug ("Close EntityManagerFactory due to initialization failure");
			this.close ();

			throw ex;
		}
	}

	/**
	 * Retrieve a <code>List</code> of <code>Element</code> instances which
	 * match the specified <code>Filter</code>.
	 *
	 * @param  <T>    The <code>Element</code> interface type
	 * @param  filter The <code>Filter</code>, not null
	 *
	 * @return        A <code>List</code> of <code>Element</code> instances
	 */

	@Override
	public <T extends Element> List<T> fetch (final Class<? extends T> type, final Filter<T> filter)
	{
		this.log.trace ("fetch: type={}, filter={}", type, filter);

		assert type != null : "type is NULL";
		assert filter != null : "filter is NULL";
		assert this.isOpen () : "datastore is closed";

		List<T> result = new ArrayList<T> ();

		if (Element.SELECTOR_ID.equals (filter.getSelector ()))
		{
			this.log.debug ("Fetching by ID for: class={}, id={}", type.getSimpleName (), filter.getValue (Element.ID));

			T data = this.em.find (type, filter.getValue (Element.ID));

			if (data != null)
			{
				this.log.debug ("Loaded Element: {}", data);

				result.add (data);
			}
			else
			{
				this.log.debug ("No Element of type {} found with ID: {}", type.getSimpleName (), filter.getValue (Element.ID));
			}
		}
		else
		{
			String qname = String.format ("%s:%s", filter.getMetaData ()
					.getElementType ().getSimpleName (),
					filter.getSelector ().getName ());

			TypedQuery<? extends T> query = this.em.createNamedQuery (qname, type);

			filter.getSelector ()
				.getProperties ()
				.stream ()
				.forEach ((x) -> query.setParameter (x.getName (), filter.getValue (x)));

			result.addAll (query.getResultList ());
		}

		return result;
	}

	/**
	 * Get a <code>List</code> containing all of the ID numbers in the
	 * <code>DataStore</code> for instances of the specified
	 * <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         A <code>List</code> of ID numbers, may be empty
	 */

	@Override
	public List<Long> getAllIds (final Class<? extends Element> element)
	{
		assert element != null : "element is NULL";

		String name = String.format ("%s:allid", element.getSimpleName ());

		TypedQuery<Long> query = this.em.createNamedQuery (name, Long.class);

		return query.getResultList ();
	}

	/**
	 * Get an instance of the transaction manager for the
	 * <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager
	 */

	@Override
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

	@Override
	public boolean isOpen ()
	{
		return this.em.isOpen ();
	}

	/**
	 * Close the JPA data store, including all connections to the underlying
	 * database.  The behaviour of this data store, and its associated queries
	 * once it has been closed is undefined.
	 */

	@Override
	public void close ()
	{
		this.log.trace ("close:");

		if ((this.em != null) && (this.em.isOpen ()))
		{
			this.log.debug ("Closing the EntityManager");
			this.em.close ();
		}

		if ((this.emf != null) && (this.emf.isOpen ()))
		{
			this.log.debug ("Closing the EntityManagerFactory");
			this.emf.close ();
		}
	}

	/**
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	@Override
	public  <T extends Element> boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.em.contains (element);
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to insert, not null
	 *
	 * @return          A reference to the <code>Element</code>
	 */

	@Override
	public <T extends Element> T insert (final T element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("Persisting the Element");
		this.em.persist (element);

		@SuppressWarnings ("unchecked") // but there should be a better way...
		T result = this.em.find (((Class<? extends T>) element.getClass ()), element.getId ());

		return result;
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element  The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element> void remove (final T element)
	{
		this.log.trace ("remove: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("Removing the element from the database");
		this.em.remove (element);
	}
}
