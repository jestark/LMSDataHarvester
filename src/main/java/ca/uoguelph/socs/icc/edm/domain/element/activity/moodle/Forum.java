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

package ca.uoguelph.socs.icc.edm.domain.element.activity.moodle;

import java.io.Serializable;

import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.NamedActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>NamedActivity</code> template,
 * with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = forum
 * <li>ClassName      = Forum
 * <li>HashBase       = 2017
 * <li>HashMult       = 677
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.3
 */

public class Forum extends NamedActivity implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The name of the <code>Activity</code> */
	private String name;

	/** The associated <code>Grade</code> instances */
	private Set<Grade> grades;

	/** The associated <code>LogEntry</code> instances */
	private List<LogEntry> log;

	/** The associated <code>SubActivity</code> instances*/
	private List<SubActivity> subActivities;

	/**
	 * Register the <code>Forum</code> with the factories on
	 * initialization.
	 */

	static
	{
		Implementation.getInstance (NamedActivity.class, Forum.class, Forum::new);
		Activity.registerImplementation ("moodle", "forum", Forum.class);
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected Forum ()
	{
		this.id = null;
		this.name = null;
		this.type = null;
		this.course = null;

		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.  The <code>Activity</code> instances are compared based upon
	 * their <code>ActivityType</code>, the associated <code>Course</code> and
	 * their names.
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
		else if (obj instanceof Forum)
		{
			result = super.equals (obj);
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the <code>ActivityType</code>, the
	 * <code>Course</code> and the name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2017;
		final int mult = 677;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return super.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded, or by the <code>ActivityBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>Activity</code> instance.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		super.setId (id);
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
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
		return super.getCourse ();
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	@Override
	protected void setCourse (final Course course)
	{
		super.setCourse (course);
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return super.getType ();
	}

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	@Override
	protected void setType (final ActivityType type)
	{
		super.setType (type);
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		this.grades.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableSet (this.grades);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	@Override
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

	@Override
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

	@Override
	protected boolean removeGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		return this.grades.remove (grade);
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>Activity</code>.
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
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
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
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.add (entry);
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		return this.log.remove (entry);
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Activity</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		this.subActivities.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.subActivities);
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @param  subActivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subActivities)
	{
		assert subActivities != null : "subActivities is NULL";

		this.subActivities = subActivities;
	}

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.add (subActivity);
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.remove (subActivity);
	}
}
