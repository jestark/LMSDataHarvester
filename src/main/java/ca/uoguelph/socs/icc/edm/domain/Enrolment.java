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

import java.util.List;
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the participation of a particular <code>User</code> in a
 * <code>Course</code>.  The purpose of the <code>Enrolment</code> interface,
 * and its implementations, is to separate data about the participation in a
 * <code>Course</code> by a particular <code>User</code> from the data that
 * identifies the <code>User</code>.
 * <p>
 * Implementations of the <code>Enrolment</code> interface act as anonymous
 * place holders for the <code>User</code> interface, within the remainder of
 * the domain model.  If an implementation of the <code>Enrolment</code>
 * interface does not contain a link to an implementation of the
 * <code>User</code> interface then it must be impossible to identify the user
 * based on the data provided by the <code>Enrolment</code>.  For any given
 * <code>Enrolment</code> the associated <code>User</code> data may, or may
 * not, be present, so implementations must not rely on the presence of the
 * <code>User</code>.
 * <p>
 * The <code>Enrolment</code> interface (and its implementations) has a strong
 * dependency on the <code>Course</code> and <code>Role</code> interfaces, and
 * a weak dependency on the <code>User</code> interface.  Associated instances
 * of the <code>Course</code> and <code>Role</code> interfaces are required for
 * an instance of the <code>Enrolment</code> interface to exist.  If one of
 * these required instances is deleted, then the <code>Enrolment</code>
 * instance must be deleted as well.  An instance of the <code>User</code>
 * interface should be present for the initial creation of the
 * <code>Enrolment</code> instance.  However, if it should be possible, if
 * difficult, to create an <code>Enrolment</code> instance without an
 * associated <code>User</code> instance.  Deletion of the associated
 * <code>User</code> instance should not impact the <code>Enrolment</code>
 * instance.
 * <p>
 * Instances of the <code>Grade</code> and <code>LogEntry</code> interfaces
 * have a strong dependency on the <code>Enrolment</code> interface.  If an
 * instance of the <code>Enrolment</code> interface is deleted, then the
 * associated instances of the <code>Grade</code> and <code>LogEntry</code>
 * interfaces must be deleted as well.
 * <p>
 * With the exception of adding and removing instances of the
 * <code>Grade</code> and <code>LogEntry</code> interfaces, instances of the
 * <code>Enrolment</code> interface, once created, are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     EnrolmentBuilder
 * @see     EnrolmentLoader
 */

public interface Enrolment extends Element
{
	/**
	 * Constants representing all of the properties of an <code>Enrolment</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>Enrolment</code> instance.
	 */

	public static class Properties extends Element.Properties
	{
		/** The associated <code>Course</code> */
		public static final Property<Course> COURSE = Property.getInstance (Enrolment.class, Course.class, "course", false, true);

		/** The final grade */
		public static final Property<Integer> FINALGRADE = Property.getInstance (Enrolment.class, Integer.class, "finalgrade", true, false);

		/** The associated <code>Role</code> */
		public static final Property<Role> ROLE = Property.getInstance (Enrolment.class, Role.class, "role", false, true);

		/** Has consent been given to use this data for research */
		public static final Property<Boolean> USABLE = Property.getInstance (Enrolment.class, Boolean.class, "usable", true, true);
	}

	/**
	 * Constants representing all of the selectors of an <code>Enrolment</code>.  A
	 * <code>Selector</code> represents the <code>Set</code> of
	 * <code>Property</code> instances used to load an <code>Enrolment</code> from
	 * the <code>DataStore</code>.
	 */

	public static class Selectors extends Element.Selectors
	{
		/** Select all <code>Enrolment</code> by <code>Role</code>*/
		public static final Selector ROLE = Selector.getInstance (Enrolment.class, false, Enrolment.Properties.ROLE);
	}

	/**
	 * Get the name associated with the <code>Enrolment</code>.  The contents
	 * of the <code>String</code> returned by this method are implementation
	 * dependent.  If the implementation has access to the <code>User</code>
	 * information this this method should return the result of the
	 * <code>getName</code> method for the associated <code>User</code>
	 * instance, otherwise it should return some other identifier, usually the
	 * identifier for the <code>Enrolment</code> in the <code>DataStore</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    User#getName
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse();

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public abstract Role getRole();

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public abstract Boolean isUsable ();

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	public abstract Integer getFinalGrade ();

	/**
	 * Get the <code>Grade</code> for the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> for which the grade is to be
	 *                  retrieved
	 * @return          The <code>Grade</code> for the specified
	 *                  <code>Activity</code>
	 */

	public abstract Grade getGrade (Activity activity);

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Enrolment</code> instance.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances.
	 */

	public abstract Set<Grade> getGrades ();

	/**
	 * Get the <code>List</code> of <code>LogEntry</code> instances associated
	 * with this <code>Enrolment</code>.  The <code>List</code> will be empty
	 * if there are no associated <code>LogEntry</code> instances.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();
}
