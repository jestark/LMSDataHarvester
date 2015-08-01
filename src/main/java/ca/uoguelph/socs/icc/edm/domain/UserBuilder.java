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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create and modify <code>User</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the necessary functionality to
 * handle <code>User</code> instances.  The "Firstname" and "Lastname" fields
 * of existing <code>User</code> instances, may be modified in place.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     User
 */

public final class UserBuilder extends AbstractBuilder<User>
{
	/**
	 * Get an instance of the <code>UserBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>UserBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 */

	public static UserBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, User.class, UserBuilder::new);
	}

	/**
	 * Get an instance of the <code>UserBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>User</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  user   The <code>User</code>, not null
	 *
	 * @return           The <code>UserBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 */

	public static UserBuilder getInstance (final DataStore datastore, User user)
	{
		assert datastore != null : "datastore is NULL";
		assert user != null : "user is NULL";

		UserBuilder builder = UserBuilder.getInstance (datastore);
		builder.load (user);

		return builder;
	}

	/**
	 * Get an instance of the <code>UserBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 *
	 * @return         The <code>UserBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static UserBuilder getInstance (final DomainModel model)
	{
		return UserBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Get an instance of the <code>UserBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>User</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @param  user  The <code>User</code>, not null
	 *
	 * @return       The <code>UserBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static UserBuilder getInstance (final DomainModel model, User user)
	{
		if (user == null)
		{
			throw new NullPointerException ("user is NULL");
		}

		UserBuilder builder = UserBuilder.getInstance (model);
		builder.load (user);

		return builder;
	}

	/**
	 * Create an instance of the <code>UserBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code> implementation class, not
	 *                   null
	 */

	protected UserBuilder (final DataStore datastore, final Class<? extends Element> element)
	{
		super (datastore, element);
	}

	/**
	 * Load a <code>User</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>User</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  user                     The <code>User</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>User</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final User user)
	{
		this.log.trace ("load: user={}", user);

		if (user == null)
		{
			this.log.error ("Attempting to load a NULL User");
			throw new NullPointerException ();
		}

		super.load (user);
		this.setIdNumber (user.getIdNumber ());
		this.setUsername (user.getUsername ());
		this.setLastname (user.getLastname ());
		this.setFirstname (user.getFirstname ());

		this.builder.setProperty (User.ID, user.getId ());
	}

	/**
	 * Get the (student) ID number of the <code>User</code>.  This will be the
	 * student number, or a similar identifier used to track the
	 * <code>User</code> by the institution from which the data was harvested.
	 * While the ID number is not used as the database identifier it is
	 * expected to be unique.
	 *
	 * @return An Integer representation of the ID number
	 */

	public Integer getIdNumber()
	{
		return this.builder.getPropertyValue (User.IDNUMBER);
	}

	/**
	 * Set the (student) ID number of the <code>User</code>.
	 *
	 * @param  idnumber                 The ID Number, not null
	 *
	 * @throws IllegalArgumentException If the ID number is negative
	 */

	public void setIdNumber (final Integer idnumber)
	{
		this.log.trace ("setIdNumber: idnumber={}", idnumber);

		if (idnumber == null)
		{
			this.log.error ("The specified ID number is NULL");
			throw new NullPointerException ("The specified ID number is NULL");
		}

		if (idnumber < 0)
		{
			this.log.error ("Trying to set a negative id number");
			throw new IllegalArgumentException ("idnumber is negative");
		}

		this.builder.setProperty (User.IDNUMBER, idnumber);
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public String getFirstname ()
	{
		return this.builder.getPropertyValue (User.FIRSTNAME);
	}

	/**
	 * Set the first name of the <code>User</code>.
	 *
	 * @param  firstname                The firstname of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the firstname is empty
	 */

	public void setFirstname (final String firstname)
	{
		this.log.trace ("setFirstname: firstname={}", firstname);

		if (firstname == null)
		{
			this.log.error ("The specified first name is NULL");
			throw new NullPointerException ("The specified first name is NULL");
		}

		if (firstname.length () == 0)
		{
			this.log.error ("firstname is an empty string");
			throw new IllegalArgumentException ("firstname is empty");
		}

		this.builder.setProperty (User.FIRSTNAME, firstname);
	}

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public String getLastname ()
	{
		return this.builder.getPropertyValue (User.LASTNAME);
	}

	/**
	 * Set the last name of the <code>User</code>.
	 *
	 * @param  lastname                 The lastname of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the lastname is empty
	 */

	public void setLastname (final String lastname)
	{
		this.log.trace ("setLastname: lastname={}", lastname);

		if (lastname == null)
		{
			this.log.error ("The specified last name is NULL");
			throw new NullPointerException ("The specified last name is NULL");
		}

		if (lastname.length () == 0)
		{
			this.log.error ("lastname is an empty string");
			throw new IllegalArgumentException ("lastname is empty");
		}

		this.builder.setProperty (User.LASTNAME, lastname);
	}

	/**
	 * Get the username for the <code>User</code>.  This will be the username
	 * that the <code>User</code> used to access the LMS from which the data
	 * associated with the <code>User</code> was harvested.  The username is
	 * expected to be unique.
	 *
	 * @return A <code>String</code> containing the username for the
	 *         <code>User</code>
	 */

	public String getUsername ()
	{
		return this.builder.getPropertyValue (User.USERNAME);
	}

	/**
	 * Set the username of the <code>User</code>.
	 *
	 * @param  username                 The username of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the username is empty
	 */

	public void setUsername (final String username)
	{
		this.log.trace ("setUsername: username={}", username);

		if (username == null)
		{
			this.log.error ("Specified username is NULL");
			throw new NullPointerException ();
		}

		if (username.length () == 0)
		{
			this.log.error ("Specified username is empty (String has length 0)");
			throw new IllegalArgumentException ("Username is empty");
		}

		this.builder.setProperty (User.USERNAME, username);
	}

	/**
	 * Create an association between the <code>User</code> and the specified
	 * <code>Enrolment</code>.  Note that only one <code>User</code> may be
	 * associated with a given <code>Enrolment</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code> to be
	 *                                  associated with the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If there is already a <code>User</code>
	 *                                  associated with the
	 *                                  <code>Enrolment</code>
	 */

	public void addEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("addEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Specified enrolment is NULL");
			throw new NullPointerException ();
		}

		if (! this.datastore.contains (enrolment))
		{
			this.log.error ("Specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment not in DataStore");
		}
	}

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
	 * @throws IllegalArgumentException If there is no association between the
	 *                                  <code>User</code> and the
	 *                                  <code>Enrolment</code>
	 */

	public void removeEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("removeEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Specified enrolment is NULL");
			throw new NullPointerException ();
		}

		if (! this.datastore.contains (enrolment))
		{
			this.log.error ("Specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment not in DataStore");
		}
	}
}
