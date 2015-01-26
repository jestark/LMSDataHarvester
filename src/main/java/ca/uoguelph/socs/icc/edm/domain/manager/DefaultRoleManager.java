/* Copyright (C) 2014,2015 James E. Stark
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
import ca.uoguelph.socs.icc.edm.domain.factory.RoleFactory;

/**
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

	private static final class DefaultRoleManagerFactory implements ManagerFactory<RoleManager>
	{
		/**
		 * Create an instance of the <code>DefaultRoleManager</code>.
		 *
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultRoleManager</code>
		 * @return       The <code>DefaultRoleManager</code>
		 */

		@Override
		public RoleManager create (DomainModel model)
		{
			return new DefaultRoleManager (model);
		}
	}

	/** The logger */
	private final Log log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		(RoleFactory.getInstance ()).registerManager (DefaultRoleManager.class, new DefaultRoleManagerFactory ());
	}

	/**
	 * Create the <code>DefaultRoleManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>DefaultRoleManager</code> is to be created, not null
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
