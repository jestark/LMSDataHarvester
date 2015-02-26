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

public final class DefaultRoleBuilder extends AbstractBuilder<Role> implements RoleBuilder
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

	/** <code>ElementFactory</code> to build the role */
	private final RoleElementFactory factory;

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

	protected DefaultRoleBuilder (final ManagerProxy<Role> manager)
	{
		super (manager);

		this.factory = null;
		this.log = LoggerFactory.getLogger (DefaultRoleBuilder.class);
	}

	@Override
	public Role build ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The role's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		return null; //this.factory.create (this.name);
	}

	@Override
	public void clear ()
	{
		this.name = null;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	@Override
	public RoleBuilder setName (final String name)
	{
		if (name != null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("Name is NULL");
		}

		this.name = name;
		
		return this;
	}
}
