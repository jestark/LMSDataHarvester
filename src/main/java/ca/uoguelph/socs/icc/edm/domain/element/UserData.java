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

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultUserBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>User</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>User</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>User</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultUserBuilder
 */

public class UserData extends AbstractElement implements User, Serializable
{
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

	/**
	 * Static initializer to register the <code>UserData</code> class with the
	 * factories.
	 */

	static
	{
		DefinitionBuilder<User, UserData> builder = DefinitionBuilder.newInstance (User.class, UserData.class);
		builder.setCreateMethod (UserData::new);

		builder.addUniqueAttribute ("id", Long.class, false, false, UserData::getId, UserData::setId);
		builder.addUniqueAttribute ("idnumber", Integer.class, true, false, UserData::getIdNumber, UserData::setIdNumber);
		builder.addUniqueAttribute ("username", String.class, true, false, UserData::getUsername, UserData::setUsername);

		builder.addAttribute ("firstname", String.class, true, true, UserData::getFirstname, UserData::setFirstname);
		builder.addAttribute ("lastname", String.class, true, true, UserData::getLastname, UserData::setLastname);

		builder.addRelationship ("enrolments", Enrolment.class, UserData::addEnrolment, UserData::removeEnrolment);

		AbstractElement.registerElement (builder.build (), DefaultUserBuilder.class);
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

		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Create a new <code>User</code> instance.
	 *
	 * @param  idnumber  The user's ID number, not null
	 * @param  firstname The user's first name, not null
	 * @param  lastname  The user's last name, not null
	 * @param  username  The user's username, not null
	 */

	public UserData (final Integer idnumber, final String firstname, final String lastname, final String username)
	{
		assert idnumber != null : "idnumber is NULL";
		assert firstname != null : "firstname is NULL";
		assert lastname != null : "lastname is NULL";
		assert username != null : "username is NULL";

		this.id = null;
		this.idnumber = idnumber;
		this.username = username;
		this.lastname = lastname;
		this.firstname = firstname;

		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Compare two <code>User</code> instances to determine if they are
	 * equal.  The <code>User</code> instances are compared based upon the
	 * this ID number and the username.
	 *
	 * @param  obj The <code>User</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>User</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof User)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.idnumber, ((User) obj).getIdNumber ());
			ebuilder.append (this.username, ((User) obj).getUsername ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>User</code> instance.
	 * The hash code is computed based upon the following fields ID number and
	 * the username.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1063;
		final int mult = 929;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.idnumber);
		hbuilder.append (this.username);

		return hbuilder.toHashCode ();
	}

	/**
	 * Determine if two <code>User</code> instances are identical.  This method
	 * acts as a stricter form of the equals method.  The equals method only
	 * compares properties that are required to be unique (and therefore
	 * immutable) for the <code>User</code> instance, while this method
	 * compares all of the properties.
	 *
	 * @param  element The <code>Element</code> to compare to the current
	 *                 instance
	 *
	 * @return         <code>True</code> if the <code>Element</code> instances
	 *                 are logically identical, <code>False</code> otherwise
	 */

	@Override
	public boolean identicalTo (final Element element)
	{
		boolean result = false;

		if (element == this)
		{
			result = true;
		}
		else if (element instanceof User)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.idnumber, ((User) element).getIdNumber ());
			ebuilder.append (this.username, ((User) element).getUsername ());
			ebuilder.append (this.lastname, ((User) element).getLastname ());
			ebuilder.append (this.firstname, ((User) element).getFirstname ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>User</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

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

	protected void setId (final Long id)
	{
		this.id = id;
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
	public Integer getIdNumber ()
	{
		return this.idnumber;
	}

	/**
	 * Set the (student) ID number of the <code>User</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>User</code> instance is loaded.
	 *
	 * @param  idnumber The ID Number, not null
	 */

	protected void setIdNumber (final Integer idnumber)
	{
		assert idnumber != null : "idnumber is NULL";

		this.idnumber = idnumber;
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
		return new String (this.firstname + " " + this.lastname);
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
		return new HashSet<Enrolment> (this.enrolments);
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

	protected boolean removeEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		return this.enrolments.remove (enrolment);
	}

	/**
	 * Get a <code>String</code> representation of the <code>User</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>User</code>
	 *         instance
	 */

	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("firstname", this.firstname);
		builder.append ("lastname", this.lastname);
		builder.append ("username", this.username);
		builder.append ("idnumber", this.idnumber);

		return builder.toString ();
	}
}
