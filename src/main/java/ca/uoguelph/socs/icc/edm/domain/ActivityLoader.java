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

import java.util.List;

/**
 * Load <code>Activity</code> instances from the <code>DataStore</code>.  This
 * interface extends <code>ElementLoader</code> with the extra functionality
 * required to handle <code>Activity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityBuilder
 */

public interface ActivityLoader extends ElementLoader<Activity>
{
	/**
	 * Get a <code>List</code> of all of the <code>Activity</code> instances
	 * which are associated with a particular <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	public abstract List<Activity> fetchAllForType (ActivityType type);
}