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

package ca.uoguelph.socs.icc.edm.domain.datastore.jpa;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

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
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

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
	private Transaction transaction;

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
	 * @param  profile The <code>Profile</code>, not null
	 */

	protected JPADataStore (final Profile profile)
	{

		this.log = LoggerFactory.getLogger (this.getClass ());

		try
		{
			this.log.debug ("Creating the JPA EntityManagerFactory");
			this.emf = Persistence.createEntityManagerFactory (profile.getName (), profile.getParameters ());

			this.log.debug ("Creating the JPA EntityManager");
			this.em = this.emf.createEntityManager ();

			this.transaction = null;
		}
		catch (Exception ex)
		{
			this.log.debug ("Close EntityManagerFactory due to initialization failure");
			this.close ();

			throw ex;
		}
	}

	/**
	 * Create a new <code>Query</code> instance for the specified
	 * <code>Selector</code>.
	 *
	 * @param  selector  The <code>Selector</code>, not null
	 * @param  impl      The <code>Element</code> implementation class, not null
	 * @param  model     The <code>DomaonModel</code>, not null
	 * @param  reference
	 * @return           The <code>Query</code>
	 */

	@Override
	public <T extends Element> Query<T> createQuery (
			Selector<T> selector,
			Class<? extends T> impl,
			DomainModel model,
			BiConsumer<T, DomainModel> reference)
	{
		assert selector != null;
		assert impl != null;

		return (selector.getCardinality () == Selector.Cardinality.KEY)
			? new JPAIdQuery<T> (selector, impl, model, reference, this.em)
			: new JPANamedQuery<T> (selector, impl, model, reference, this.em);
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
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       An instance of the transaction manager
	 */

	@Override
	public Transaction getTransaction (final DomainModel model)
	{
		if (this.transaction == null)
		{
			this.transaction = new JPATransaction (model, this.em.getTransaction ());
		}

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
	public <T extends Element> boolean contains (final T element)
	{
		this.log.trace ("contains: element={}", element);

		assert element != null : "element is NULL";
		assert this.isOpen () : "datastore is closed";

		return this.em.contains (element);
	}

	/**
	 * Clear any caches present in the <code>DataStore</code>.
	 */

	@Override
	public void clear ()
	{
		this.em.clear ();
	}

	/**
	 * Remove the specified <code>Element</code> instance from any caches in the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code>, not null
	 */

	@Override
	public void evict (final Element element)
	{
		this.em.detach (element);
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  definition The <code>Definition</code> for the, not null
	 * @param  element    The <code>Element</code> instance to insert, not null
	 * @return            A reference to the <code>Element</code>
	 */

	@Override
	public <T extends Element> T insert (final Element.Definition<T> definition, final T element)
	{
		this.log.trace ("insert: element={}", element);

		assert element != null : "element is NULL";
		assert this.transaction.isActive () : "No Active transaction";

		this.log.debug ("Persisting the Element");
		this.em.persist (element);

		T result = this.em.find (definition.getElementClass (), element.getId ());

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
