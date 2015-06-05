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
 * Create and modify <code>User</code> instances.  This interface extends the
 * <code>ElementBuilder</code> interface with the necessary functionality to
 * handle <code>User</code> instances.  Implementations of this interface
 * should allow for the modification of the "Firstname" and "Lastname" fields
 * of existing <code>User</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface UserBuilder extends ElementBuilder<User>
{
	/**
	 * Get the (student) ID number of the <code>User</code>.  This will be the
	 * student number, or a similar identifier used to track the
	 * <code>User</code> by the institution from which the data was harvested.
	 * While the ID number is not used as the database identifier it is
	 * expected to be unique.
	 *
	 * @return An <code>Integer</code> representation of the ID number.
	 */

	public abstract Integer getIdNumber ();

	/**
	 * Set the (student) ID number of the <code>User</code>.
	 *
	 * @param  idnumber                 The ID Number, not null
	 *
	 * @return                          A reference to this
	 *                                  <code>UserBuilder</code>
	 *
	 * @throws IllegalArgumentException If the ID number is negative
	 */

	public abstract UserBuilder setIdNumber (Integer idnumber);

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public abstract String getFirstname ();

	/**
	 * Set the first name of the <code>User</code>.
	 *
	 * @param  firstname                The first name of the
	 *                                  <code>User</code>, not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the firstname is an empty
	 */

	public abstract UserBuilder setFirstname (String firstname);

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public abstract String getLastname ();

	/**
	 * Set the last name of the <code>User</code>.
	 *
	 * @param  lastname                 The last name of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the lastname is an empty
	 */

	public abstract UserBuilder setLastname (String lastname);

	/**
	 * Get the username for the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the username for the
	 *         <code>User</code>
	 */

	public abstract String getUsername ();

	/**
	 * Set the username of the <code>User</code>.
	 *
	 * @param  username                 The username of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the username is an empty
	 */

	public abstract UserBuilder setUsername (String username);

	/**
	 * Create an association between the <code>User</code> and the specified
	 * <code>Enrolment</code>.  Note that only one <code>User</code> may be
	 * associated with a given <code>Enrolment</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code> to be
	 *                                  associated with the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If there is already a <code>User</code>
	 *                                  associated with the
	 *                                  <code>Enrolment</code>
	 */

	public abstract UserBuilder addEnrolment (Enrolment enrolment);

	/**
	 * Break an association between the <code>User</code> and the specified
	 * <code>Enrolment</code>.  To break an association between the
	 * <code>User</code> and the specified <code>Enrolment</code>, both
	 * the <code>User</code> and <code>Enrolment</code> must be exist in
	 * the <code>DataStore</code> associated with the <code>UserBuilder</code>
	 * instance that is to break the association. Furthermore, there must be an
	 * existing association between the <code>User</code> and the
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code> to remove
	 *                                  from the <code>User</code>, not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If there is no association between the
	 *                                  <code>User</code> and the
	 *                                  <code>Enrolment</code>
	 */

	public abstract UserBuilder removeEnrolment (Enrolment enrolment);
}
