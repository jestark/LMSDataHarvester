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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Semester;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultCourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.CourseElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.CourseFactory;

/**
 * Implementation of the <code>Course</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Course</code>
 * interface, along with the relevant manager and builder.  See the
 * <code>Course</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultCourseBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager
 */

public class CourseData implements Course, Serializable
{
	/**
	 * Implementation of the <code>CourseElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>CourseData</code>
	 */

	private static final class CourseDataFactory implements CourseElementFactory
	{
		/**
		 * Create a new <code>Course</code> instance.
		 *
		 * @param  name     The name of the <code>Course</code>, not null
		 * @param  semester The <code>Semester</code> of offering, not null
		 * @param  year     The year of offering, not null
		 *
		 * @return          The new <code>Course</code> instance
		 */

		@Override
		public Course create (String name, Semester semester, Integer year)
		{
			return new CourseData (name, semester, year);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>course</code>.
		 *
		 * @param  course The <code>Course</code> to which the ID number is assigned,
		 *                not null
		 * @param  id     The ID number assigned to the <code>Course</code>, not null
		 */

		@Override
		public void setId (Course course, Long id)
		{
			((CourseData) course).setId (id);
		}

		/**
		 * Add the specified <code>Activity</code> to the specified <code>Course</code>.
		 *
		 * @param  course    The <code>Course</code> to which the <code>Activity</code>
		 *                   is to be added, not null
		 * @param  enrolment The <code>Enrolment</code> to add to the
		 *                   <code>Course</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Activity</code> was
		 *                   successfully added to the <code>Course</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean addActivity (Course course, Activity activity)
		{
			return ((CourseData) course).addActivity (activity);
		}

		/**
		 * Remove the specified <code>Activity</code> from the specified <code>Course</code>.
		 *
		 * @param  course    The <code>Course</code> from which the <code>Activity</code>
		 *                   is to be removed, not null
		 * @param  enrolment The <code>Enrolment</code> to remove from the
		 *                   <code>Course</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Activity</code> was
		 *                   successfully removed from the <code>Course</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean removeActivity (Course course, Activity activity)
		{
			return ((CourseData) course).removeActivity (activity);
		}

		/**
		 * Add the specified <code>Enrolment</code> to the specified <code>Course</code>.
		 *
		 * @param  course    The <code>Course</code> to which the <code>Enrolment</code>
		 *                   is to be added, not null
		 * @param  enrolment The <code>Enrolment</code> to add to the
		 *                   <code>User</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Enrolment</code> was
		 *                   successfully added to the <code>Course</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean addEnrolment (Course course, Enrolment enrolment)
		{
			return ((CourseData) course).addEnrolment (enrolment);
		}

		/**
		 * Remove the specified <code>Enrolment</code> from the specified
		 * <code>Course</code>.
		 *
		 * @param  course    The <code>Course</code> from which the
		 *                   <code>Enrolment</code> is to be removed, not null
		 * @param  enrolment The <code>Enrolment</code> to remove from the
		 *                   <code>User</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Enrolment</code> was
		 *                   successfully removed from the <code>Course</code>,
		 *                   <code>False</code> otherwise
		 */

		public boolean removeEnrolment (Course course, Enrolment enrolment)
		{
			return ((CourseData) course).removeEnrolment (enrolment);
		}
	}

	private static final long serialVersionUID = 1L;

	/** The primary key of the course. */
	private Long id;

	/** The name of the course. */
	private String name;

	/** The semester in which the course was offered. */
	private Semester semester;

	/** The year in which the course was offered. */
	private Integer year;

	/** The set of Activities which are associated with the course. */
	private Set<Activity> activities;

	/** The set of individuals which are enrolled in the course. */
	private Set<Enrolment> enrolments;

	/**
	 * Static initializer to register the <code>CourseData</code> class with the
	 * factories.
	 */

	static
	{
		(CourseFactory.getInstance ()).registerElement (CourseData.class, DefaultCourseBuilder.class, new CourseDataFactory ());
	}

	/**
	 * Create the <code>Course</code> with null values.
	 */

	public CourseData ()
	{
		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;
		this.activities = null;
		this.enrolments = null;
	}

	/**
	 * Create a new <code>Course</code> instance.
	 *
	 * @param  name     The name of the <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 */

	public CourseData (String name, Semester semester, Integer year)
	{
		this ();
		this.name = name;
		this.semester = semester;
		this.year = year;

		this.activities = new HashSet<Activity> ();
		this.enrolments = new HashSet<Enrolment> ();
	}	

	/**
	 * Compare two <code>Course</code> instances to determine if they are equal.
	 * The <code>Course</code> instances are compared based upon their names, as
	 * well as their year and <code>Semester</code> of offering.
	 *
	 * @param  obj The <code>Course</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Course</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof CourseData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.name, ((CourseData) obj).name);
			ebuilder.append (this.year, ((CourseData) obj).year);
			ebuilder.append (this.semester, ((CourseData) obj).semester);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Course</code> instance.
	 * The hash code is computed based upon the name of the instance as well as
	 * the year and <code>Semester</code> of offering.
	 *
	 * @return An <code>Integer</code> containing the hash code
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
	 * Get the <code>DataStore</code> identifier for the <code>Course</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Course</code> instance is
	 * loaded, or by the <code>CourseBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new <code>Course</code>
	 * instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Course</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Course</code> instance is
	 * loaded.
	 *
	 * @param  name The name of the <code>Course</code>
	 */

	public void setName (String name)
	{
		this.name = name;
	}

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was offered.
	 * This method is intended to be used by a <code>DataStore</code> when the 
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  semester The <code>Semester</code> in which the <code>Course</code>
	 *                  was offered
	 */

	protected void setSemester (Semester semester)
	{
		this.semester = semester;
	}

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	@Override
	public Integer getYear ()
	{
		return this.year;
	}

	/**
	 * Set the year in which the <code>Course</code> was offered.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Course</code> instance is loaded.
	 *
	 * @param  year The year in which the <code>Course</code> was offered
	 */

	protected void setYear (Integer year)
	{
		this.year = year;
	}

	/**
	 * Get the <code>Set</code> of <code>Activity</code> instances which are
	 * associated with the <code>Course</code>.  The <code>Set</code> will be
	 * empty if there are no <code>Activity</code> instances associated with the
	 * <code>Course</code>.
	 *
	 * @return A <code>Set</code> of <code>Activity</code> instances
	 */

	@Override
	public Set<Activity> getActivities ()
	{
		return new HashSet<Activity> (this.activities);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Activity</code> instances
	 * associated with the <code>Course</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Course</code> instance is
	 * loaded.
	 *
	 * @param  activities The <code>Set</code> of <code>Activity</code>
	 *                    instances, not null
	 */

	protected void setActivities (Set<Activity> activities)
	{
		this.activities = activities;
	}

	/**
	 * Add the specified <code>Activity</code> to the <code>Course</code>.
	 *
	 * @param  activity The <code>Activity</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Activity</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected boolean addActivity (Activity activity)
	{
		return this.activities.add (activity);
	}

	/**
	 * Remove the specified <code>Activity</code> from the <code>Course</code>.
	 *
	 * @param  activity The <code>Activity</code> to remove,  not null
	 *
	 * @return          <code>True</code> if the <code>Activity</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeActivity (Activity activity)
	{
		return this.activities.remove (activity);
	}

	/**
	 * Get the <code>Set</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>Set</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	@Override
	public Set<Enrolment> getEnrolments ()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>Course</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Course</code> instance is
	 * loaded.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	/**
	 * Add the specified <code>Enrolment</code> to the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added, <code>False</code> otherwise
	 */

	protected boolean addEnrolment (Enrolment enrolment)
	{
		return this.enrolments.add (enrolment);
	}

	/**
	 * Remove the specified <code>Enrolment</code> from the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeEnrolment (Enrolment enrolment)
	{
		return this.enrolments.remove (enrolment);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Course</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Course</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.name);
		builder.append ("semester", this.semester);
		builder.append ("year", this.year);

		return builder.toString ();
	}
}
