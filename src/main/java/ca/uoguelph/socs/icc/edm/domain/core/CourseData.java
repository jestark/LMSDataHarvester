/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Semester;

/**
 * Implementation of the Course interface.  It is expected that this object
 * will be accessed though the Course interface, and its relevant managers.
 * See the Course interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Course
 * @see     CourseManager
 */

public class CourseData implements Course
{
	/**
	 * The datastore id of this course.
	 */

	private Long id;

	/**
	 * The name of this course.
	 */

	private String name;

	/**
	 * The semester in which this course was offered.
	 */

	private Semester semester;

	/**
	 * The year in which this course was offered.
	 */

	private Integer year;

	/**
	 * The set of Activities which are associated with this course.
	 */

	private Set<Activity> activities;

	/**
	 * All of the individuals which are enrolled in this course.
	 */

	private Set<Enrolment> enrolments;

	/**
	 * Create the Course with null values.  Default no-arguement constructor
	 * used by the datastore (particularly the Java Persistence API) when
	 * loading an instance of the class.  This constructor initializes all
	 * values to null and expects that the values in the class will be set via
	 * the (protected) mutator methods.
	 */

	protected CourseData ()
	{
		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;
		this.activities = null;
		this.enrolments = null;
	}

	/**
	 * Override java.lang.object's equals method to compare to courses based on
	 * their names, as well as their semester and year of offering.
	 *
	 * @param obj The reference object with which to compare.
	 * @return <code>true</code> if the two  have the same name and were offered
	 * at the same time, (or if the references are identical),
	 * <code>false</code> otherwise.
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.appendSuper (super.equals (obj));
				ebuilder.append (this.name, ((CourseData) obj).name);
				ebuilder.append (this.year, ((CourseData) obj).year);
				ebuilder.append (this.semester, ((CourseData) obj).semester);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	/**
	 * Override of java.lang.object's hashCode method to compute a hash code
	 * based on the name and time of offering of the course.
	 *
	 * @return The hash code.
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1061;
		final int mult = 937;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.year);
		hbuilder.append (this.semester);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the datastore ID for the course.
	 *
	 * @return The unique numeric ID of the course.
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Internal method used by the datastore and managers to set the datastore
	 * ID for the course.
	 *
	 * @param id The unique numeric ID of the course.
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the course.
	 *
	 * @return The name of the course.
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Internal method used by the datastore and managers to set the name of the
	 * course.
	 *
	 * @param name The name of the course.
	 */

	public void setName (String name)
	{
		this.name = name;
	}

	/**
	 * Get the semester in which the course was offered.
	 *
	 * @return The semester in which the course was offered.
	 */

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

	/**
	 * Internal method used by the datastore and managers to set the semester in 
	 * which the course was offered.
	 *
	 * @param semester The semester in which the course was offered.
	 */

	protected void setSemester (Semester semester)
	{
		this.semester = semester;
	}

	/**
	 * Get the year in which the course was offered.
	 *
	 * @return The year in which the course was offered.
	 */

	@Override
	public Integer getYear ()
	{
		return this.year;
	}

	/**
	 * Internal method used by the datastore and managers to set the year in 
	 * which the course was offered.
	 *
	 * @param year The year in which the course was offered.
	 */

	protected void setYear (Integer year)
	{
		this.year = year;
	}

	/**
	 * Get a set of all of the activities for the course.
	 *
	 * @return The set of all of the activities for the course.
	 */

	@Override
	public Set<Activity> getActivities ()
	{
		return new HashSet<Activity> (this.activities);
	}

	/**
	 * Internal method used by the datastore and managers to set the initial
	 * set of activities for the course.
	 *
	 * @param activities The Set of activities for the course.
	 */

	protected void setActivities (Set<Activity> activities)
	{
		this.activities = activities;
	}

	/**
	 * Internal method used by the managers to add activities to the course.
	 *
	 * @param activity The activity to add to the course.
	 * @return <code>true</code> if the activity was successfully added to the
	 * course, </code>false</code> otherwise.
	 */

	protected boolean addActivity (Activity activity)
	{
		return this.activities.add (activity);
	}

	/**
	 * Get a set of all of the enrollments for the course.
	 *
	 * @return The set of all of the enrolments for the course.
	 */

	@Override
	public Set<Enrolment> getEnrolments ()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	/**
	 * Internal method used by the datastore and managers to set the initial
	 * set of enrolments for the course.
	 *
	 * @param enrolments The Set of enrolments for the course.
	 */

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	/**
	 * Internal method used by the managers to add an enrolment to the course.
	 *
	 * @param enrolment The enrolment to add to the course.
	 * @return <code>true</code> if the enrolment was successfully added,
	 * <code>false</code> otherwise.
	 */

	protected boolean addEnrolment (Enrolment enrolment)
	{
		return this.enrolments.add (enrolment);
	}

	/**
	 * Override java.lang.Object's toString function to display the name and
	 * time of offering of the course.
	 *
	 * @return A string identifying the course.
	 */

	@Override
	public String toString()
	{
		return new String (this.name + ": " + this.semester + ", " + this.year);
	}
}
