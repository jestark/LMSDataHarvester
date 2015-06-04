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

import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

/**
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class JPATransaction implements Transaction
{
	/** The log */
	private final Logger log;

	/** The data store */
	private final EntityTransaction transaction;

	/**
	 * Create the <code>JPADataStoreTransaction</code>.
	 *
	 * @param datastore
	 */

	protected JPATransaction (final EntityTransaction transaction)
	{
		this.log = LoggerFactory.getLogger (JPATransaction.class);
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
	}

	/**
	 * Rollback the transaction.
	 */

	@Override
	public void rollback ()
	{
		this.log.trace ("rollback:");

		this.transaction.rollback ();
	}
}
