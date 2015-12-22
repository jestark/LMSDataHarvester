/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.moodle;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

/**
 * Representation of a user's registration in a <code>Course</code>.  This class
 * caries the data that will be used to create an <code>Enrolment</code>
 * instance.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class Registration
{
	/** The name of the user's role in the <code>Course</code> */
	private final String role;

	/** The username of the registered user */
	private final String user;

	/** The final grade of the user in the <code>Course</code> */
	private final @Nullable Integer grade;

	/** Can the data for this user be used for research? */
	private final Boolean usable;

	/**
	 * Create the <code>Registration</code>.
	 *
	 * @param role   The user's role in the <code>Course</code>, not null
	 * @param user   The username of the registered user, not null
	 * @param usable Can the data for this user be used for research, not null
	 * @param grade  The user's final grade in the course, may be null
	 * @return       The <code>Registration</code>
	 */

	static Registration create (
			final String role,
			final String username,
			final Boolean usable,
			final @Nullable Integer grade)
	{
		assert role != null : "role is NULL";
		assert username != null : "username is NULL";
		assert usable != null : "usable is NULL";

		return new Registration (role, username, usable, grade);
	}

	/**
	 * Create the <code>Registration</code>.
	 *
	 * @param role   The user's role in the <code>Course</code>, not null
	 * @param user   The username of the registered user, not null
	 * @param usable Can the data for this user be used for research, not null
	 * @param grade  The user's final grade in the course, may be null
	 */

	private Registration (
			final String role,
			final String user,
			final Boolean usable,
			final @Nullable Integer grade)
	{
		this.role = role;
		this.user = user;
		this.grade =  grade;
		this.usable = usable;
	}

	/**
	 * Get the user's final grade in the course.
	 *
	 * @return The user's final grade
	 */

	@CheckReturnValue
	public Integer getGrade ()
	{
		return this.grade;
	}

	/**
	 * Get the name of the user's role in the course.
	 *
	 * @return The name of the role
	 */

	public String getRole ()
	{
		return this.role;
	}

	/**
	 * Get the usable flag.
	 *
	 * @return The usable flag
	 */

	public Boolean getUsable ()
	{
		return this.usable;
	}

	/**
	 * Get the username
	 *
	 * @return The username
	 */

	public String getUsername ()
	{
		return this.user;
	}
}
