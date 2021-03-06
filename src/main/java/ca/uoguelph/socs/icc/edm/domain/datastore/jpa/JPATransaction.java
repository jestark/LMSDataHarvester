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

import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

/**
 * JPA implementation of the <code>Transaction</code> interface.  This class
 * wraps the JPA <code>EntityTransaction</code> and adapts it to the
 * <code>Transaction</code> interface.
 *
 * @author James E. Stark
 * @version 1.0
 */

final class JPATransaction implements Transaction
{
	/** The log */
	private final Logger log;

	/** The <code>DomainModel</code> upon which the <code>Transaction</code> operates */
	private final DomainModel model;

	/** The data store */
	private final EntityTransaction transaction;

	/**
	 * Create the <code>JPADataStoreTransaction</code>, encapsulating the
	 * specified <code>EntityTransaction</code>.
	 *
	 * @param  model       The <code>DomainModel</code>, not null
	 * @param  transaction The <code>EntityTransaction</code>, not null
	 */

	JPATransaction (final DomainModel model, final EntityTransaction transaction)
	{
		this.log = LoggerFactory.getLogger (JPATransaction.class);

		assert model != null : "model is NULL";
		assert transaction != null : "transaction is NULL";

		this.model = model;
		this.transaction = transaction;
	}

	/**
	 * Determine if there is a transaction in progress.
	 */

	@Override
	public boolean isActive ()
	{
		return this.transaction.isActive ();
	}

	/**
	 * Determine if the current transaction must be rolled back.
	 */

	@Override
	public boolean getRollbackOnly ()
	{
		return this.transaction.getRollbackOnly ();
	}

	/**
	 * Force the current transaction to be rolled back.
	 */

	@Override
	public void setRollbackOnly ()
	{
		this.log.trace ("setrollbackonly:");

		this.transaction.setRollbackOnly ();
	}

	/**
	 * Begin a new transaction.
	 */

	@Override
	public void begin ()
	{
		this.log.trace ("begin:");

		this.transaction.begin ();
	}

	/**
	 * Commit the transaction.
	 */

	@Override
	public void commit ()
	{
		this.log.trace ("commit:");

		this.transaction.commit ();

		// re-close the DomainModel to make sure that it is cleaned up.
		if (! this.model.isOpen ())
		{
			this.model.close ();
		}
	}

	/**
	 * Rollback the transaction.
	 */

	@Override
	public void rollback ()
	{
		this.log.trace ("rollback:");

		this.transaction.rollback ();

		// re-close the DomainModel to make sure that it is cleaned up.
		if (! this.model.isOpen ())
		{
			this.model.close ();
		}
	}
}
