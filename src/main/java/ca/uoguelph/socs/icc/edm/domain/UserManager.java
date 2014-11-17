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

public final class UserManager extends Manager<User>
{
	/**
	 * Domain Model Type constant.  Used by the domain model to determine which
	 * implementation classes to use, and for instantiation of this manager.
	 */

	public static final DomainModelType TYPE = DomainModelType.USER;

	/**
	 * Get an instance of the UserManager for the specified domain model.
	 *
	 * @param model The instance of the Domain model for which the UserManager
	 * is to be retrieved.
	 * @return The UserManager instance for the specified domain model.
	 * 
	 * @throws IllegalArguementException If the domain model is null.
	 */

	public static UserManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException ();
		}

		return (UserManager) model.getManager (UserManager.TYPE);
	}

	/**
	 * Create the User manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this User manager.
	 */

	protected UserManager (DomainModel model)
	{
		super (model, UserManager.TYPE);
	}

	/**
	 * Retrieve a single user, with the specified id number from the datastore.
	 *
	 * @param idnumber The (student) id number of the user to retrieve
	 * @return The user object associated with the id number
	 */

	public User fetchByIdNumber (Integer idnumber)
	{
		return null;
	}
	
	/**
	 * Retrieve a single user, with the specified username from the datastore.
	 *
	 * @param username The username of the entery to retrieve.
	 * @return The user object associated with the username
	 */

	public User fetchByUsername (String username)
	{
		return null;
	}
}
