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

package ca.uoguelph.socs.icc.edm.domain.datastore.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;

/**
 * Basic <code>Transaction</code> implementation for the
 * <code>MemDataStore</code>.  This class implements the transaction interface
 * and its behaviour, but does not do anything.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class MemTransaction implements Transaction
{
	/** The log  */
	private final Logger log;

	/** The <code>DomainModel</code> upon which the <code>Transaction</code> operates */
	private final DomainModel model;

	/** Is the <code>Transaction</code> active? */
	private boolean active;

	/** Must the <code>Transaction</code> be rolled back? */
	private boolean rollback;

	/**
	 * Create the <code>MemTransaction</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 */

	public MemTransaction (final DomainModel model)
	{
		assert model != null : "model is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;

		this.active = false;
		this.rollback = false;
	}

	/**
	 * Determine if the current <code>Transaction</code> is active.
	 *
	 * @return <code>True</code> if the transaction is active,
	 *         <code>False</code> otherwise
	 */

	public boolean isActive ()
	{
		return this.active;
	}

	/**
	 * Determine if the current <code>Transaction</code> must be rolled back.
	 *
	 * @return                       <code>True</code> if the transaction must
	 *                               be rolled back, <code>False</code>
	 *                               otherwise
	 * @throws IllegalStateException if the <code>Transaction</code> is not
	 *                               active
	 */

	public boolean getRollbackOnly ()
	{
		if (! this.active)
		{
			this.log.error ("Can not determine is transaction must be rolled back:  Transaction not active");
			throw new IllegalStateException ("No active transaction");
		}

		return this.rollback;
	}

	/**
	 * Force the current <code>Transaction</code> to be rolled back.
	 *
	 * @throws IllegalStateException if the <code>Transaction</code> is not
	 *                               active
	 */

	public void setRollbackOnly ()
	{
		this.log.trace ("setRollbackOnly:");

		if (! this.active)
		{
			this.log.error ("Can not force rollback: Transaction not active");
			throw new IllegalStateException ("No active transaction");
		}

		this.rollback = true;
	}

	/**
	 * Begin a new <code>Transaction</code>.
	 *
	 * @throws IllegalStateException if the <code>Transaction</code> is already
	 *                               active
	 */

	public void begin ()
	{
		this.log.trace ("begin:");

		if (this.active)
		{
			this.log.error ("Can not begin:  Transaction already in progress");
			throw new IllegalStateException ("Transaction already in progress");
		}

		if (! this.model.isOpen ())
		{
			this.log.error ("Can not begin: Datastore is closed");
			throw new IllegalStateException ("Datastore is closed");
		}

		this.active = true;
	}

	/**
	 * Commit the current <code>Transaction</code>.
	 *
	 * @throws IllegalStateException if the <code>Transaction</code> is not
	 *                               active
	 * @throws IllegalStateException if the <code>Transaction must be rolled
	 *                               back
	 */

	public void commit ()
	{
		this.log.trace ("commit:");

		if (! this.active)
		{
			this.log.error ("Can not Commit:  Transaction not active");
			throw new IllegalStateException ("No active transaction");
		}

		if (this.rollback)
		{
			this.log.error ("Can not Commit:  Transaction requires rollback");
			throw new IllegalStateException ("Transaction must be rolled back");
		}

		this.active = false;

		// re-close the DomainModel to make sure that it is cleaned up.
		if (! this.model.isOpen ())
		{
			this.model.close ();
		}
	}

	/**
	 * Rollback the current <code>Transaction</code>.
	 *
	 * @throws IllegalStateException if the <code>Transaction</code> is not
	 *                               active
	 */

	public void rollback ()
	{
		this.log.trace ("rollback:");

		if (! this.active)
		{
			this.log.error ("Can not Rollback:  Transaction not active");
			throw new IllegalStateException ("No active transaction");
		}

		this.active = false;
		this.rollback = false;

		// re-close the DomainModel to make sure that it is cleaned up.
		if (! this.model.isOpen ())
		{
			this.model.close ();
		}
	}
}
