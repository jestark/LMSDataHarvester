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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultUserBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.UserElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.UserFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultUserManager;

/**
 * This class implements the User interface, to represent a user in the domain
 * model.  See the user interface for the details about how the user
 * representations behave.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.User
 */

public class UserData implements User, Serializable
{
	private static final class UserDataFactory implements UserElementFactory
	{
		@Override
		public User create (Integer idnumber, String firstname, String lastname, String username)
		{
			return new UserData (idnumber, firstname, lastname, username);
		}

		@Override
		public void setId (User user, Long id)
		{
			((UserData) user).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the user. */
	private Long id;

	/** The ID number (Student ID) of the user. */
	private Integer idnumber;

	/** The username of the user. */
	private String username;

	/** The user's first (given) name. */
	private String firstname;

	/** The user's last name (surname). */
	private String lastname;

	/** The set of enrolments which are associated with the user */
	private Set<Enrolment> enrolments;

	static
	{
		(UserFactory.getInstance ()).registerElement (UserData.class, DefaultUserManager.class, DefaultUserBuilder.class, new UserDataFactory ());
	}

	/**
	 * Create the user with null values.
	 */

	public UserData ()
	{
		this.id = null;
		this.idnumber = null;
		this.username = null;
		this.lastname = null;
		this.firstname= null;
		this.enrolments = null;
	}

	public UserData (Integer idnumber, String firstname, String lastname, String username)
	{
		this ();

		this.idnumber = idnumber;
		this.username = firstname;
		this.lastname = lastname;
		this.firstname= username;

		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Override the equals method to test the equality of two Users based on the
	 * identifying user data.  The following fields are used to compare two users:
	 *
	 * <ul>
	 * 	<li>The user's id number
	 * 	<li>The user's username
	 * 	<li>The user's first name
	 * 	<li>The user's last name
	 * </ul>
	 * <p>
	 * If all of these fields compare as equal then the user's are considered
	 * to be equal.  A comparison of these fields will only occur if the
	 * object's being compared have different references (otherwise this method
	 * will immediately return true), and the two objects are of the same
	 * class (otherwise this method will immediately return false).</p>
	 *
	 * @param  obj The object to compare to this user
	 * @return     <code>true</code> if the users should be considered to be
	 *             equal, <code>false</code> otherwise.
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof UserData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.idnumber, ((UserData) obj).idnumber);
			ebuilder.append (this.username, ((UserData) obj).username);
			ebuilder.append (this.lastname, ((UserData) obj).lastname);
			ebuilder.append (this.firstname, ((UserData) obj).firstname);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Override the hashCode method to generate a hash code based on the user
	 * data.  The following fields are used to generate the hash code, since they
	 * are immutable:
	 *
	 * <ul>
	 * 	<li>The user's id number
	 * 	<li>The user's username
	 * 	<li>The user's first name
	 * 	<li>The user's last name
	 * </ul>
	 *
	 * @return The hash code for the user.
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1063;
		final int mult = 929;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.idnumber);
		hbuilder.append (this.username);
		hbuilder.append (this.lastname);
		hbuilder.append (this.firstname);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the datastore identifier for the user.  The datastore identifier is a
	 * number that acts as the primary key in the datastore.  As such this number
	 * is unique for every user.
	 *
	 * @return a Long integer containing the user's datastore identifier
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Intialise the user's datastore identifier.  This method is intended to be
	 * used by a datastore (particularly JPA) to initialse the user's datastore
	 * identification when the user is loaded from a data store.
	 *
	 * @param id The user's datastore identifier.
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the user's id number.  In most cases this will be the user's student
	 * number, or a similar idetifier used to track the student by the students
	 * institution.  While the id number is not used as the database identifier
	 * it is expected to be unique.
	 *
	 * @return An Integer representation of the user's id number.
	 */

	@Override
	public Integer getIdNumber ()
	{
		return this.idnumber;
	}

	/**
	 * Intialise the user's id number.  This method is intended to be used by a
	 * datastore (particularly JPA) to initialse the user's id number when the
	 * user is loaded from a data store.
	 *
	 * @param idnumber The user's id number.
	 */

	protected void setIdNumber (Integer idnumber)
	{
		this.idnumber = idnumber;
	}

	/**
	 * Get the user's username.  This will be the username that the user in
	 * question used to access the LMS from which the user's data was harvested.
	 * The username is expected to be unique.
	 *
	 * @return A String containing the user's username.
	 */

	@Override
	public String getUsername ()
	{
		return this.username;
	}

	/**
	 * Intialise the user's username.  This method is intended to be used by a
	 * datastore (particularly JPA) to initialse the user's username when the
	 * user is loaded from a data store.
	 *
	 * @param username The user's username.
	 */

	protected void setUsername (String username)
	{
		this.username = username;
	}

	/**
	 * Get the users first (given) name.
	 *
	 * @return A String containing the user's first name.
	 */


	@Override
	public String getFirstname()
	{
		return this.firstname;
	}

	/**
	 * Initialize the user's first name.  This method is intended to be used by a
	 * datastore (particularly JPA) to initialize the user's first name when the
	 * user is loaded from a data store.
	 *
	 * @param firstname The user's first name.
	 */

	protected void setFirstname (String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * Get the user's last name.
	 *
	 * @return A String containing the user's last name.
	 */

	@Override
	public String getLastname()
	{
		return this.lastname;
	}

	/**
	 * Initialize the user's last name.  This method is intended to be used by a
	 * datastore (particularly JPA) to initialize the user's last name when the
	 * user is loaded from a data store
	 *
	 * @param lastname The user's last name.
	 */

	protected void setLastname (String lastname)
	{
		this.lastname = lastname;
	}

	/**
	 * Get the full name of the user.  The output of this methos should be a
	 * String formatted as: <code>"&lt;first name&gt;&nbsp;&lt;last name&gt;"
	 * </code>
	 *
	 * @return A string containing the name of the user.
	 */

	@Override
	public String getName()
	{
		return new String (this.firstname + " " + this.lastname);
	}

	/**
	 * Get the object describing the user's enrolment in a particular course.
	 *
	 * @param course The course for which the users enrolment is to be retrieved.
	 * @return The enrolment object for the user in the specified course.
	 */

	@Override
	public Enrolment getEnrolment (Course course)
	{
		Enrolment result = null;

		for (Enrolment i : this.enrolments)
		{
			if (course == i.getCourse ())
			{
				result = i;
				break;
			}
		}

		return result;
	}

	/**
	 * Get the set of all of the enrolments which are associated with this user.
	 *
	 * @return A Set of enrolment objects.
	 */

	@Override
	public Set<Enrolment> getEnrolments()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	/**
	 * Initialize the set of enrolment objects associated with this user.  This
	 * method is intended to be used by a datastore (particularly JPA) to
	 * initialse the user's enrolments with the user is loaded from a data store.
	 *
	 * @param enrolments The set of enrolments.
	 */

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	/**
	 * Add an enrolment.  Enrolments must be unique.  If the enrolment which is
	 * to be added to this user evaluates to be equal to a pre-existing enrolment
	 * then the new enrolment will not be added and the this method will return
	 * <code>false</code>.
	 *
	 * @param enrolment The enrolment to add.
	 * @return An indication if the enrolment was successfully added.
	 * <code>True</code> if the enrolment was added to the user entry,
	 * <code>False</code> otherwise.
	 */

	protected boolean addEnrolment (Enrolment enrolment)
	{
		return this.enrolments.add (enrolment);
	}

	/**
	 * Override java.lang.Object's toString method to display the name of the
	 * user.
	 *
	 * @return A String containing the full name of the user.
	 */

	@Override
	public String toString()
	{
		return this.getName();
	}
}
