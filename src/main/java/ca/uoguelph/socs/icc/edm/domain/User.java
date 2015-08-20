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

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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
	/** The idnumber for the <code>User</code> */
	public static final Property<Integer> IDNUMBER;

	/** The first name of the <code>User</code> */
	public static final Property<String> FIRSTNAME;

	/** The last name of the <code>User</code> */
	public static final Property<String> LASTNAME;

	/** The username of the <code>User</code> */
	public static final Property<String> USERNAME;

	/** The <code>Enrolment</code> instances associated with the <code>User</code> */
	public static final Property<Enrolment> ENROLMENTS;

	/** Select an <code>User</code> instance by its id number */
	public static final Selector SELECTOR_IDNUMBER;

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
		IDNUMBER = Property.getInstance (Integer.class, "idnumber", false, true);
		FIRSTNAME = Property.getInstance (String.class, "firstname", true, true);
		LASTNAME = Property.getInstance (String.class, "lastname", true, true);
		USERNAME = Property.getInstance (String.class, "username", false, true);

		ENROLMENTS = Property.getInstance (Enrolment.class, "enrolments", true, false);

		SELECTOR_IDNUMBER = Selector.getInstance (IDNUMBER, true);
		SELECTOR_ENROLMENTS = Selector.getInstance (ENROLMENTS, true);
		SELECTOR_USERNAME = Selector.getInstance (USERNAME, true);

		Definition.getBuilder (User.class, Element.class)
			.addProperty (IDNUMBER, User::getIdNumber, User::setIdNumber)
			.addProperty (FIRSTNAME, User::getFirstname, User::setFirstname)
			.addProperty (LASTNAME, User::getLastname, User::setLastname)
			.addProperty (USERNAME, User::getUsername, User::setUsername)
			.addRelationship (ENROLMENTS, User::getEnrolments, User::addEnrolment, User::removeEnrolment)
			.addSelector (SELECTOR_IDNUMBER)
			.addSelector (SELECTOR_USERNAME)
			.addSelector (SELECTOR_ENROLMENTS)
			.build ();
	}

	/**
	 * Get the (student) ID number of the <code>User</code>.  This will be the
	 * student number, or a similar identifier used to track the
	 * <code>User</code> by the institution from which the data was harvested.
	 * While the ID number is not used as the database identifier it is
	 * expected to be unique.
	 *
	 * @return An <code>Integer</code> representation of the ID number.
	 */

	public abstract Integer getIdNumber ();

	/**
	 * Set the (student) ID number of the <code>User</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>User</code> instance is loaded.
	 *
	 * @param  idnumber The ID Number, not null
	 */

	protected abstract void setIdNumber (Integer idnumber);

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

