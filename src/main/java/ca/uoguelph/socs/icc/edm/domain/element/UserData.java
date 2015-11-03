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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;
import java.util.Set;

import java.util.Collections;
import java.util.HashSet;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Implementation of the <code>User</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>User</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>User</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class UserData extends User implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the user. */
	private Long id;

	/** The username of the user. */
	private String username;

	/** The user's first (given) name. */
	private String firstname;

	/** The user's last name (surname). */
	private String lastname;

	/** The set of enrolments which are associated with the user */
	private Set<Enrolment> enrolments;

	/**
	 * Static initializer to register the <code>UserData</code> class with the
	 * factories.
	 */

	static
	{
		User.registerImplementation (UserData.class, UserData::new);
	}

	/**
	 * Create the user with null values.
	 */

	protected UserData ()
	{
		this.id = null;
		this.username = null;
		this.lastname = null;
		this.firstname= null;

		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>User</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded, or by the <code>UserBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new
	 * <code>User</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
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
	 * Set the username of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  username The username, not null
	 */

	@Override
	protected void setUsername (final String username)
	{
		assert username != null : "username is NULL";

		this.username = username;
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	@Override
	public String getFirstname()
	{
		return this.firstname;
	}

	/**
	 * Set the first name of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  firstname The first name, not null
	 */

	@Override
	protected void setFirstname (final String firstname)
	{
		assert firstname != null : "firstname is NULL";

		this.firstname = firstname;
	}

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	@Override
	public String getLastname()
	{
		return this.lastname;
	}

	/**
	 * Set the last name of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  lastname The last name, not null
	 */

	@Override
	protected void setLastname (final String lastname)
	{
		assert lastname != null : "lastname is NULL";

		this.lastname = lastname;
	}

	/**
	 * Get the full name of the <code>User</code>.  This method will return a
	 * concatenation of the <code>firstname</code> and <code>lastname</code> of
	 * the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the name of the user.
	 */

	@Override
	public String getName()
	{
		return String.format ("%s %s", this.firstname, this.lastname);
	}

	/**
	 * Get the <code>Enrolment</code> instance for the <code>User</code> in the
	 * specified <code>Course</code>.
	 *
	 * @param  course The <code>Course</code> for which the
	 *                <code>Enrolment</code> instance is to be retrieved
	 * @return        The <code>Enrolment</code> instance for the
	 *                <code>User</code> in the specified <code>Course</code>,
	 *                or null
	 */

	@Override
	public Enrolment getEnrolment (final Course course)
	{
		return this.getEnrolments ()
			.stream ()
			.filter (x -> x.getCourse ().equals (course))
			.findFirst ()
			.orElse (null);
	}

	/**
	 * Get the <code>Set</code> of <code>Enrolment<code> instances which are
	 * associated with this <code>User</code>.  If there are no associated
	 * <code>Enrolment</code> instances, then the <code>Set</code> will be
	 * empty.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	@Override
	public Set<Enrolment> getEnrolments()
	{
		this.enrolments.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableSet (this.enrolments);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>User</code> instance.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>User</code>
	 * instance is loaded.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	@Override
	protected void setEnrolments (final Set<Enrolment> enrolments)
	{
		assert enrolments != null : "enrolments is NULL";

		this.enrolments = enrolments;
	}

	/**
	 * Add an <code>Enrolment</code> to the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add
	 * @return           <code>True</code> if the enrolment was added to the
	 *                   <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	@Override
	protected boolean addEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		return this.enrolments.add (enrolment);
	}

	/**
	 * Remove an <code>Enrolment</code> from the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove
	 * @return           <code>True</code> if the enrolment was removed from
	 *                   the <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	@Override
	protected boolean removeEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		return this.enrolments.remove (enrolment);
	}
}
