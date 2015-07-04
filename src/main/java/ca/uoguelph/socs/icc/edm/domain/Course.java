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
 * A representation of a course within the domain model.  Instances of the
 * <code>Course</code> interface contain the identifying information for a
 * particular offering of a course, including its name and semester (and year)
 * of offering.  The instances of the <code>Course</code> interface act as a
 * container for all of the data, via the associated instances of the
 * <code>Activity</code> and <code>Enrolment</code> interfaces, concerning a
 * particular offering of a course.
 * <p>
 * Within the domain model the <code>Course</code> interface is a root level
 * element, as such instances of the <code>Course</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Course</code> interface is required for an instance of
 * the <code>Enrolment</code>, or <code>Activity</code> interfaces to exist.
 * If a particular instance of the <code>Course</code> interface is deleted,
 * then all of the associated instances of the <code>Activity</code> and
 * <code>Enrolment</code> interfaces must be deleted as well.
 * <p>
 * Once created, <code>Course</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     CourseBuilder
 * @see     CourseLoader
 */

public interface Course extends Element
{
	/** The name of the <code>Course</code> */
	public static final Property<String> NAME = Property.getInstance (Course.class, String.class, "name", false, true);

	/** The <code>Semester</code> of offering for the <code>Course</code> */
	public static final Property<Semester> SEMESTER = Property.getInstance (Course.class, Semester.class, "semester", false, true);

	/** The year of offering for the <code>Course</code> */
	public static final Property<Integer> YEAR = Property.getInstance (Course.class, Integer.class, "year", false, true);

	/** Select an <code>Course</code> instance by its name and date of offering */
	public static final Selector SELECTOR_OFFERING = Selector.getInstance (Course.class, "offering", true, Course.NAME, Course.SEMESTER, Course.YEAR);

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public abstract Semester getSemester ();

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public abstract Integer getYear ();

	/**
	 * Get the <code>List</code> of <code>Activity</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if there are no <code>Activity</code> instances associated with
	 * the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

	public abstract List<Activity> getActivities ();

	/**
	 * Get the <code>Set</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>Set</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();
}

