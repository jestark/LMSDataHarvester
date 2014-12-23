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

package ca.uoguelph.socs.icc.edm.domain.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGenerator;

public final class DefaultRoleBuilder extends AbstractBuilder<Role> implements RoleBuilder
{
	/** The logger */
	private final Log log;

	/** <code>ElementFactory</code> to build the role */
	private final RoleElementFactory factory;

	/** The name of the Role */
	private String name;

	protected DefaultRoleBuilder (RoleManager manager, RoleElementFactory factory, IdGenerator generator)
	{
		super (manager);

		this.factory = factory;
		this.log = LogFactory.getLog (DefaultRoleBuilder.class);
	}

	@Override
	protected Role build ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The role's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		return this.factory.create (this.name);
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
	public RoleBuilder setName (String name)
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
