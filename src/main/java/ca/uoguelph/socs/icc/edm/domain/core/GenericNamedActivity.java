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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

/**
 * Generic representation of an <code>Activity</code> which has a name.  This
 * class acts as an abstract base class for all of the <code>Activity</code>
 * implementations which have a name.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class GenericNamedActivity extends AbstractNamedActivity implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The activity instance */
	private ActivityInstance instance;

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public GenericNamedActivity ()
	{
		super ();
		this.instance = null;
	}

	/**
	 * Create the <code>Activity</code>.
	 *
	 * @param  name The name of the <code>Activity</code>
	 */

	public GenericNamedActivity (String name)
	{
		super (name);

		this.instance = null;
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.  The <code>Activity</code> instances are compared based upon their
	 * names.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
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
		else if (obj instanceof GenericNamedActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.instance.getCourse (), ((Activity) obj).getCourse ());
			ebuilder.append (this.instance.getType (), ((Activity) obj).getType ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return this.instance.hashCode ();
	}
	
	/**
	 * Compute the <code>hashCode</code> for the <code>GenericNamedActivity</code>
	 * instance.  This method is intended to by used by the parent
	 * <code>Activity</code> instance to get a <code>hashCode</code> for the
	 * referenced <code>Activity</code> data.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	protected int actHashCode ()
	{
		final int base = 1019;
		final int mult = 983;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
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
		return this.instance.getCourse ();
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return this.instance.getType ();
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
		return this.instance.isStealth ();
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
		return this.instance.getGrades ();
	}

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade The <code>Grade</code> to add, not null
	 *
	 * @return       <code>True</code> if the <code>Grade</code> was
	 *               successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addGrade (Grade grade)
	{
		return this.instance.addGrade (grade);
	}

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade The <code>Grade</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>Grade</code> was
	 *               successfully removed from, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeGrade (Grade grade)
	{
		return this.instance.removeGrade (grade);
	}

	/**
	 * Get an <code>Activity</code> instance which directly contains the core
	 * data.  The core data for an <code>Activity</code> includes the associated
	 * <code>Course</code> and <code>ActivityType</code>.
	 *
	 * @return An <code>Activity</code> containing the core data
	 */

	public Activity getInstance ()
	{
		return this.instance;
	}

	/**
	 * Set the link to that <code>Activity</code> instance that contains the core
	 * <code>Activity</code> data.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is loaded.
	 *
	 * @param  instance The <code>Activity</code> instance containing the core
	 *                  data
	 */

	protected void setInstance (ActivityInstance instance)
	{
		this.instance = instance;
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances which
	 * act upon the <code>Activity</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	@Override
	public List<LogEntry> getLog ()
	{
		return this.instance.getLog ();
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
	protected boolean addLog (LogEntry entry)
	{
		return this.instance.addLog (entry);
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
	protected boolean removeLog (LogEntry entry)
	{
		return this.instance.removeLog (entry);
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

		builder.append ("type", this.instance.getType ());
		builder.append ("course", this.instance.getCourse ());
		builder.append ("stealth", this.instance.isStealth ());
		builder.appendSuper (super.toString ());

		return builder.toString ();
	}
}
