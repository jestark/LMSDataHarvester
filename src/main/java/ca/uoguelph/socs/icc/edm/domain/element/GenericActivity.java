/* Copyright (C) 2015 James E. Stark
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

import java.io.Serializable;

import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>Activity</code> interface, for
 * <code>Activity</code> instances which do not contain additional data.  It is
 * expected that instances of this class will be accessed though the
 * <code>Activity</code> interface, along with the relevant manager, and
 * builder.  See the <code>Activity</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */


public class GenericActivity extends ActivityInstance implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>${ClassName}</code> */
	private Long id;

	/** The type of the <code>Activity</code> */
	private ActivityType type;

	/** The associated <code>Course</code> */
	private Course course;

	/** The associated <code>LogEntry</code> instances */
	private List<LogEntry> log;

	/**
	 * Static initializer to register the <code>GenericActivity</code> class
	 * with the factories.
	 */

	static
	{
		Profile.registerCreator (Implementation.getInstance (ActivityInstance.metadata, GenericActivity.class, GenericActivity::new));
	}

	/**
	 * Create the <code>GenericActivity</code> with null values.
	 */

	protected GenericActivity ()
	{
		this.id = null;
		this.type = null;
		this.course = null;

		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
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
		else if (obj instanceof GenericActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.append (this.type, ((GenericActivity) obj).getType ());
			ebuilder.append (this.course, ((GenericActivity) obj).getCourse ());

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
		hbuilder.append (this.getType ());
		hbuilder.append (this.getCourse ());

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
		return this.id;
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
		this.id = id;
	}

	/**
	 * Get the name of the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances have names.  For those
	 * <code>Activity</code> instances which do not have names, the name of the
	 * associated <code>ActivityType</code> will be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return (this.type != null) ? this.type.getName () : null;
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
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
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
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	@Override
	protected void setType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		this.type = type;
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
		return new HashSet<Grade> ();
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
		return new ArrayList<LogEntry> (this.log);
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
	 * associated with the <code>Actvity</code>.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		return new ArrayList<SubActivity> ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>Activity</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("type", this.type);
		builder.append ("course", this.course);

		return builder.toString ();
	}
}
