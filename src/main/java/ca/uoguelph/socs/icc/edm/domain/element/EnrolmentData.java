/* Copyright (C) 2014, 2015, 2016 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;

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
 */

public class EnrolmentData extends Enrolment
{
	/**
	 * Representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (Element.Definition.class)
	public static final class Definition extends Enrolment.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (EnrolmentData.class, EnrolmentData::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the enrolment */
	private @Nullable Long id;

	/** The course associated with the enrolment */
	private Course course;

	/** The user's role in the associated course */
	private Role role;

	/** The user's final grade in the associated course */
	protected @Nullable Integer finalGrade;

	/** Flag indicating if the enrolment can be used for research */
	protected Boolean usable;

	/** The set of grades associated with the enrolment */
	protected List<Grade> grades;

	/** The log entries generated by this enrolment */
	protected List<LogEntry> log;

	/**
	 * Create the <code>Enrolment</code> with null values.
	 */

	protected EnrolmentData ()
	{
		this.id = null;
		this.role = null;
		this.course = null;
		this.finalGrade = null;

		this.usable = Boolean.valueOf (false);

		this.grades = new ArrayList<Grade> ();
		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Create an <code>Enrolment</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	private EnrolmentData (final Enrolment.Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.course = Preconditions.checkNotNull (builder.getCourse (), "course");
		this.role = Preconditions.checkNotNull (builder.getRole (), "role");
		this.usable = Preconditions.checkNotNull (builder.isUsable (), "usable");
		this.finalGrade = builder.getFinalGrade ();

		this.grades = new ArrayList<Grade> ();
		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Enrolment</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used initialize the <code>DataStore</code> identifier on a new
	 * <code>Enrolment</code> instance is loaded.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
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
		return this.propagateDomainModel (this.course);
	}

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 * This method is intended to be used to initialize a new
	 * <code>Enrolment</code> instance.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	@Override
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
		return this.propagateDomainModel (this.role);
	}

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used to intiailize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  role The <code>Role</code>, not null
	 */

	@Override
	protected void setRole (final Role role)
	{
		assert role != null : "role is NULL";

		this.role = role;
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
		return this.getGrades ()
			.stream ()
			.filter (x -> x.getActivity () == activity)
			.findFirst ()
			.orElse (null);
	}

	/**
	 * Get the <code>List</code> of <code>Grade</code> instances which are
	 * associated with the <code>Enrolment</code> instance.
	 *
	 * @return A <code>List</code> of <code>Grade</code> instances.
	 */

	@Override
	public List<Grade> getGrades ()
	{
		this.grades.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.grades);
	}

	/**
	 * Initialize the <code>List</code> of <code>Grade</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  grades The <code>List</code> of <code>Grade</code> instances, not
	 *                null
	 */

	@Override
	protected void setGrades (final List<Grade> grades)
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

	@Override
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

	@Override
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
	@CheckReturnValue
	public Integer getFinalGrade ()
	{
		return this.finalGrade;
	}

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.  This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  finalGrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 */

	@Override
	protected void setFinalGrade (final @Nullable Integer finalGrade)
	{
		assert ((finalGrade == null) || (finalGrade >= 0)) : "Grade can not be negative";
		assert ((finalGrade == null) || (finalGrade <= 100)) : "Grade can not be greater than 100%";

		this.finalGrade = finalGrade;
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
	 * <code>Course</code>. This method is intended to be used to initialize a
	 * new <code>Enrolment</code> instance.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	@Override
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
		this.log.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.log);
	}

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>Enrolment</code> instance.  This method is
	 * intended to be used to initialize a new <code>Enrolment</code> instance.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances,
	 *             not null
	 */

	@Override
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

	@Override
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

	@Override
	protected boolean removeLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.remove (entry);
	}
}
