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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Factory interface to create new <code>User</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>User</code> domain
 * model interface.  It also provides the functionality required to set the
 * <code>DataStore</code> ID for the <code>User</code> instance, as well as
 * adding and removing any associated <code>Enrolment</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.UserBuilder
 */

public interface UserElementFactory extends ElementFactory<User>
{
	/**
	 * Create a new <code>User</code> instance.
	 *
	 * @param  idnumber  The user's ID number, not null
	 * @param  firstname The user's first name, not null
	 * @param  lastname  The user's last name, not null
	 * @param  username  The user's username, not null
	 *
	 * @return           The new <code>User</code> instance
	 */

	public abstract User create (Integer idnumber, String firstname, String lastname, String username);

	/**
	 * Add the specified <code>Enrolment</code> to the specified <code>User</code>.
	 *
	 * @param  user      The <code>User</code> to which the <code>Enrolment</code>
	 *                   is to be added, not null
	 * @param  enrolment The <code>Enrolment</code> to add to the
	 *                   <code>User</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added to the <code>User</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean addEnrolment (User user, Enrolment enrolment);

	/**
	 * Remove the specified <code>Enrolment</code> from the specified
	 * <code>User</code>. 
	 *
	 * @param  user      The <code>User</code> from which the <code>Enrolment</code>
	 *                   is to be removed, not null
	 * @param  enrolment The <code>Enrolment</code> to remove from the
	 *                   <code>User</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed from the <code>User</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean removeEnrolment (User user, Enrolment enrolment);
}
