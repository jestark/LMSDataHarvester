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

package ca.uoguelph.socs.icc.edm.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Role
 */

public final class RoleManager extends DomainModelManager<Role>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>RoleManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>RoleManager</code>is to be retrieved, not null
	 * @return       The <code>RoleManager</code> instance for the specified
	 *               domain model
	 * @see    DomainModel#getManager
	 */

	public static RoleManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (RoleManager.class);
	}

	/**
	 * Create the <code>RoleManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>RoleManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected RoleManager (DomainModel model, DataStoreQuery<Role> query)
	{
		super (model, query);

		this.log = LogFactory.getLog (UserManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>RoleBuilder</code>
	 */

	public RoleBuilder getBuilder ()
	{
		return (RoleBuilder) this.builder;
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
