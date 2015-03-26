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
 * Manage <code>Action</code> instances in the <code>DataStore</code>.  This
 * interface extends <code>ElementManager</code> with the extra functionality
 * required to handle <code>Action</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActionBuilder
 */

public interface ActionManager extends ElementManager<Action>
{
	/**
	 * Get an instance of the <code>ActionBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActionBuilder</code> instance
	 */

	public abstract ActionBuilder getBuilder ();

	/**
	 * Retrieve the Action with the specified name from the
	 * <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>Action</code> to retrieve, not null
	 *
	 * @return      The <code>Action</code> associated with the specified name
	 */

	public abstract Action fetchByName (String name);
}
