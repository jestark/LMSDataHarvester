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
 * Manage <code>Activity</code> instances in the <code>DataStore</code>.  This
 * interface extends <code>ElementManager</code> with the extra functionality
 * required to handle <code>Activity</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityBuilder
 */

public interface ActivityManager extends ElementManager<Activity>
{
	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  builder The <code>ActivityBuilder</code> interface of the
	 *                 builder to be returned, not null
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

	public abstract <T extends ActivityBuilder> T getBuilder (Class<T> builder, ActivityType type);

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  type The <code>ActivityType</code> of the <code>Activity</code>
	 *              to be created by the <code>ActivityBuilder</code>
	 *
	 * @return      An <code>ActivityBuilder</code> instance
	 */

	public abstract ActivityBuilder getBuilder (ActivityType type);

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  builder  The <code>SubActivityBuilder</code> interface of
	 *                  the builder to be returned, not null
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	public abstract <T extends SubActivityBuilder> T getBuilder (Class<T> builder, Activity activity);

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	public abstract SubActivityBuilder getBuilder (Activity activity);

	/**
	 * Get a <code>List</code> of all of the <code>Activity</code> instances
	 * which are associated with a particular <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	public abstract List<Activity> fetchAllForType (ActivityType type);
}
