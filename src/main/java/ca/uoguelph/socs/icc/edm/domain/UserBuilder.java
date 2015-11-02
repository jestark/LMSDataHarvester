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

import java.util.Set;
import java.util.HashSet;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

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

public final class UserBuilder implements Builder<User>
{
	/** The Logger */
	private final Logger log;

	/** Helper to substitute <code>Enrolment</code> instances */
	private final Retriever<Enrolment> enrolmentRetriever;

	/** Helper to operate on <code>User</code> instances */
	private final Persister<User> persister;

	/** Query instance to look up <code>User</code> instances by <code>Enrolment</code> */
	private final Query<User> enrolmentQuery;

	/** Method reference to the constructor of the implementation class */
	private final Supplier<User> supplier;

	/** The loaded or previously built <code>User</code> instance */
	private User user;

	/** The <code>DataStore</code> id number for the <code>User</code> */
	private Long id;

	/** The firstname of the <code>User</code> */
	private String firstname;

	/** The last name of the <code>User</code> */
	private String lastname;

	/** The username of the <code>User</code> */
	private String username;

	/** The associates <code>Enrolment</code> instances */
	private final Set<Enrolment> enrolments;

	/**
	 * Create an instance of the <code>UserBuilder</code>.
	 *
	 * @param  supplier           Method reference to the constructor of the
	 *                            implementation class, not null
	 * @param  persister          The <code>Persister</code> used to store the
	 *                            <code>User</code>, not null
	 * @param  enrolmentRetriever <code>Retriever</code> for
	 *                            <code>Enrolment</code> instances, not null
	 * @param  enrolmentQuery     <code>Query</code> to check if an
	 *                            <code>Enrolment</code> instances is already
	 *                            associated with another <code>User</code>
	 *                            instance, not null
	 */

	protected UserBuilder (final Supplier<User> supplier, final Persister<User> persister, final Retriever<Enrolment> enrolmentRetriever, final Query<User> enrolmentQuery)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.enrolmentRetriever = enrolmentRetriever;
		this.enrolmentQuery = enrolmentQuery;
		this.persister = persister;
		this.supplier = supplier;

		this.user = null;
		this.id = null;
		this.firstname = null;
		this.lastname = null;
		this.username = null;

		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Create an instance of the <code>User</code>.
	 *
	 * @return                       The new <code>User</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public User build ()
	{
		this.log.trace ("build:");

		if (this.firstname == null)
		{
			this.log.error ("Attempting to create an User without a First name");
			throw new IllegalStateException ("firstname is NULL");
		}

		if (this.lastname == null)
		{
			this.log.error ("Attempting to create an User without an Last name");
			throw new IllegalStateException ("lastname is NULL");
		}

		if (this.username == null)
		{
			this.log.error ("Attempting to create an User without a username");
			throw new IllegalStateException ("username is NULL");
		}

		if ((this.user == null)
				|| (! this.persister.contains (this.user))
				|| (! this.username.equals (this.user.getUsername ())))
		{
			User result = this.supplier.get ();
			result.setId (this.id);
			result.setFirstname (this.firstname);
			result.setLastname (this.lastname);
			result.setUsername (this.username);

			this.enrolments.forEach (x -> result.addEnrolment (x));

			this.user = this.persister.insert (this.user, result);

			if (! this.user.equalsAll (result))
			{
				this.log.error ("User is already in the datastore with a name of: {} vs. the specified value of: {}", this.user.getName (), result.getName ());
				throw new IllegalArgumentException ("User is already in the datastore with a different name");
			}
		}
		else
		{
			this.user.setFirstname (this.firstname);
			this.user.setLastname (this.lastname);

			this.enrolments.stream ()
				.filter (x -> ! this.user.getEnrolments ().contains (x))
				.forEach (x -> this.user.addEnrolment (x));
		}

		return this.user;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>UserBuilder</code>
	 */

	public UserBuilder clear ()
	{
		this.log.trace ("clear:");

		this.user = null;
		this.id = null;
		this.firstname = null;
		this.lastname = null;
		this.username = null;

		this.enrolments.clear ();

		return this;
	}

	/**
	 * Load a <code>User</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>User</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  user                     The <code>User</code>, not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>User</code> instance to be
	 *                                  loaded are not valid
	 */

	public UserBuilder load (final User user)
	{
		this.log.trace ("load: user={}", user);

		if (user == null)
		{
			this.log.error ("Attempting to load a NULL User");
			throw new NullPointerException ();
		}

		this.clear ();

		this.user = user;
		this.id = user.getId ();
		this.setUsername (user.getUsername ());
		this.setLastname (user.getLastname ());
		this.setFirstname (user.getFirstname ());

		user.getEnrolments ().forEach (x -> this.addEnrolment (x));

		return this;
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public String getFirstname ()
	{
		return this.firstname;
	}

	/**
	 * Set the first name of the <code>User</code>.
	 *
	 * @param  firstname                The first name of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the first name is empty
	 */

	public UserBuilder setFirstname (final String firstname)
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

		this.firstname = firstname;

		return this;
	}

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public String getLastname ()
	{
		return this.lastname;
	}

	/**
	 * Set the last name of the <code>User</code>.
	 *
	 * @param  lastname                 The last name of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the last name is empty
	 */

	public UserBuilder setLastname (final String lastname)
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

	public String getUsername ()
	{
		return this.username;
	}

	/**
	 * Set the username of the <code>User</code>.
	 *
	 * @param  username                 The user name of the <code>User</code>,
	 *                                  not null
	 *
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If the user name is empty
	 */

	public UserBuilder setUsername (final String username)
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

		this.username = username;

		return this;
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
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If there is already a <code>User</code>
	 *                                  associated with the
	 *                                  <code>Enrolment</code>
	 */

	public UserBuilder addEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("addEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Specified enrolment is NULL");
			throw new NullPointerException ();
		}

		Enrolment add = this.enrolmentRetriever.fetch (enrolment);

		if (add == null)
		{
			this.log.error ("Specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment not in DataStore");
		}

		if (! this.enrolmentQuery.setValue (User.ENROLMENTS, add).queryAll ().isEmpty ())
		{
			this.log.error ("The Enrolment is already assigned to another user");
			throw new IllegalArgumentException ("The Enrolment is already assigned to another User");
		}

		this.enrolments.add (add);

		return this;
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
	 * @return                          This <code>UserBuilder</code>
	 * @throws IllegalArgumentException If there is no association between the
	 *                                  <code>User</code> and the
	 *                                  <code>Enrolment</code>
	 */

	public UserBuilder removeEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("removeEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Specified enrolment is NULL");
			throw new NullPointerException ();
		}

		Enrolment del = this.enrolmentRetriever.fetch (enrolment);

		if (del == null)
		{
			this.log.error ("Specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment not in DataStore");
		}

		this.enrolments.remove (del);

		return this;
	}
}
