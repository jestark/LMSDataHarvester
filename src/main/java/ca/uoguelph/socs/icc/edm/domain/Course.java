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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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

public abstract class Course extends Element
{
	/** The name of the <code>Course</code> */
	public static final Property<String> NAME;

	/** The <code>Semester</code> of offering for the <code>Course</code> */
	public static final Property<Semester> SEMESTER;

	/** The year of offering for the <code>Course</code> */
	public static final Property<Integer> YEAR;

	/** The <code>Activity</code> instances associated with the <code>Course</code> */
	public static final Property<Activity> ACTIVITIES;

	/** The <code>Enrolment</code> instances associated with the <code>Course</code> */
	public static final Property<Enrolment> ENROLMENTS;

	/** Select an <code>Course</code> instance by its name and date of offering */
	public static final Selector SELECTOR_OFFERING;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Course</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", false, true);
		SEMESTER = Property.getInstance (Semester.class, "semester", false, true);
		YEAR = Property.getInstance (Integer.class, "year", false, true);

		ACTIVITIES = Property.getInstance (Activity.class, "activities", true, false);
		ENROLMENTS = Property.getInstance (Enrolment.class, "enrolments", true, false);

		SELECTOR_OFFERING = Selector.getInstance ("offering", true, NAME, SEMESTER, YEAR);

		Definition.getBuilder (Course.class, Element.class)
			.addProperty (NAME, Course::getName, Course::setName)
			.addProperty (SEMESTER, Course::getSemester, Course::setSemester)
			.addProperty (YEAR, Course::getYear, Course::setYear)
			.addRelationship (ACTIVITIES, Course::getActivities, Course::addActivity, Course::removeActivity)
			.addRelationship (ENROLMENTS, Course::getEnrolments, Course::addEnrolment, Course::removeEnrolment)
			.addSelector (SELECTOR_OFFERING)
			.build ();
	}

	/**
	 * Get an <code>CourseBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>CourseBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>Course</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>CourseBuilder</code>
	 */

	@Override
	public CourseBuilder getBuilder (final DataStore datastore)
	{
		if (datastore == null)
		{
			throw new NullPointerException ();
		}

		return new CourseBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Course</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Course</code> instance
	 * is loaded.
	 *
	 * @param  name The name of the <code>Course</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public abstract Semester getSemester ();

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Course</code> instance is loaded.
	 *
	 * @param  semester The <code>Semester</code> in which the
	 *                  <code>Course</code> was offered
	 */

	protected abstract void setSemester (Semester semester);

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public abstract Integer getYear ();

	/**
	 * Set the year in which the <code>Course</code> was offered.  This method
	 * is intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  year The year in which the <code>Course</code> was offered
	 */

	protected abstract void setYear (Integer year);

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
	 * Initialize the <code>List</code> of <code>Activity</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  activities The <code>List</code> of <code>Activity</code>
	 *                    instances, not null
	 */

	protected abstract void setActivities (List<Activity> activities);

	/**
	 * Add the specified <code>Activity</code> to the <code>Course</code>.
	 *
	 * @param  activity The <code>Activity</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Activity</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addActivity (Activity activity);

	/**
	 * Remove the specified <code>Activity</code> from the <code>Course</code>.
	 *
	 * @param  activity The <code>Activity</code> to remove,  not null
	 *
	 * @return          <code>True</code> if the <code>Activity</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeActivity (Activity activity);

	/**
	 * Get the <code>Set</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>Set</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (Set<Enrolment> enrolments);

	/**
	 * Add the specified <code>Enrolment</code> to the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove the specified <code>Enrolment</code> from the
	 * <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

