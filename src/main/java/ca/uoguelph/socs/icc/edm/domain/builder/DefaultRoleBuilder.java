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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Default implementation of the <code>RoleBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultRoleBuilder extends AbstractBuilder<Role, RoleElementFactory> implements RoleBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultRoleBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Role, RoleBuilder>
	{
		/**
		 * Create the <code>RoleBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>RoleManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>RoleManager</code> instance, not null
		 *
		 * @return         The <code>RoleBuilder</code>
		 */

		@Override
		public RoleBuilder create (final ManagerProxy<Role> manager)
		{
			return new DefaultRoleBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The name of the Role */
	private String name;

	/**
	 * static initializer to register the <code>DefaultRoleBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (RoleBuilder.class, DefaultRoleBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultRoleBuilder</code>.
	 *
	 * @param  manager The <code>RoleManager</code> which the 
	 *                 <code>RoleBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	protected DefaultRoleBuilder (final ManagerProxy<Role> manager)
	{
		super (Role.class, RoleElementFactory.class, manager);

		this.log = LoggerFactory.getLogger (DefaultRoleBuilder.class);
	}

	@Override
	protected Role buildElement ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The role's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		Role result = this.element;

		if ((this.element == null) || (! this.name.equals (this.element.getName ())))
		{
			result = this.factory.create (this.name);
		}

		return result;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	protected void postInsert ()
	{
		// Role is a root element, so do nothing
	}

	@Override
	protected void postRemove ()
	{
		// Role is a root element, so do nothing
	}

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.name = null;
	}

	/**
	 * Load a <code>Role</code> instance into the <code>RoleBuilder</code>.  This
	 * method resets the <code>RoleBuilder</code> and initializes all of its
	 * parameters from the specified <code>Role</code> instance.  The parameters
	 * are validated as they are set.
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
		this.log.trace ("Load Role: {}", role);

		super.load (role);
		this.setName (role.getName ());
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the <code>Role</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Role</code>.
	 *
	 * @param  name                     The name of the <code>Role</code>, not
	 *                                  null
	 *
	 * @return                          This <code>RoleBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public RoleBuilder setName (final String name)
	{
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

		this.name = name;
		
		return this;
	}
}
