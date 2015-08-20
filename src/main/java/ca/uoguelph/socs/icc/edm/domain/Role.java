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

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the role of a user in a particular <code>Course</code>.
 * The <code>Role</code> describes the nature of the participation in the
 * <code>Course</code> by the <code>User</code>.  Common roles include:
 * Instructor, Student and Teaching Assistant.
 * <p>
 * Within the domain model the <code>Role</code> interface is a root level
 * element, as such instances of the <code>Role</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Role</code> interface is required for an instance of
 * the <code>Enrolment</code> interface to exist.  If a particular instance of
 * the <code>Role</code> interface is deleted, then all of the associated
 * instances of the <code>Enrolment</code> interface must be deleted as well.
 * <p>
 * Once created, <code>Role</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     RoleBuilder
 * @see     RoleLoader
 */

public abstract class Role extends Element
{
	/** The name of the <code>Role</code> */
	public static final Property<String> NAME;

	/** Select an <code>Role</code> instance by its name */
	public static final Selector SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Role</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", false, true);

		SELECTOR_NAME = Selector.getInstance (NAME, true);

		Definition.getBuilder (Role.class, Element.class)
			.addProperty (NAME, Role::getName, Role::setName)
			.addRelationship (Enrolment.class, Enrolment.ROLE, Enrolment.SELECTOR_ROLE)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get the name of the <code>Role</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Role</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Role</code> instance is
	 * loaded.
	 *
	 * @param name The name of the <code>Role</code>
	 */

	protected abstract void setName (String name);
}

