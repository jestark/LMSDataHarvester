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
 * A representation of the role of a user in a particular <code>Course</code>.
 * The <code>Role</code> describes the nature of the participation in the
 * <code>Course</code> by the <code>User</code>.  Common roles include:
 * Instructor, Student and Teaching Assistant.
 * <p>
 * Within the domain model the <code>Role</code> interface is a root level
 * element, as such instances of the <code>Role</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Role</code> interface is required for an instance of
 * the <code>Enrolment</code> interface to exist.  If a particular instance of
 * the <code>Role</code> interface is deleted, then all of the associated
 * instances of the <code>Enrolment</code> interface must be deleted as well.
 * <p>
 * Once created, <code>Role</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     RoleBuilder
 * @see     RoleLoader
 */

public interface Role extends Element
{
	/**
	 * Enumeration of all of the properties of an <code>Role</code>.
	 * Properties represent the data contained within the <code>Role</code>
	 * instance.
	 */

	public static enum Properties { ID, NAME };

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();
}

