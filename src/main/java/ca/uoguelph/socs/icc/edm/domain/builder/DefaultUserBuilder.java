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

import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>UserBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultUserBuilder extends AbstractBuilder<User> implements UserBuilder
{
	/**
	 * static initializer to register the <code>DefaultUserBuilder</code> with
	 * the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultUserBuilder.class, DefaultUserBuilder::new);
	}

	/**
	 * Create an instance of the <code>DefaultUserBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>User</code> instance will be inserted
	 */

	protected DefaultUserBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
	}

	/**
	 * Load a <code>User</code> instance into the <code>UserBuilder</code>.
	 * This method resets the <code>UserBuilder</code> and initializes all of
	 * its parameters from the specified <code>User</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  user                     The <code>User</code> to load into the
	 *                                  <code>UserBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>User</code> instance to be loaded
	 *                                  are not valid
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

		this.setPropertyValue ("id", user.getId ());
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

	@Override
	public Integer getIdNumber()
	{
		return this.getPropertyValue (Integer.class, "idnumber");
	}

	/**
	 * Set the (student) ID number of the <code>User</code>.
	 *
	 * @param  idnumber                 The ID Number, not null
	 *
	 * @throws IllegalArgumentException If the ID number is negative
	 */

	@Override
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

		this.setPropertyValue ("idnumber", idnumber);
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	@Override
	public String getFirstname ()
	{
		return this.getPropertyValue (String.class, "firstname");
	}

	/**
	 * Set the first name of the <code>User</code>.
	 *
	 * @param  firstname                The firstname of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the firstname is empty
	 */

	@Override
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

		this.setPropertyValue ("firstname", firstname);
	}

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	@Override
	public String getLastname ()
	{
		return this.getPropertyValue (String.class, "lastname");
	}

	/**
	 * Set the last name of the <code>User</code>.
	 *
	 * @param  lastname                 The lastname of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the lastname is empty
	 */

	@Override
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

		this.setPropertyValue ("lastname", lastname);
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

	@Override
	public String getUsername ()
	{
		return this.getPropertyValue (String.class, "username");
	}

	/**
	 * Set the username of the <code>User</code>.
	 *
	 * @param  username                 The username of the <code>User</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the username is empty
	 */

	@Override
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

		this.setPropertyValue ("username", username);
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

	@Override
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
