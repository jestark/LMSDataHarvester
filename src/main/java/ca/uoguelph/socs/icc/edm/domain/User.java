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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a user within the domain model.  Classes implementing
 * this interface contain the information required to identify the person who
 * is associated with the data (<code>Enrolment</code>, <code>Grade</code>,
 * and <code>LogEntry</code> instances, etc.) contained within the domain
 * model.  As such, instances of the <code>User</code> interface are treated
 * somewhat specially.
 * <p>
 * In the domain model, <code>User</code> is a root level element, as its
 * existence is not dependent on any other components of the domain model.
 * Only <code>Enrolment</code> depends on <code>User</code>, and that
 * dependency is weak.  The domain model is designed such that
 * <code>User</code> is optional.  However, it would be wise to make sure that
 * any <code>DataStore</code> instance that does not contain <code>User</code>
 * instances is completely immutable.
 * <p>
 * With the exception of adding and removing <code>Enrolment<code> instances,
 * <code>User</code> instances, once created, are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     UserBuilder
 * @see     UserLoader
 */

public abstract class User extends Element
{
	/** The first name of the <code>User</code> */
	public static final Property<String> FIRSTNAME;

	/** The last name of the <code>User</code> */
	public static final Property<String> LASTNAME;

	/** The username of the <code>User</code> */
	public static final Property<String> USERNAME;

	/** The <code>Enrolment</code> instances associated with the <code>User</code> */
	public static final Property<Enrolment> ENROLMENTS;

	/** Select the <code>User</code> instance for a <code>Enrolment</code> */
	public static final Selector SELECTOR_ENROLMENTS;

	/** Select an <code>User</code> instance by its username */
	public static final Selector SELECTOR_USERNAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>User</code>.
	 */

	static
	{
		FIRSTNAME = Property.getInstance (String.class, "firstname", Property.Flags.REQUIRED, Property.Flags.MUTABLE);
		LASTNAME = Property.getInstance (String.class, "lastname", Property.Flags.REQUIRED, Property.Flags.MUTABLE);
		USERNAME = Property.getInstance (String.class, "username", Property.Flags.REQUIRED);

		ENROLMENTS = Property.getInstance (Enrolment.class, "enrolments", Property.Flags.MUTABLE, Property.Flags.MULTIVALUED);

		SELECTOR_ENROLMENTS = Selector.getInstance (ENROLMENTS, true);
		SELECTOR_USERNAME = Selector.getInstance (USERNAME, true);

		Definition.getBuilder (User.class, Element.class)
			.addProperty (FIRSTNAME, User::getFirstname, User::setFirstname)
			.addProperty (LASTNAME, User::getLastname, User::setLastname)
			.addProperty (USERNAME, User::getUsername, User::setUsername)
			.addRelationship (ENROLMENTS, User::getEnrolments, User::addEnrolment, User::removeEnrolment)
			.addSelector (SELECTOR_USERNAME)
			.addSelector (SELECTOR_ENROLMENTS)
			.build ();
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
			ebuilder.append (this.getUsername (), ((User) obj).getUsername ());

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
		hbuilder.append (this.getUsername ());

		return hbuilder.toHashCode ();
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

		builder.append ("firstname", this.getFirstname ());
		builder.append ("lastname", this.getLastname ());
		builder.append ("username", this.getUsername ());

		return builder.toString ();
	}

	/**
	 * Get an <code>UserBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates a <code>UserBuilder</code>
	 * on the specified <code>DataStore</code> and initializes it with the
	 * contents of this <code>User</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>UserBuilder</code>
	 */

	@Override
	public UserBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return new UserBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>User</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<User> getMetaData ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (User.class, this.getClass ());
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public abstract String getFirstname ();

	/**
	 * Set the first name of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  firstname The first name, not null
	 */

	protected abstract void setFirstname (String firstname);

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public abstract String getLastname ();

	/**
	 * Set the last name of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  lastname The last name, not null
	 */

	protected abstract void setLastname (String lastname);

	/**
	 * Get the username for the <code>User</code>.  This will be the username
	 * that the <code>User</code> used to access the LMS from which the data
	 * associated with the <code>User</code> was harvested.  The username is
	 * expected to be unique.
	 *
	 * @return A <code>String</code> containing the username for the
	 *         <code>User</code>
	 */

	public abstract String getUsername ();

	/**
	 * Set the username of the <code>User</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>User</code> instance
	 * is loaded.
	 *
	 * @param  username The username, not null
	 */

	protected abstract void setUsername (String username);

	/**
	 * Get the full name of the <code>User</code>.  This method will return a
	 * concatenation of the <code>firstname</code> and <code>lastname</code> of
	 * the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the name of the user.
	 */

	public abstract String getName ();

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

	public abstract Enrolment getEnrolment (Course course);

	/**
	 * Get the <code>Set</code> of <code>Enrolment<code> instances which are
	 * associated with this <code>User</code>.  If there are no associated
	 * <code>Enrolment</code> instances, then the <code>Set</code> will be
	 * empty.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>User</code> instance.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>User</code>
	 * instance is loaded.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (Set<Enrolment> enrolments);

	/**
	 * Add an <code>Enrolment</code> to the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add
	 * @return           <code>True</code> if the enrolment was added to the
	 *                   <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove an <code>Enrolment</code> from the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove
	 * @return           <code>True</code> if the enrolment was removed from
	 *                   the <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

