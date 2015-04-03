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
import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.EnrolmentElementFactory;

/**
 * Implementation of the <code>Enrolment</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Enrolment</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Enrolment</code> interface documentation for details.
 * <p>
 * This class implements the <code>Enrolment</code> interface without any
 * user-identifying data.  See the <code>UserEnrolmentData</code> for a
 * user-identifying implementation of the <code>Enrolment</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager
 * @see     UserEnrolmentData
 */

public class EnrolmentData extends AbstractElement implements Enrolment, Serializable
{
	/**
	 * Implementation of the <code>EnrolmentElementFactory</code> interface.
	 * Allows the builders to create instances of <code>EnrolmentData</code>.
	 */

	private static final class Factory extends AbstractElement.Factory<Enrolment> implements EnrolmentElementFactory
	{
		/**
		 * Create a new <code>Enrolment</code> instance.
		 *
		 * @param  user   The <code>User</code> enrolled in the
		 *                <code>Course</code>, not null
		 * @param  course The <code>Course</code> in which the
		 *                <code>User</code> is enrolled, not null
		 * @param  role   The <code>Role</code> of the <code>User</code> in the
		 *                <code>Course</code>, not null
		 * @param  grade  The final grade assigned to the <code>User</code> in
		 *                the <code>Course</code>
		 * @param  usable Indication if the <code>User</code> has consented to
		 *                their data being used for research
		 *
		 * @return        The new <code>Enrolment</code> instance
		 */

		@Override
		public Enrolment create (final User user, final Course course, final Role role, final Integer grade, final Boolean usable)
		{
			assert user != null : "user is NULL";
			assert course != null : "course is NULL";
			assert role != null : "role is NULL";
			assert usable != null : "usable is NULL";
			assert ((grade == null) || ((grade >= 0) && (grade <= 100))) : "grade out of range, must be between 0 and 100";

			return new EnrolmentData (course, role, grade, usable);
		}

		/**
		 * Set the usable flag for the specified <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> for which the usable
		 *                   flag is to be set, not null
		 * @param  usable    Indication if the <code>User</code> has given
		 *                   consent for their data to be used for research,
		 *                   not null
		 */

		public void setUsable (final Enrolment enrolment, final Boolean usable)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert usable != null : "usable is NULL";

			((EnrolmentData) enrolment).setUsable (usable);
		}

		/**
		 * Set the final grade for the specified <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> for which the final
		 *                   grade is to be set
		 * @param  grade The final grade assigned to the <code>User</code>
		 */

		public void setFinalGrade (final Enrolment enrolment, final Integer grade)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert ((grade == null) || ((grade >= 0) && (grade <= 100))) : "grade out of range, must be between 0 and 100";

			((EnrolmentData) enrolment).setFinalGrade (grade);
		}

		/**
		 * Add the specified <code>Grade</code> to the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to which the
		 *                   <code>Grade</code> is to be added, not null
		 * @param  grade     The <code>Grade</code> to add to the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Grade</code> was
		 *                   successfully added to the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		@Override
		public boolean addGrade (final Enrolment enrolment, final Grade grade)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert grade != null : "grade is NULL";

			return ((EnrolmentData) enrolment).addGrade (grade);
		}

		/**
		 * Remove the specified <code>Grade</code> from the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> from which the
		 *                   <code>Grade</code> is to be removed, not null
		 * @param  grade     The <code>Grade</code> to remove from the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>Grade</code> was
		 *                   successfully removed from the
		 *                   <code>Enrolment</code>, <code>False</code>
		 *                   otherwise
		 */

		@Override
		public boolean removeGrade (final Enrolment enrolment, final Grade grade)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert grade != null : "grade is NULL";

			return ((EnrolmentData) enrolment).removeGrade (grade);
		}

		/**
		 * Add the specified <code>LogEntry</code> to the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to which the
		 *                   <code>LogEntry</code> is to be added, not null
		 * @param  entry     The <code>LogEntry</code> to add to the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>LogEntry</code> was
		 *                   successfully added to the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		@Override
		public boolean addLogEntry (final Enrolment enrolment, final LogEntry entry)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert entry != null : "entry is NULL";

			return ((EnrolmentData) enrolment).addLog (entry);
		}

		/**
		 * Remove the specified <code>LogEntry</code> from the specified
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> from which the
		 *                   <code>LogEntry</code> is to be removed, not null
		 * @param  entry     The <code>LogEntry</code> to remove from the
		 *                   <code>Enrolment</code>, not null
		 *
		 * @return           <code>True</code> if the <code>LogEntry</code> was
		 *                   successfully removed from the
		 *                   <code>Enrolment</code>, <code>False</code>
		 *                   otherwise
		 */

		@Override
		public boolean removeLogEntry (final Enrolment enrolment, final LogEntry entry)
		{
			assert enrolment instanceof EnrolmentData : "enrolment is not an instance of EnrolmentData";
			assert entry != null : "entry is NULL";

			return ((EnrolmentData) enrolment).removeLog (entry);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the enrolment */
	private Long id;

	/** The course associated with the enrolment */
	private Course course;

	/** The user's role in the associated course */
	private Role role;

	/** The user's final grade in the associated course */
	protected Integer finalgrade;

	/** Flag indicating if the enrolment can be used for research */
	protected Boolean usable;

	/** The set of grades associated with the enrolment */
	protected Set<Grade> grades;

	/** The log entries generated by this enrolment */
	protected List<LogEntry> log;

	/**
	 * Static initializer to register the <code>EnrolmentData</code> class with
	 * the factories.
	 */

	static
	{
		AbstractElement.registerElement (Enrolment.class, EnrolmentData.class, DefaultEnrolmentBuilder.class, EnrolmentElementFactory.class, new Factory ());
	}

	/**
	 * Create the <code>Enrolment</code> with null values.
	 */

	public EnrolmentData ()
	{
		this.id = null;
		this.role = null;
		this.course = null;
		this.finalgrade = null;

		this.usable = Boolean.valueOf (false);

		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Create a new <code>Enrolment</code> instance.
	 *
	 * @param  course The <code>Course</code> in which the <code>User</code> is
	 *                enrolled, not null
	 * @param  role   The <code>Role</code> of the <code>User</code> in the
	 *                <code>Course</code>, not null
	 * @param  grade  The final grade assigned to the <code>User</code> in the
	 *                <code>Course</code>
	 * @param  usable Indication if the <code>User</code> has consented to their
	 *                data being used for research
	 */

	public EnrolmentData (final Course course, final Role role, final Integer grade, final Boolean usable)
	{
		assert course != null : "course is NULL";
		assert role != null : "role is NULL";
		assert usable != null : "usable is NULL";

		assert ((grade == null) || (grade >= 0)) : "Grade can not be negative";
		assert ((grade == null) || (grade <= 100)) : "Grade can not be greater than 100%";

		this.id = null;
		this.course = course;
		this.role = role;
		this.finalgrade = grade;
		this.usable = usable;

		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Compare two <code>Enrolment</code> instances to determine if they are
	 * equal.  The <code>Enrolment</code> instances are compared based upon the
	 * <code>Course</code> and the <code>Role</code>.
	 *
	 * @param  obj The <code>Enrolment</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Enrolment</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof EnrolmentData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.course, ((EnrolmentData) obj).course);
			ebuilder.append (this.role, ((EnrolmentData) obj).role);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Enrolment</code> instance.
	 * The hash code is computed based upon the associated <code>Course</code>
	 * and the associated <code>Role</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1091;
		final int mult = 907;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.course);
		hbuilder.append (this.role);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Enrolment</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Enrolment</code>
	 * instance is loaded, or by the <code>EnrolmentBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>Enrolment</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse()
	{
		return this.course;
	}

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected void setCourse (final Course course)
	{
		assert course != null : "course is NULL";

		this.course = course;
	}

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	@Override
	public Role getRole()
	{
		return this.role;
	}

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  role The <code>Role</code>, not null
	 */

	protected void setRole (final Role role)
	{
		assert role != null : "role is NULL";

		this.role = role;
	}

	/**
	 * Get the name associated with the <code>Enrolment</code>.  This method
	 * returns a <code>String</code> representation of the
	 * <code>DataStore</code> identifier as the name of the
	 * <code>Enrolment</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    Enrolment#getName
	 */

	@Override
	public String getName ()
	{
		return (this.id != null) ? this.id.toString () : "(unset)";
	}

	/**
	 * Get the <code>Grade</code> for the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> for which the grade is to be
	 *                  retrieved
	 * @return          The <code>Grade</code> for the specified
	 *                  <code>Activity</code>
	 */

	@Override
	public Grade getGrade (final Activity activity)
	{
		Grade result = null;

		for (Grade i : this.grades)
		{
			if (activity == i.getActivity ())
			{
				result = i;
				break;
			}
		}

		return result;
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Enrolment</code> instance.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances.
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		return new HashSet<Grade> (this.grades);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
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
	 * Add the specified <code>Grade</code> to the <code>Enrolment</code>.
	 *
	 * @param  grade  The <code>Grade</code> to add, not null
	 *
	 * @return        <code>True</code> if the <code>Grade</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	protected boolean addGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		return this.grades.add (grade);
	}

	/**
	 * Remove the specified <code>Grade</code> from the <code>Enrolment</code>.
	 *
	 * @param  grade The <code>Grade</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>Grade</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		return this.grades.remove (grade);
	}

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	@Override
	public Integer getFinalGrade ()
	{
		return this.finalgrade;
	}

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 */

	protected void setFinalGrade (final Integer finalgrade)
	{
		assert ((finalgrade == null) || (finalgrade >= 0)) : "Grade can not be negative";
		assert ((finalgrade == null) || (finalgrade <= 100)) : "Grade can not be greater than 100%";

		this.finalgrade = finalgrade;
	}

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	@Override
	public Boolean isUsable ()
	{
		return this.usable;
	}

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	protected void setUsable (final Boolean usable)
	{
		assert usable != null : "usable is NULL";

		this.usable = usable;
	}

	/**
	 * Get the <code>List</code> of <code>LogEntry</code> instances associated
	 * with this <code>Enrolment</code>.  The <code>List</code> will be empty
	 * if there are no associated <code>LogEntry</code> instances.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	@Override
	public List<LogEntry> getLog ()
	{
		return this.log;
	}

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	protected void setLog (final List<LogEntry> log)
	{
		assert log != null : "log is NULL";

		this.log = log;
	}

	/**
	 * Add the specified <code>LogEntry</code> to the <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to add, not null
	 *
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully added, <code>False</code> otherwise
	 */

	protected boolean addLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.add (entry);
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the
	 * <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.remove (entry);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Enrolment</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Enrolment</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("id", this.id);
		builder.append ("usable", this.usable);
		builder.append ("finalgrade", this.finalgrade);
		builder.append ("course", this.course);
		builder.append ("role", this.role);

		return builder.toString ();
	}
}
