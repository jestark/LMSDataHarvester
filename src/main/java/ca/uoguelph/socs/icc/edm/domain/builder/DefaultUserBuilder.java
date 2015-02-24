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

package ca.uoguelph.socs.icc.edm.domain.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

public final class DefaultUserBuilder extends AbstractBuilder<User> implements UserBuilder
{
	private static class Factory implements BuilderFactory<UserBuilder>
	{
		@Override
		public UserBuilder create (DomainModel model)
		{
			return new DefaultUserBuilder ((AbstractManager<User>) model.getUserManager ());
		}
	}

	/** The logger */
	private final Log log;

	/** <code>ElementFactory</code> to build the user */
	private final UserElementFactory factory;

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

	protected DefaultUserBuilder (AbstractManager<User> manager)
	{
		super (manager);
		this.factory = null;

		this.clear ();

		this.log = LogFactory.getLog (DefaultUserBuilder.class);
	}

	@Override
	protected User build ()
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

		return this.factory.create (this.idnumber, this.firstname, this.lastname, this.username);
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
	 *
	 * @return The ID number of the user
	 */

	@Override
	public Integer getIdNumber()
	{
		return this.idnumber;
	}

	/**
	 *
	 * @param  idnumber The ID number of the user, not null
	 * @return          This <code>UserBuilder</code>
	 */

	@Override
	public UserBuilder setIdNumber (Integer idnumber)
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
	public UserBuilder setFirstname (String firstname)
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
	public UserBuilder setLastname (String lastname)
	{
		if (lastname == null)
		{
			this.log.error ("The specified last name is NULL");
			throw new NullPointerException ("The specified last name is NULL");
		}

		this.lastname = lastname;

		return this;
	}

	/**
	 * 
	 */

	@Override
	public String getUsername ()
	{
		return this.username;
	}

	/**
	 * Set the login id (username) for the user object to be built.
	 *
	 * @param  username                  The username of the user, not null
	 * @return                           The Same UserBuilder instance
	 * @throws IllegalArguementException if the username is empty
	 */

	@Override
	public UserBuilder setUsername (String username)
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
