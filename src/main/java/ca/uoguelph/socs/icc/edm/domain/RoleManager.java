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

/**
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class RoleManager extends AbstractManager<Role>
{
	/**
	 * Create the Role manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this Role manager.
	 */

	protected RoleManager (DomainModel model)
	{
		super (model);
	}

	/**
	 * Retrieve a role object from the underlying datastore based on its name.
	 *
	 * @param name The name of the role.
	 * @return A role object
	 */

	public Role fetchByName (String name)
	{
		return null;
	}
}
