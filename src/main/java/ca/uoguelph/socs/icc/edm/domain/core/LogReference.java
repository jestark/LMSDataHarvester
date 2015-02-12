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

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

/**
 * An abstract representation of the relationship between a
 * <code>LogEntry</code> and a sub-activity.  This class acts as the abstract
 * base class for all of the logging related to sub-activities.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class LogReference implements LogEntry, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated log entry */
	private LogEntry entry;

	/** The associated sub-activity */
	private ActivityGroupMember activity;

	/**
	 * Create the <code>LogEntry</code> with null values.
	 */

	public LogReference ()
	{
		this.entry = null;
		this.activity = null;
	}

	/**
	 * Create the <code>LogReference</code> specifying all of its values.  
	 *
	 * @param  entry    The parent <code>LogEntry</code> object
	 * @param  activity The Sub-activity
	 */

	public LogReference (LogEntry entry, ActivityGroupMember activity)
	{
		this ();

		this.entry = entry;
		this.activity = activity;
	}

	/**
	 * Compare two <code>LogEntry</code> instances to determine if they are equal.
	 * The <code>LogEntry</code> instances are compared based upon the contents of
	 * the parent <code>LogEntry</code> as determined by its <code>equals</code>
	 * method and the referenced sub-activity.
	 *
	 * @param  obj The <code>LogEntry</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>LogEntry</code> instances
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
		else if (obj instanceof LogReference)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.append (this.entry.getAction (), ((LogReference) obj).getAction ());
			ebuilder.append (this.entry.getActivity (), ((LogReference) obj).getActivity ());
			ebuilder.append (this.entry.getEnrolment (), ((LogReference) obj).getEnrolment ());
			ebuilder.append (this.entry.getIPAddress (), ((LogReference) obj).getIPAddress ());
			ebuilder.append (this.entry.getTime (), ((LogReference) obj).getTime ());
			ebuilder.append (this.activity, ((LogReference) obj).activity);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogEntry</code> instance.
	 * The hash code is computed based upon the contents of the parent
	 * <code>LogEntry</code> as returned by its <code>hashCode</code> method and
	 * the referenced sub-activity.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1109;
		final int mult = 877;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);

		hbuilder.append (this.entry.getAction ());
		hbuilder.append (this.entry.getActivity ());
		hbuilder.append (this.entry.getEnrolment ());
		hbuilder.append (this.entry.getIPAddress ());
		hbuilder.append (this.entry.getTime ());
		hbuilder.append (this.activity);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the parent <code>LogEntry</code> instance.
	 *
	 * @return The parent <code>LogEntry</code> instance
	 */

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	/**
	 * Set the reference to the parent <code>LogEntry</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>LogEntry</code> instance is loaded.
	 *
	 * @param  entry The parent <code>LogEntry</code> instance, not null
	 */

	protected void setEntry (LogEntry entry)
	{
		this.entry = entry;
	}

	/**
	 * Get the Sub-Activity upon which the logged action was performed.
	 *
	 * @return A reference to the associated <code>Activity</code> instance
	 */

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * Set the Sub-Activity upon which the logged action was performed.  This
	 * method is intended to be used by a <code>DataStore</code> when the
	 * <code>LogEntry</code> instance is loaded.
	 *
	 * @param  activity The sub-activity, not null
	 */

	protected void setActivity (ActivityGroupMember activity)
	{
		this.activity = activity;
	}

	/**
	 * Get the <code>Enrolment</code> object for the user which performed the
	 * logged activity.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	@Override
	public Enrolment getEnrolment()
	{
		return this.entry.getEnrolment ();
	}

	/**
	 * Get the <code>Course</code> for which the activity was logged.
	 *
	 * @return A reference to the associated <code>Course</code> 
	 */

	@Override
	public Course getCourse()
	{
		return this.entry.getCourse ();
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	@Override
	public Action getAction()
	{
		return this.entry.getAction ();
	}

	/**
	 * Get the time of the logged activity.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	@Override
	public Date getTime()
	{
		return this.entry.getTime ();
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * activity.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	@Override
	public String getIPAddress ()
	{
		return this.entry.getIPAddress ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>LogEntry</code>
	 *         instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.entry.getEnrolment ());
		builder.append ("action", this.entry.getAction ());
		builder.append ("activity", this.entry.getActivity ());
		builder.append ("time", this.entry.getTime ());
		builder.append ("ipaddress", this.entry.getIPAddress ());	
		builder.append ("subactivity", this.entry.getActivity ());

		return builder.toString ();
	}
}
