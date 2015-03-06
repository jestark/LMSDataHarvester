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

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>RoleManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultRoleManager extends AbstractManager<Role> implements RoleManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultRoleManager</code>.
	 */

	private static final class Factory implements ManagerFactory<RoleManager>
	{
		/**
		 * Create an instance of the <code>DefaultRoleManager</code>.
		 *
		 * @param  model The <code>DataStore</code> upon which the
		 *               <code>DefaultRoleManager</code> will operate, not null
		 * @return       The <code>DefaultRoleManager</code>
		 */

		@Override
		public RoleManager create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultRoleManager (datastore);
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
		AbstractManager.registerManager (RoleManager.class, DefaultRoleManager.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultRoleManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>DefaultRoleManager</code> will operate, not null
	 */

	public DefaultRoleManager (final DataStore datastore)
	{
		super (Role.class, datastore);

		this.log = LoggerFactory.getLogger (RoleManager.class);
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> interface, suitable for use
	 * with the <code>DataStore</code>.
	 *
	 * @return An <code>RoleBuilder</code> instance
	 */

	@Override
	public RoleBuilder getBuilder ()
	{
		return this.getBuilder (RoleBuilder.class);
	}

	/**
	 * Retrieve an <code>Role</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Role</code>.
	 *
	 * @param  role The <code>Role</code> to retrieve, not null
	 *
	 * @return        A reference to the <code>Role</code> in the
	 *                <code>DataStore</code>, may be null
	 */

	@Override
	public Role fetch (final Role role)
	{
		this.log.trace ("Fetching Role with the same identity as: {}", role);

		if (role == null)
		{
			this.log.error ("The specified Role is NULL");
			throw new NullPointerException ();
		}

		Role result = role;

		if (! (this.fetchQuery ()).contains (role))
		{
			result = this.fetchByName (role.getName ());
		}

		return result;
	}

	/**
	 * Retrieve a <code>Role</code> object from the underlying
	 * <code>DataStore</code> based on its name.
	 *
	 * @param  name The name of the <code>Role</code>, not null
	 * @return      A <code>Role</code> object
	 */

	public Role fetchByName (final String name)
	{
		this.log.trace ("Fetching Role with name: {}", name);

		if (name == null)
		{
			this.log.error ("The specified Role name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("name", name);

		return (this.fetchQuery ()).query ("name", params);
	}
}
