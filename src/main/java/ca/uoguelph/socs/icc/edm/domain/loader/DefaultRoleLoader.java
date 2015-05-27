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

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>RoleLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultRoleLoader extends AbstractLoader<Role> implements RoleLoader
{
	/**
	 * Static initializer to register the <code>RoleLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (Role.class, DefaultRoleLoader::new);
	}

	/**
	 * Create the <code>DefaultRoleLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>DefaultRoleLoader</code> will operate, not
	 *                   null
	 */

	public DefaultRoleLoader (final DataStore datastore)
	{
		super (Role.class, datastore);
	}

	/**
	 * Retrieve a <code>Role</code> object from the underlying
	 * <code>DataStore</code> based on its name.
	 *
	 * @param  name The name of the <code>Role</code>, not null
	 * @return      A <code>Role</code> object
	 */

	@Override
	public Role fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Role name is NULL");
			throw new NullPointerException ();
		}

		return ((this.fetchQuery ("name")).setParameter ("name", name)).query ();
	}
}
