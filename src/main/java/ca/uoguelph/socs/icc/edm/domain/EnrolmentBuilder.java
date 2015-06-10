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

/**
 * Create new <code>Enrolment</code> instances.  This interface extends the
 * <code>ElementBuilder</code> by adding the functionality required to
 * create <code>Enrolment</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Enrolment
 */

public interface EnrolmentBuilder extends ElementBuilder<Enrolment>
{
	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse ();

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Course</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setCourse (Course course);

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public abstract Role getRole ();

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  role                      The <code>Role</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Role</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setRole (Role role);

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
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 *
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

	public abstract void setFinalGrade (Integer finalgrade);

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public abstract Boolean isUsable ();

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	public abstract void setUsable (Boolean usable);
}
