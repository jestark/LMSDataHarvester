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
 * Create new <code>Grade</code> instances.  This interface extends the
 * <code>ElementBuilder</code> by adding the functionality required to
 * create <code>Grade</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Grade
 */

public interface GradeBuilder extends ElementBuilder<Grade>
{
	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public abstract Activity getActivity ();

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setActivity (Activity activity);

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment ();

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public abstract void setEnrolment (Enrolment enrolment);

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade, may be
	 *         null
	 */

	public abstract Integer getGrade ();

	/**
	 * Set the value of the <code>Grade</code>.
	 *
	 * @param  grade                    The value of the <code>Grade</code>,
	 *                                  not null
	 *
	 * @return                          This <code>GradeBuilder</code>
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

	public abstract void setGrade (Integer grade);
}
