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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>RoleBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultRoleBuilder extends AbstractBuilder<Role, Role.Properties> implements RoleBuilder
{
	/**
	 * static initializer to register the <code>DefaultRoleBuilder</code> with
	 * the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultRoleBuilder.class, DefaultRoleBuilder::new);
	}

	/**
	 * Create the <code>DefaultRoleBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Role</code> instance will be inserted
	 */

	protected DefaultRoleBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
	}

	/**
	 * Load a <code>Role</code> instance into the <code>RoleBuilder</code>.
	 * This method resets the <code>RoleBuilder</code> and initializes all of
	 * its parameters from the specified <code>Role</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  role                     The <code>Role</code> to load into the
	 *                                  <code>RoleBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Role</code> instance to be loaded
	 *                                  are not valid
	 */

	@Override
	public void load (final Role role)
	{
		this.log.trace ("load: role={}", role);

		if (role == null)
		{
			this.log.error ("Attempting to load a NULL Role");
			throw new NullPointerException ();
		}

		super.load (role);
		this.setName (role.getName ());

		this.setPropertyValue (Role.Properties.ID, role.getId ());
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	@Override
	public String getName ()
	{
		return this.getPropertyValue (String.class, Role.Properties.NAME);
	}

	/**
	 * Set the name of the <code>Role</code>.
	 *
	 * @param  name                     The name of the <code>Role</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public void setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("Name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.setPropertyValue (Role.Properties.NAME, name);
	}
}
