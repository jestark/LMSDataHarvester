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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Create, Insert and remove users from the data store.  Implementations of
 * this interface are responsible for adding users to and removing users from
 * the data store.
 *
 * It should be noted that since the binding between users and enrolments is
 * weak, a recursive removal of a user will not remove the associated
 * enrolments, and the existence of associated enrolments will not prevent
 * the non-recursive removal of a user.
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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultUserManager</code>
		 * @return       The <code>DefaultUserManager</code>
		 */

		@Override
		public UserManager create (DomainModel model)
		{
			return new DefaultUserManager (model);
		}
	}

	/** The logger */
	private final Logger log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (User.class, UserManager.class, DefaultUserManager.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultUserManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>DefaultUserManager</code> is to be created, not null
	 */

	public DefaultUserManager (DomainModel model)
	{
		super (User.class, model);

		this.log = LoggerFactory.getLogger (UserManager.class);
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified id number,
	 * from the data-store.
	 *
	 * @param  idnumber The id number of the <code>User</code> to retrieve, not null
	 * @return          The <code>User</code> object associated with the id number
	 */

	public User fetchByIdNumber (Integer idnumber)
	{
		this.log.trace ("Fetching user with ID number: {}", idnumber);

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
	 * from the data-store.
	 *
	 * @param  username The username of the entry to retrieve, not null
	 * @return          The <code>User</code> object associated with the username
	 */

	public User fetchByUsername (String username)
	{
		this.log.trace ("Fetching user with username: {}", username);

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
