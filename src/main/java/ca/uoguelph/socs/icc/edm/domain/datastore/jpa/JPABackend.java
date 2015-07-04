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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Backend;
import ca.uoguelph.socs.icc.edm.domain.datastore.Filter;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     JPADataStoreQuery
 * @see     JPADataStoreTransaction
 */

public final class JPABackend implements Backend
{
	private final Logger log;

	/** The JPA Entity manager factory for this database */
	private final EntityManagerFactory emf;

	/** The JPA entity manager for access to the database. */
	private EntityManager em;

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

	public JPABackend (final Profile profile)
	{
		this.log = LoggerFactory.getLogger (JPABackend.class);

//		try
//		{
			this.log.debug ("Creating the JPA EntityManagerFactory");
			this.emf = null; //Persistence.createEntityManagerFactory (unitname);

			this.log.debug ("Creating the JPA EntityManager");
			this.em = null; //this.emf.createEntityManager (properties);
//		}
//		catch (RuntimeException ex)
//		{
//			this.log.error ("Failed to create database connection", ex);

//			if (this.em != null)
//			{
//				this.log.debug ("Close EntityManager due to initialization failure");
//				this.em.close ();
//			}

//			if (this.emf != null)
//			{
//				this.log.debug ("Close EntityManagerFactory due to initialization failure");
//				this.emf.close ();
//			}

//			throw ex;
//		}
	}

	/**
	 * Override the <code>finalize</code> method from <code>Object</code> to
	 * ensure that all of the connections to the database have been closed.
	 */

	@Override
	protected void finalize () throws Throwable
	{
		this.close ();
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
	 * Determine if the specifed <code>Element</code> instance exists in the
	 * <code>DataStore</code>.
	 *
	 * @param  entity  The <code>Element</code> instance to check, not null
	 *
	 * @return         <code>true</code> if the <code>Element</code> instance
	 *                 exists in the <code>DataStore</code>, <code>false</code>
	 *                 otherwise
	 */

	@Override
	public <T extends Element, U extends T> boolean contains (final U element)
	{
		return this.em.contains (element);
	}

	@Override
	public <T extends Element, U extends T> List<T> fetch (final Filter<T, U> filter)
	{
		return null;
	}

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to insert, not null
	 */

	@Override
	public <T extends Element, U extends T> void insert (final U element)
	{
		assert element != null : "element is NULL";

		this.em.persist (element);
	}

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to remove, not null
	 */

	@Override
	public <T extends Element, U extends T> void remove (final U element)
	{
		assert element != null : "element is NULL";

		this.em.remove (element);
	}
}
