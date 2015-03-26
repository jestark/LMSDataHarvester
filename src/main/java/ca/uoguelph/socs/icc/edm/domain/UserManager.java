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
 * Manage <code>User</code> instances in the <code>DataStore</code>.  This
 * interface extends <code>ElementManager</code> with the extra functionality
 * required to handle <code>User</code> instances.
 * <p>
 * Since the binding between <code>User</code> instances and
 * <code>enrolment</code> instances is weak, a <code>User</code> instance may
 * be removed from the <code>DataStore</code>
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     UserBuilder
 */

public interface UserManager extends ElementManager<User>
{
	/**
	 * Get an instance of the <code>UserBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>UserBuilder</code> instance
	 */

	public abstract UserBuilder getBuilder ();

	/**
	 * Retrieve a single <code>User</code> object, with the specified id
	 * number, from the <code>DataStore</code>.
	 *
	 * @param  idnumber The ID number of the <code>User</code> to retrieve, not
	 *                  null
	 *
	 * @return          The <code>User</code> object associated with the ID
	 *                  number
	 */

	public abstract User fetchByIdNumber (Integer idnumber);

	/**
	 * Retrieve a single <code>User</code> object, with the specified username,
	 * from the <code>DataStore</code>.
	 *
	 * @param  username The username of the entry to retrieve, not null
	 *
	 * @return          The <code>User</code> object associated with the
	 *                  username
	 */

	public abstract User fetchByUsername (String username);
}
