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
 * Manage <code>ActivitySource</code> instances in the <code>DataStore</code>.
 * This interface extends <code>ElementManager</code> with the extra
 * functionality required to handle <code>ActivitySource</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySourceBuilder
 */

public interface ActivitySourceManager extends ElementManager<ActivitySource>
{
	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivitySourceBuilder</code> instance
	 */

	public abstract ActivitySourceBuilder getBuilder ();

	/**
	 * Retrieve the <code>ActivitySource</code> object associated with the
	 * specified name from the <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>ActivitySource</code> to retrieve,
	 *              not null
	 *
	 * @return      The <code>ActivitySource</code> instance associated with
	 *              the specified name
	 */

	public abstract ActivitySource fetchByName (String name);
}
