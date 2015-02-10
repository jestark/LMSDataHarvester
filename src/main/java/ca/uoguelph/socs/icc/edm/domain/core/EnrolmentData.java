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
import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;

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

public class EnrolmentData implements Enrolment, Serializable
{
	/**
	 * Implementation of the <code>EnrolmentElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>EnrolmentData</code>.
	 */

	private static final class EnrolmentDataFactory implements EnrolmentElementFactory
	{
		/**
		 * Create a new <code>Enrolment</code> instance.
		 *
		 * @param  user   The <code>User</code> enrolled in the <code>Course</code>,
		 *                not null
		 * @param  course The <code>Course</code> in which the <code>User</code> is
		 *                enrolled, not null
		 * @param  role   The <code>Role</code> of the <code>User</code> in the
		 *                <code>Course</code>, not null
		 * @param  grade  The final grade assigned to the <code>User</code> in the
		 *                <code>Course</code>
		 * @param  usable Indication if the <code>User</code> has consented to their
		 *                data being used for research
		 *
		 * @return        The new <code>Enrolment</code> instance
		 */

		@Override
		public Enrolment create (User user, Course course, Role role, Integer grade, Boolean usable)
		{
			return new EnrolmentData (course, role, grade, usable);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to which the ID number is
		 *                   assigned, not null
		 * @param  id        The ID number assigned to the <code>Enrolment</code>,
		 *                   not null
		 */

		@Override
		public void setId (Enrolment enrolment, Long id)
		{
			((EnrolmentData) enrolment).setId (id);
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
		public boolean addGrade (Enrolment enrolment, Grade grade)
		{
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
		 *                   successfully removed from the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		@Override
		public boolean removeGrade (Enrolment enrolment, Grade grade)
		{
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
		public boolean addLogEntry (Enrolment enrolment, LogEntry entry)
		{
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
		 *                   successfully removed from the <code>Enrolment</code>,
		 *                   <code>False</code> otherwise
		 */

		@Override
		public boolean removeLogEntry (Enrolment enrolment, LogEntry entry)
		{
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
	 * Static initializer to register the <code>EnrolmentData</code> class with the
	 * factories.
	 */

	static
	{
		(EnrolmentFactory.getInstance ()).registerElement (EnrolmentData.class, DefaultEnrolmentBuilder.class, new EnrolmentDataFactory ());
	}

	/**
	 * Create the <code>Enrolment</code> with null values.
	 */

	public EnrolmentData ()
	{
		this.id = null;
		this.log = null;
		this.role = null;
		this.course = null;
		this.usable = new Boolean (false);
		this.finalgrade = null;
		this.grades = null;
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
	 *
	 * @return        The new <code>Enrolment</code> instance
	 */

	public EnrolmentData (Course course, Role role, Integer grade, Boolean usable)
	{
		this ();

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
	 * @return     <code>True</code> if the two <code>Enrolment</code> instances
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
	 * The hash code is computed based upon the associated <code>Course</code> and
	 * the associated <code>Role</code>.
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
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Enrolment</code> instance
	 * is loaded, or by the <code>EnrolmentBuilder</code> implementation to set
	 * the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Enrolment</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (Long id)
	{
		this.id = id;
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented by
	 * the <code>Enrolment</code> instance is enrolled.
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

	protected void setCourse (Course course)
	{
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
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is loaded.
	 *
	 * @param  role The <code>Role</code>, not null
	 */

	protected void setRole (Role role)
	{
		this.role = role;
	}

	/**
	 * Get the name associated with the <code>Enrolment</code>.  This method
	 * returns a <code>String</code> representation of the <code>DataStore</code>
	 * identifier as the name of the <code>Enrolment</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    Enrolment#getName
	 */

	@Override
	public String getName ()
	{
		String result = new String ("(unset)");
		Long id = this.getId ();

		if (id != null)
		{
			result = id.toString ();
		}

		return result;
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
	public Grade getGrade (Activity activity)
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
	 * associated with the <code>Enrolment</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected void setGrades (Set<Grade> grades)
	{
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

	protected boolean addGrade (Grade grade)
	{
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

	protected boolean removeGrade (Grade grade)
	{
		return this.grades.remove (grade);
	}

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The final
	 * grade will be null if no final grade was assigned for the <code>User</code>
	 * associated with this <code>Enrolment</code>.
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
	 * Set the final grade for the <code>User</code> in the <code>Course</code>.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>Enrolment</code> instance is loaded.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the course,
	 *                    on the interval [0, 100]
	 */

	protected void setFinalGrade (Integer finalgrade)
	{
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
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	protected void setUsable (Boolean usable)
	{
		this.usable =usable;
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
	 * associated with the <code>Enrolment</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances, not
	 *             null
	 */

	protected void setLog (List<LogEntry> log)
	{
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

	protected boolean addLog (LogEntry entry)
	{
		return this.log.add (entry);
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the <code>Enrolment</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>LogEntry</code> was
	 *               successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeLog (LogEntry entry)
	{
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
		builder.append ("course", this.course);
		builder.append ("role", this.role);

		return builder.toString ();
	}
}
