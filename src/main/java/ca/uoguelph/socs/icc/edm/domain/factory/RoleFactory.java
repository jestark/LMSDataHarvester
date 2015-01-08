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

package ca.uoguelph.socs.icc.edm.domain.factory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManagerFactory;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModelType;
import ca.uoguelph.socs.icc.edm.domain.builder.RoleElementFactory;

public final class RoleFactory extends AbstractManagerFactory<Role, RoleManager, RoleBuilder, RoleElementFactory>
{
	private static RoleFactory instance;

	static
	{
		RoleFactory.instance = new RoleFactory ();
	}

	public static RoleFactory getInstance ()
	{
		return RoleFactory.instance;
	}

	private RoleFactory ()
	{
		super (DomainModelType.ROLE);
	}
}
