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
 * Create <code>Activity</code> instances.  This interface extends the
 * <code>ActivityBuilder</code> interface with the necessary functionality to
 * handle named <code>Activity</code> instances.  It is intended that this
 * interface will act as a base for all <code>ElementBuilder</code>
 * implementations for <code>Activity</code> instances which contain data
 * beyond the associated <code>ActivityType</code> and <code>Course</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface NamedActivityBuilder<T extends Activity> extends ActivityBuilder<T>
{
	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return The name of the <code>Activity</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Activity</code>.
	 *
	 * @param  name                     The name of the <code>Activity</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException if the name is empty
	 */

	public abstract void setName (String name);
}
