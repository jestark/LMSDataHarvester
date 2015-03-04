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
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityElementFactory;

/**
 * Abstract implementation of the <code>Activity</code> interface.  This class
 * acts as an abstract base class, containing the common <code>Element</code>
 * references, for all of the classes implementing the <code>Activity</code>
 * interface, which are not sub-activities.  It is expected that instances of
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager
 */

public abstract class ActivityInstance extends AbstractActivity implements Serializable
{
	/**
	 * Implementation of the <code>ActivityElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>ActivityInstance</code>.
	 */

	protected static abstract class Factory extends AbstractActivity.Factory implements ActivityElementFactory
	{
		/**
		 * Add the specified <code>Grade</code> to the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> to which the
		 *                  <code>Grade</code> is to be added, not null
		 * @param  grade    The <code>Grade</code> to add to the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>Grade</code> was
		 *                  successfully added to the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		@Override
		public boolean addGrade (final Activity activity, final Grade grade)
		{
			assert activity instanceof ActivityInstance : "activity is not an instance of ActivityInstance";
			assert grade != null : "grade is NULL";
			
			return ((ActivityInstance) activity).addGrade (grade);
		}

		/**
		 * Remove the specified <code>Grade</code> from the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> from which the
		 *                  <code>Grade</code> is to be removed, not null
		 * @param  grade    The <code>Grade</code> to remove from the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>Grade</code> was
		 *                  successfully removed from the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		@Override
		public boolean removeGrade (final Activity activity, final Grade grade)
		{
			assert activity instanceof ActivityInstance : "activity is not an instance of ActivityInstance";
			assert grade != null : "grade is NULL";
			
			return ((ActivityInstance) activity).removeGrade (grade);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Flag indicting if the activity is a "system" activity */
	private Boolean stealth;

	/** The course with which the activity is associated */
	private Course course;

	/** The type of the activity*/
	private ActivityType type;

	/** The set of grades for the activity */
	private Set<Grade> grades;

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public ActivityInstance ()
	{
		super ();
		
		this.type = null;
		this.course = null;

		this.stealth = Boolean.valueOf (false);
		
		this.grades = new HashSet<Grade> ();
	}

	/**
	 * Create a new <code>Activity</code> instance.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  stealth Indicator if the <code>Activity</code> was added by the
	 *                 system, not null
	 */

	public ActivityInstance (final ActivityType type, final Course course, final Boolean stealth)
	{
		this ();

		assert type != null : "type is NULL";
		assert course != null : "course is NULL";
		assert stealth != null : "stealth is NULL";

		this.type = type;
		this.course = course;
		this.stealth = stealth;
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are equal.
	 * The <code>Activity</code> instances are compared based upon the
	 * <code>ActivityType</code> and the associated <code>Course</code>.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivityInstance)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.type, ((ActivityInstance) obj).type);
			ebuilder.append (this.course, ((ActivityInstance) obj).course);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the <code>ActivityType</code> and
	 * the <code>Course</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1039;
		final int mult = 953;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
	
		hbuilder.append (this.type);
		hbuilder.append (this.course);

		return hbuilder.hashCode ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.course;
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Activity</code> instance is loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected void setCourse (final Course course)
	{
		assert course != null : "course is NULL";
		
		this.course = course;
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return this.type;
	}

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a <code>DataStore</code>
	 * when the <code>Activity</code> instance is loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected void setType (final ActivityType type)
	{
		assert type != null : "type is NULL";
		
		this.type = type;
	}

	/**
	 * Get an indication if the <code>Activity</code> was explicitly added to the
	 * <code>Course</code>.  Some <code>Activity</code> instances are added to a
	 * <code>Course</code> by the system, rather than being added by the
	 * instructor.  For these <code>Activity</code> instances, the stealth flag
	 * will be set.
	 *
	 * @return <code>True</code> if the stealth flag is set, <code>False</code>
	 *         otherwise
	 */

	@Override
	public Boolean isStealth ()
	{
		return this.stealth;
	}

	/**
	 * Set the stealth flag for the <code>Activity</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  stealth The stealth flag, not null
	 */

	public void setStealth (final Boolean stealth)
	{
		assert stealth != null : "stealth is NULL";
		
		this.stealth = stealth;
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances are graded.  If the <code>Activity</code> does is not graded
	 * then the <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		return new HashSet<Grade> (this.grades);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected void setGrades (final Set<Grade> grades)
	{
		assert grades != null : "grades is NULL";
		
		this.grades = grades;
	}

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected boolean addGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";
		
		return this.grades.add (grade);
	}

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	protected boolean removeGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";
		
		return this.grades.remove (grade);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Activity</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("type", this.type);
		builder.append ("course", this.course);
		builder.append ("stealth", this.stealth);

		return builder.toString ();
	}
}
