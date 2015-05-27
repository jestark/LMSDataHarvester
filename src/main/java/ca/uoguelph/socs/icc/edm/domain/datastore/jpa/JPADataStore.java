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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     JPADataStoreQuery
 * @see     JPADataStoreTransaction
 */

public final class JPADataStore extends AbstractDataStore
{
	/** The JPA Entity manager factory for this database */
	private final EntityManagerFactory emf;

	/** The JPA entity manager for access to the database. */
	private EntityManager em;

	/** The transaction object */
	private final JPADataStoreTransaction transaction;

	/**
	 * Create the JPA data store.  This method will setup a connection to the
	 * specified database.  If JPA fails to create the connection it will see
	 * that the relevant components are closed down before re-throwing the
	 * exception from JPA.
	 *
	 * @param  unitname   The JPA unit name for the database.
	 * @param  properties Map of properties to be passed to JPA to create the
	 *                    database connection.
	 */

	public JPADataStore (final DataStoreProfile profile, final String unitname, final Map<String, String> properties)
	{
		super (profile);

		try
		{
			this.log.debug ("Creating the JPA EntityManagerFactory");
			this.emf = Persistence.createEntityManagerFactory (unitname);

			this.log.debug ("Creating the JPA EntityManager");
			this.em = this.emf.createEntityManager (properties);

			this.transaction = new JPADataStoreTransaction (this);
		}
		catch (RuntimeException ex)
		{
			this.log.error ("Failed to create database connection", ex);

			if (this.em != null)
			{
				this.log.debug ("Close EntityManager due to initialization failure");
				this.em.close ();
			}

			if (this.emf != null)
			{
				this.log.debug ("Close EntityManagerFactory due to initialization failure");
				this.emf.close ();
			}

			throw ex;
		}
	}

	/**
	 * Override the <code>finalize</code> method from <code>Object</code> to
	 * ensure that all of the connections to the database have been closed.
	 */

	protected void finalize () throws Throwable
	{
		this.close ();
	}

	/**
	 * Get a reference to the JPA entity manager.  This method is intended to
	 * only be used by the <code>JPADataStoreQuery</code> and
	 * <code>JPADataStoreTransaction</code> classes to perform their functions.
	 *
	 * @return A reference to the JPA <code>EntityManager</code>
	 */

	protected EntityManager getEntityManager ()
	{
		return this.em;
	}

	/**
	 * Determine if the <code>DataStore</code> is open.  The
	 * <code>DataStore</code> is only usable when it is open.  If the
	 * <code>DataStore</code> is not open then the behaviour of its methods and
	 * its associated query and transaction object is undefined.
	 *
	 * @return <code>true</code> if the connection to the database is open,
	 *         <code>false</code> otherwise
	 */

	@Override
	public Boolean isOpen ()
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

		// clean up all of the queries.

		// Close the entity manager and factory.
		if ((this.emf != null) && (this.emf.isOpen ()))
		{
			if ((this.em != null) && (this.em.isOpen ()))
			{
				this.log.debug ("Closing the EntityManager");
				this.em.close ();
			}

			this.log.debug ("Closing the EntityManagerFactory");
			this.emf.close ();
		}
	}

	/**
	 * Get the relevant DataStoreQuery for the provided interface and
	 * implementation classes.
	 *
	 * @param  <T>                The interface type of the query object
	 * @param  <X>                The implementation type of the query object
	 * @param  type               Interface type class, not null
	 * @param  impl               Implementation type class, not null
	 * @return                    Query object for the specified interface and
	 *                            implementation
	 */

	@Override
	protected <T extends Element, U extends T> AbstractDataStoreQuery<T, U> createQuery (final Class<T> type, final Class<U> impl)
	{
		this.log.trace ("createQuery: type={} impl={}", type, impl);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		return new JPADataStoreQuery<T, U> (this, type, impl);
	}

	/**
	 * Get the transaction manager for the <code>DataStore</code>.
	 *
	 * @return An instance of the transaction manager.
	 */

	@Override
	public DataStoreTransaction getTransaction ()
	{
		return this.transaction;
	}
}