/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.datastore;

/**
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class JPADataStoreTransaction implements DataStoreTransaction
{
	/** The data store */
	private final JPADataStore datastore;

	/**
	 *
	 * @param datastore
	 */

	protected JPADataStoreTransaction (JPADataStore datastore)
	{
		this.datastore = datastore;
	}

	/**
	 *
	 */

	@Override
	public Boolean isActive ()
	{
		return ((this.datastore.getEntityManager ()).getTransaction ()).isActive ();
	}
	
	/**
	 *
	 */

	@Override
	public Boolean getRollbackOnly ()
	{
		return ((this.datastore.getEntityManager ()).getTransaction ()).getRollbackOnly ();
	}
	
	/**
	 * 
	 */

	@Override
	public void setRollbackOnly ()
	{
		((this.datastore.getEntityManager ()).getTransaction ()).setRollbackOnly ();
	}
	
	/**
	 * Begin a new transaction.
	 */

	@Override
	public void begin ()
	{
		((this.datastore.getEntityManager ()).getTransaction ()).begin ();
	}
	
	/**
	 * Commit the transaction.
	 */

	@Override
	public void commit ()
	{
		((this.datastore.getEntityManager ()).getTransaction ()).commit ();
	}
	
	/**
	 * Rollback the transaction.
	 */

	@Override
	public void rollback ()
	{
		((this.datastore.getEntityManager ()).getTransaction ()).rollback ();
	}
}
