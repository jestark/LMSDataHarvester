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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.Map;
import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;
import ca.uoguelph.socs.icc.edm.domain.UserManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>UserManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultUserManager extends AbstractManager<User> implements UserManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultUserManager</code>.
	 */

	private static final class Factory implements ManagerFactory<UserManager>
	{
		/**
		 * Create an instance of the <code>DefaultUserManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultUserManager</code> will be acting,
		 *                   not null
		 * @return           The <code>DefaultUserManager</code>
		 */

		@Override
		public UserManager create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultUserManager (datastore);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (UserManager.class, DefaultUserManager.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultUserManager</code>.
	 *
	 * @param  datastore The <code>DataStore</code> instance which the
	 *                   <code>DefaultUserManager</code> will be manipulating,
	 *                   not null
	 */

	public DefaultUserManager (final DataStore datastore)
	{
		super (User.class, datastore);
	}

	/**
	 * Get an instance of the <code>UserBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>UserBuilder</code> instance
	 */

	@Override
	public UserBuilder getBuilder ()
	{
		return this.getBuilder (UserBuilder.class);
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified ID
	 * number, from the <code>DataStore</code>.
	 *
	 * @param  idnumber The ID number of the <code>User</code> to retrieve, not
	 *                  null
	 *
	 * @return          The <code>User</code> object associated with the ID
	 *                  number
	 */

	public User fetchByIdNumber (final Integer idnumber)
	{
		this.log.trace ("fetchByIdNumber: idnumber={}", idnumber);

		if (idnumber == null)
		{
			this.log.error ("The specified User ID number is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("idnumber", idnumber);

		return (this.fetchQuery ()).query ("idnumber", params);
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified username,
	 * from the <code>DataStore</code>.
	 *
	 * @param  username The username of the entry to retrieve, not null
	 *
	 * @return          The <code>User</code> object associated with the
	 *                  username
	 */

	public User fetchByUsername (final String username)
	{
		this.log.trace ("fetchByUsername: username={}", username);

		if (username == null)
		{
			this.log.error ("The specified user name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("username", username);

		return (this.fetchQuery ()).query ("username", params);
	}
}
