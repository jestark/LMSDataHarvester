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

package ca.uoguelph.socs.icc.edm.domain;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Load <code>Role</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Role</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class RoleLoader extends AbstractLoader<Role>
{
	/**
	 * Get an instance of the <code>RoleLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>RoleLoader</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	public static RoleLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, Role.class, RoleLoader::new);
	}

	/**
	 * Create the <code>DefaultRoleLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public RoleLoader (final DataStore datastore)
	{
		super (Role.class, datastore);
	}

	/**
	 * Retrieve a <code>Role</code> object from the underlying
	 * <code>DataStore</code> based on its name.
	 *
	 * @param  name                  The name of the <code>Role</code>, not null
	 *
	 * @return                       The <code>Role</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public Role fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Role name is NULL");
			throw new NullPointerException ();
		}

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		Query<Role> query = this.fetchQuery (Role.SELECTOR_NAME);
		query.setProperty (Role.NAME, name);

		return query.query ();
	}
}
