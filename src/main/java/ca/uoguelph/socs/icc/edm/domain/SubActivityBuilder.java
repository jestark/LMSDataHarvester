/* Copyright (C) 2015 James E. Stark
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
 * Create <code>SubActivity</code> instances.  This interface extends the
 * <code>ElementBuilder</code> interface with the functionality required to
 * handle <code>SubActivity</code> instances.  It is indented that this
 * interface will act as a base for all of the <code>ElementBuilder</code>
 * implementations which handle <code>SubActivity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface SubActivityBuilder extends ElementBuilder<Activity>
{
	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>SubActivity</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>SubActivity</code>.
	 *
	 * @param  name                     The name of the
	 *                                  <code>SubActivity</code>, not null
	 *
	 * @return                          This <code>SubActivityBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	public abstract SubActivityBuilder setName (String name);

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract Activity getParent ();
}
