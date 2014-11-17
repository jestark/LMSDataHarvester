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

import java.util.Set;

/**
 * A representation of a user within the domain model.  Classes implementing
 * this interface contain the information required to identify the person who
 * is associated with the data (enrolments, grades, log entries, etc.) 
 * contained within the domain model.  As such, users are treated somewhat 
 * specially.
 *
 * <p>In the domain model, the user is a root level element, as its existence
 * is not dependent on any other components of the domain model.  Only 
 * Enrolment depends on User, and that dependentcy is weak.  The domain model
 * is designed such that User is optional.  However, it would be wise to make
 * sure that any datastore that doe not contain the user's is completely 
 * immutable.</p>
 *
 * <p>With the exception of adding and removing enrolments, User's, once
 * created, are immutable.</p>
 *
 * @see ca.uoguelph.socs.icc.edm.domain.UserManager The user manager.
 * @see ca.uoguelph.socs.icc.edm.domain.Enrolment The Enrolment interface.
 * @author James E. Stark
 * @version 1.0
 */

public interface User extends DomainModelElement
{
	/**
	 * Get the user's id number.  In most cases this will be the user's student
	 * number, or a similar idetifier used to track the student by the students
	 * institution.  While the id number is not used as the database identifier
	 * it is expected to be unique.
	 *
	 * @return An Integer representation of the user's ID Number.
	 */

	public abstract Integer getIdNumber ();
	
	/**
	 * Get the users first (given) name.
	 *
	 * @return A String containing the user's first name.
	 */

	public abstract String getFirstname ();
	
	/**
	 * Get the user's last name.
	 *
	 * @return A String containing the user's last name.
	 */

	public abstract String getLastname ();
	
	/**
	 * Get the user's username.  This will be the username that the user in
	 * question used to access the LMS from which the user's data was harvested.
	 * The username is expected to be unique.
	 *
	 * @return A String containing the user's username.
	 */

	public abstract String getUsername ();
	
	/**
	 * Get the full name of the user.  The output of this methos should be a
	 * String formatted as: <code>"&lt;first name&gt;&nbsp;&lt;last name&gt;"
	 * </code>
	 *
	 * @return A string containing the name of the user.
	 */

	public abstract String getName ();
	
	/**
	 * Get the object describing the user's enrolment in a particular course.
	 *
	 * @param course The course for which the users enrolment is to be retrieved.
	 * @return The enrolment object for the user in the specified course.
	 */

	public abstract Enrolment getEnrolment (Course course);
	
	/**
	 * Get the set of all of the enrolments whiuch are associated with this user.
	 *
	 * @return A Set of enrolment objects.
	 */

	public abstract Set<Enrolment> getEnrolments ();
}

