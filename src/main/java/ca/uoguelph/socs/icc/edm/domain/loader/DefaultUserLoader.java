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

package ca.uoguelph.socs.icc.edm.domain.loader;

import java.util.Map;
import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;
import ca.uoguelph.socs.icc.edm.domain.UserLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>UserLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultUserLoader extends AbstractLoader<User> implements UserLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultUserLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<UserLoader>
	{
		/**
		 * Create an instance of the <code>DefaultUserLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultUserLoader</code> will be acting,
		 *                   not null
		 * @return           The <code>DefaultUserLoader</code>
		 */

		@Override
		public UserLoader create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultUserLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (UserLoader.class, DefaultUserLoader.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultUserLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code> instance which the
	 *                   <code>DefaultUserLoader</code> will be manipulating,
	 *                   not null
	 */

	public DefaultUserLoader (final DataStore datastore)
	{
		super (User.class, datastore);
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
