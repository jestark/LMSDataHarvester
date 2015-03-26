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
 *
 * @author James E. Stark
 * @version 1.0
 */

public interface DataStoreTransaction
{
	/**
	 * Determine if the current transaction is active.
	 */

	public abstract Boolean isActive ();

	/**
	 * Determine if the current transaction must be rolled back.
	 */

	public abstract Boolean getRollbackOnly ();

	/**
	 * Force the current transaction to be rolled back.
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
