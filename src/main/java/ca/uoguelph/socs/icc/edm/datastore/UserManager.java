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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Create, Insert and remove users from the datastore.  Implementations of
 * this interface are responcible for adding users to and removing users from
 * the datastore.  
 *
 * It should be noted that since the binding between users and enrolments is
 * weak, a recursive removal of a user will not remove the associated 
 * enrolments, and the existence of assoicated enrolments will not prevent 
 * the non0recursive removal of a user. 
 *
 * @see ca.uoguelph.socs.icc.edm.domain.User The User interface.
 * @author James E. Stark
 * @version 1.0
 */

public interface UserManager extends DataStoreManager<User>
{
	/**
	 * Retrieve a single user, with the specified id number from the datastore.
	 *
	 * @param idnumber The (student) id number of the user to retrieve
	 * @return The user object associated with the id number
	 */

	public abstract User fetchByIdNumber (Integer idnumber);
	
	/**
	 * Retrieve a single user, with the specified username from the datastore.
	 *
	 * @param username The username of the entery to retrieve.
	 * @return The user object associated with the username
	 */

	public abstract User fetchByUsername (String username);
}
