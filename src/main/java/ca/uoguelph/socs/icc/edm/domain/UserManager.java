/* Copyright (C) 2014 James E. Stark
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
 * Create, Insert and remove users from the data store.  Implementations of
 * this interface are responsible for adding users to and removing users from
 * the data store.
 *
 * It should be noted that since the binding between users and enrolments is
 * weak, a recursive removal of a user will not remove the associated
 * enrolments, and the existence of associated enrolments will not prevent
 * the non-recursive removal of a user.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface UserManager extends ElementManager<User>
{
	/**
	 * Retrieve a single <code>User</code> object, with the specified id number,
	 * from the data-store.
	 *
	 * @param  idnumber The id number of the <code>User</code> to retrieve, not null
	 * @return          The <code>User</code> object associated with the id number
	 */

	public abstract User fetchByIdNumber (Integer idnumber);

	/**
	 * Retrieve a single <code>User</code> object, with the specified username,
	 * from the data-store.
	 *
	 * @param  username The username of the entry to retrieve, not null
	 * @return          The <code>User</code> object associated with the username
	 */

	public abstract User fetchByUsername (String username);
}
