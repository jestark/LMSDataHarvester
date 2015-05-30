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
	/** The user's (student) ID number */
	private Integer idnumber;

	/** The user's first name */
	private String firstname;

	/** The user's last name */
	private String lastname;

	/** This user's username (login id) */
	private String username;

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
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>User</code> instance will be inserted
	 */

	protected DefaultUserBuilder (final DataStore datastore)
	{
		super (User.class, datastore);

		this.clear ();
	}

	@Override
	protected User buildElement ()
	{
		this.log.trace ("build the User");

		if (this.idnumber == null)
		{
			this.log.error ("Can not build: The user's idnumber is not set");
			throw new IllegalStateException ("id number not set");
		}

		if (this.firstname == null)
		{
			this.log.error ("Can not build: The user's first name is not set");
			throw new IllegalStateException ("first name not set");
		}

		if (this.lastname == null)
		{
			this.log.error ("Can not build: The user's last name is not set");
			throw new IllegalStateException ("last name not set");
		}

		if (this.username == null)
		{
			this.log.error ("Can not build: The user's user name is not set");
			throw new IllegalStateException ("user name not set");
		}

		User result = this.element;

		if ((this.element == null) || (! this.username.equals (this.element.getUsername ()) || (! this.idnumber.equals (this.element.getIdNumber ()))))
		{
			this.log.debug ("Creating a new User Instance");

//			result = this.factory.create (this.idnumber, this.firstname, this.lastname, this.username);
		}
		else
		{
			if (! this.firstname.equals (this.element.getFirstname ()))
			{
//				this.factory.setFirstname (this.element, this.firstname);
			}

			if (! this.firstname.equals (this.element.getFirstname ()))
			{
//				this.factory.setLastname (this.element, this.lastname);
			}
		}

		return result;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.idnumber = null;
		this.firstname = null;
		this.lastname = null;
		this.username = null;
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
		this.log.trace ("Load User: {}", user);

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
		return this.idnumber;
	}

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

	@Override
	public UserBuilder setIdNumber (final Integer idnumber)
	{
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

		this.idnumber = idnumber;

		return this;
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
		return this.firstname;
	}

	/**
	 * Set the first name of the <code>User</code>.
	 *
	 * @param  firstname                The firstname of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the firstname is an empty
	 */

	@Override
	public UserBuilder setFirstname (final String firstname)
	{
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

		this.firstname = firstname;

		return this;
	}

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	@Override
	public String getLastname ()
	{
		return this.lastname;
	}

	/**
	 * Set the last name of the <code>User</code>.
	 *
	 * @param  lastname                 The lastname of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the lastname is an empty
	 */

	@Override
	public UserBuilder setLastname (final String lastname)
	{
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

		this.lastname = lastname;

		return this;
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
		return this.username;
	}

	/**
	 * Set the username of the <code>User</code>.
	 *
	 * @param  username                 The username of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the username is an empty
	 */

	@Override
	public UserBuilder setUsername (final String username)
	{
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

		this.username = username;

		return this;
	}
}
