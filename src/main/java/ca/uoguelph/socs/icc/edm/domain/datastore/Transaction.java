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

/**
 * Manage transactions performed against a <code>DataStore</code>.  All write
 * operations on a <code>DataStore</code> must occur within an active
 * transaction.  Instances of this interface allow transactions to be created,
 * committed and rolled back.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface Transaction
{
	/**
	 * Determine if the current <code>Transaction</code> is active.
	 *
	 * @return <code>True</code> if the transaction is active,
	 *         <code>False</code> otherwise
	 */

	public abstract boolean isActive ();

	/**
	 * Determine if the current <code>Transaction</code> must be rolled back.
	 *
	 * @return <code>True</code> if the transaction must be rolled back,
	 *         <code>False</code> otherwise
	 */

	public abstract boolean getRollbackOnly ();

	/**
	 * Force the current <code>Transaction</code> to be rolled back.
	 */

	public abstract void setRollbackOnly ();

	/**
	 * Begin a new transaction.
	 */

	public abstract void begin ();

	/**
	 * Commit the transaction.
	 */

	public abstract void commit ();

	/**
	 * Rollback the transaction.
	 */

	public abstract void rollback ();
}
