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

package ca.uoguelph.socs.icc.edm.domain.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultRoleManager extends AbstractManager<Role> implements RoleManager
{
	/** The logger */
	private final Log log;

	/**
	 * Create the <code>RoleManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>RoleManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected DefaultRoleManager (DomainModel model)
	{
		super (model);

		this.log = LogFactory.getLog (RoleManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>RoleBuilder</code>
	 */

	public RoleBuilder getBuilder ()
	{
		return (RoleBuilder) this.fetchBuilder ();
	}

	/**
	 * Retrieve a <code>Role</code> object from the underlying data-store based on
	 * its name.
	 *
	 * @param  name The name of the <code>Role</code>, not null
	 * @return      A <code>Role</code> object
	 */

	public Role fetchByName (String name)
	{
		return null;
	}
}
