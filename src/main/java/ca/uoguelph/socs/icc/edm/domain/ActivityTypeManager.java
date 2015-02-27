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
 * Manage <code>ActivityType</code> instances in the <code>DataStore</code>.
 * This interface extends <code>ElementManager</code> with the extra
 * functionality required to handle <code>ActivityType</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityTypeBuilder
 */

public interface ActivityTypeManager extends ElementManager<ActivityType>
{
	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivityTypeBuilder</code> instance
	 */

	public abstract ActivityTypeBuilder getBuilder ();

	/**
	 * Retrieve the <code>ActivityType</code> object from the underlying 
	 * data-store which has the specified <code>ActivitySource</code> and name.
	 *
	 * @param  source The <code>ActivitySource</code> containing the 
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 *
	 * @return        The <code>ActivityType</code> object which is associated
	 *                with the specified source and name
	 */

	public abstract ActivityType fetchByName (ActivitySource source, String name);
}
