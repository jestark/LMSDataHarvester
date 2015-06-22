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

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;

/**
 * A representation of the grade received by a <code>User</code> for a
 * particular <code>Activity</code>.  Instances of the <code>Grade</code>
 * interface are identified by the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  As such, an
 * <code>ElementLoader</code> does not exist for the <code>Grade</code>
 * interface, as the <code>Grade</code> instance may be retrieved from the
 * associated <code>Enrolment</code> or <code>Activity</code>.
 * <p>
 * Within the domain model, <code>Grade</code> is a leaf level interface.  No
 * instances of any other domain model interface depend upon the existence of
 * an instance of the <code>Grade</code> interface.  Instances of the
 * <code>Grade</code> interface have strong dependencies upon instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces.  If an instance
 * of the <code>Enrolment</code> or <code>Activity</code> interfaces is
 * deleted, then all of the associated instances of the <code>Grade</code>
 * interface must be deleted as well.
 * <p>
 * Once created, <code>Grade</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     GradeBuilder
 */

public interface Grade extends Element
{
	/**
	 * Constants representing all of the properties of an <code>Grade</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>Grade</code> instance.
	 */

	public static class Properties
	{
		/** The associated <code>Activity</code> */
		public static final Property<Activity> ACTIVITY = Property.getInstance (Grade.class, Activity.class, "activity", false, true);

		/** The associated <code>Enrolment</code> */
		public static final Property<Enrolment> ENROLMENT = Property.getInstance (Grade.class, Enrolment.class, "enrolment", false, true);

		/** The assigned grade */
		public static final Property<Integer> GRADE = Property.getInstance (Grade.class, Integer.class, "grade", true, true);
	}

	/**
	 * Get the name of the <code>Enrolment</code> to which the
	 * <code>Grade</code> is assigned.  This is a convenience method which
	 * return the result from the <code>getName</code> method on the associated
	 * <code>Enrolment</code> instance.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    Enrolment#getName
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public abstract Activity getActivity ();

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment ();

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade.
	 */

	public abstract Integer getGrade ();
}
