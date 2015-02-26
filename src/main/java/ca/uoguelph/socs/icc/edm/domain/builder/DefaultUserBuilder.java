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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultUserBuilder extends AbstractBuilder<User> implements UserBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultUserBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<User, UserBuilder>
	{
		/**
		 * Create the <code>UserBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>UserManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>UserManager</code> instance, not null
		 *
		 * @return         The <code>UserBuilder</code>
		 */

		@Override
		public UserBuilder create (final ManagerProxy<User> manager)
		{
			return new DefaultUserBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The user's (student) ID number */
	private Integer idnumber;

	/** The user's first name */
	private String firstname;

	/** The user's last name */
	private String lastname;

	/** This user's username (login id) */
	private String username;

	/**
	 * static initializer to register the <code>DefaultUserBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (UserBuilder.class, DefaultUserBuilder.class, new Factory ());
	}

	/**
	 * create an instance of the <code>DefaultUserBuilder</code>.
	 *
	 * @param  manager The instance of the <code>UserManager</code>, not null
	 */

	protected DefaultUserBuilder (final ManagerProxy<User> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultUserBuilder.class);
		this.clear ();
	}

	@Override
	public User build ()
	{
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

		return null; //this.factory.create (this.idnumber, this.firstname, this.lastname, this.username);
	}

	@Override
	public void clear ()
	{
		this.idnumber = null;
		this.firstname = null;
		this.lastname = null;
		this.username = null;
	}

	/**
	 * Get the (student) ID number of the <code>User</code>.  This will be the
	 * student number, or a similar identifier used to track the <code>User</code>
	 * by the institution from which the data was harvested.  While the ID number
	 * is not used as the database identifier it is expected to be unique.
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
	 * @param  idnumber The ID Number, not null
	 *
	 * @return A reference to this <code>UserBuilder</code>
	 */

	@Override
	public UserBuilder setIdNumber (final Integer idnumber)
	{
		if (idnumber == null)
		{
			this.log.error ("The specified ID number is NULL");
			throw new NullPointerException ("The specified ID number is NULL");
		}

		this.idnumber = idnumber;

		return this;
	}

	@Override
	public String getFirstname ()
	{
		return this.firstname;
	}

	@Override
	public UserBuilder setFirstname (final String firstname)
	{
		if (firstname == null)
		{
			this.log.error ("The specified first name is NULL");
			throw new NullPointerException ("The specified first name is NULL");
		}

		this.firstname = firstname;

		return this;
	}

	@Override
	public String getLastname ()
	{
		return this.lastname;
	}

	@Override
	public UserBuilder setLastname (final String lastname)
	{
		if (lastname == null)
		{
			this.log.error ("The specified last name is NULL");
			throw new NullPointerException ("The specified last name is NULL");
		}

		this.lastname = lastname;

		return this;
	}

	@Override
	public String getUsername ()
	{
		return this.username;
	}

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
