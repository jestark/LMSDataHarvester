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
 * Create new <code>Action</code> instances.  This interface extends the
 * <code>ElementBuilder</code> interface with the functionality required to
 * create <code>Action</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface ActionBuilder extends ElementBuilder<Action>
{
	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Action</code>.
	 *
	 * @param  name                     The name of the <code>Action</code>, not
	 *                                  null
	 *
	 * @return                          This <code>ActionBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	public abstract ActionBuilder setName (String name);
}
