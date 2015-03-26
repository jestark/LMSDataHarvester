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

/**
 * Manage <code>Role</code> instances in the <code>DataStore</code>.  This
 * interface extends <code>ElementManager</code> with the extra functionality
 * required to handle <code>Role</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     RoleBuilder
 */

public interface RoleManager extends ElementManager<Role>
{
	/**
	 * Get an instance of the <code>RoleBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>RoleBuilder</code> instance
	 */

	public abstract RoleBuilder getBuilder ();

	/**
	 * Retrieve a <code>Role</code> instance from the <code>DataStore</code>
	 * based on  its name.
	 *
	 * @param  name The name of the <code>Role</code>, not null
	 *
	 * @return      A <code>Role</code> instance
	 */

	public abstract Role fetchByName (String name);
}
