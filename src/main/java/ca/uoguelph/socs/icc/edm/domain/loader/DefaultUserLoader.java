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

/**
 * Default implementation of the <code>UserLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultUserLoader extends AbstractLoader<User> implements UserLoader
{
	/**
	 * Static initializer to register the <code>UserLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (User.class, DefaultUserLoader::new);
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

	@Override
	public User fetchByIdNumber (final Integer idnumber)
	{
		this.log.trace ("fetchByIdNumber: idnumber={}", idnumber);

		if (idnumber == null)
		{
			this.log.error ("The specified User ID number is NULL");
			throw new NullPointerException ();
		}

		return ((this.fetchQuery ("idnumber")).setParameter ("idnumber", idnumber)).query ();
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

	@Override
	public User fetchByUsername (final String username)
	{
		this.log.trace ("fetchByUsername: username={}", username);

		if (username == null)
		{
			this.log.error ("The specified user name is NULL");
			throw new NullPointerException ();
		}

		return ((this.fetchQuery ("username")).setParameter ("username", username)).query ();
	}
}
