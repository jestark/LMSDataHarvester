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
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.definition.DefinitionBuilder;

/**
 * Implementation of the <code>LogEntry</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>LogEntry</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>LogEntry</code> interface documentation for details.
 * <p>
 * Note that there is a co-dependency between this class and
 * <code>LogReference</code>.  See <code>LogReference</code>
 * for the details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder
 */

public class LogData extends AbstractElement implements LogEntry, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the log entry */
	private Long id;

	/** The enrolment which generated the log entry */
	private Enrolment enrolment;

	/** The activity which is associated with the log entry */
	private Activity activity;

	/** The logged action, which was performed on the associated activity */
	private Action action;

	/** The time at which the action was performed */
	private Date time;

	/** The originating IP Address for the logged action */
	private String ip;

	/** The <code>SubActivity</code> which is associated with the log entry */
	private LogReference reference;

	/**
	 * Static initializer to register the <code>LogData</code> class with the
	 * factories.
	 */

	static
	{
		DefinitionBuilder<LogEntry, LogData> builder = DefinitionBuilder.newInstance (LogEntry.class, LogData.class);
		builder.setCreateMethod (LogData::new);

		builder.addUniqueAttribute ("id", Long.class, false, false, LogData::getId, LogData::setId);

		builder.addAttribute ("action", Action.class, true, false, LogData::getAction, LogData::setAction);
		builder.addAttribute ("activity", Activity.class, true, false, LogData::getActivity, LogData::setActivity);
		builder.addAttribute ("enrolment", Enrolment.class, true, false, LogData::getEnrolment, LogData::setEnrolment);
		builder.addAttribute ("reference", LogReference.class, false, false, LogData::getReference, LogData::setReference);
		builder.addAttribute ("ip", String.class, true, false, LogData::getIPAddress, LogData::setIPAddress);
		builder.addAttribute ("time", Date.class, true, true, LogData::getTime, LogData::setTime);

		AbstractElement.registerElement (builder.build (), DefaultLogEntryBuilder.class);
	}

	/**
	 * Create the <code>LogEntry</code> with null values.
	 */

	public LogData ()
	{
		this.id = null;
		this.ip = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.reference = null;
		this.time = new Date ();
	}

	/**
	 * Create a new <code>LogEntry</code> instance.  Defaults to the current
	 * system time, if the specified time is null.
	 *
	 * @param  action    The <code>Action</code>, not null
	 * @param  activity  The <code>Activity</code> upon which the
	 *                   <code>Action</code> was performed, not null
	 * @param  enrolment The <code>Enrolment</code> which performed the
	 *                   <code>Action</code>, not null
	 * @param  ip        The remote IP Address
	 * @param  time      The time that the <code>Action</code> was performed
	 */

	public LogData (final Action action, final Activity activity, final Enrolment enrolment, final String ip, final Date time)
	{
		assert action != null : "action is NULL";
		assert activity != null : "activity is NULL";
		assert enrolment != null : "enrolment is NULL";

		this.id = null;
		this.reference = null;

		this.action = action;
		this.activity = activity;
		this.enrolment = enrolment;
		this.ip = ip;

		this.time = (time != null) ? new Date (time.getTime ()) : new Date ();
	}

	/**
	 * Compare two <code>LogEntry</code> instances and determine if they are
	 * equal.
	 * <p>
	 * <ul>
	 * <li>The <code>Action</code>
	 * <li>The <code>Activity</code>
	 * <li>The <code>Enrolment</code>
	 * <li>The logged IP Address
	 * <li>The time
	 * <ul>
	 *
	 * @param  obj The <code>LogEntry</code> instance to compare with this one
	 *
	 * @return     <code>True</code> if the <code>LogEntry</code> instances
	 *             should be considered to be equal, <code>False</code>
	 *             otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof LogData)
		{
			if (this.reference != null)
			{
				result = this.reference.equals (((LogData) obj).reference);
			}
			else
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.append (this.action, ((LogData) obj).action);
				ebuilder.append (this.activity, ((LogData) obj).activity);
				ebuilder.append (this.enrolment, ((LogData) obj).enrolment);
				ebuilder.append (this.ip, ((LogData) obj).ip);
				ebuilder.append (this.time, ((LogData) obj).time);
				ebuilder.append (this.reference, ((LogData) obj).reference);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogEntry</code> instance.
	 * The hash code is computed based upon the following fields:
	 * <p>
	 * <ul>
	 * <li>The <code>Action</code>
	 * <li>The <code>Activity</code>
	 * <li>The <code>Enrolment</code>
	 * <li>The logged IP Address
	 * <li>The time
	 * <ul>
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1093;
		final int mult = 887;

		int result = 0;

		if (this.reference != null)
		{
			result = this.reference.hashCode ();
		}
		else
		{
			HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
			hbuilder.append (this.action);
			hbuilder.append (this.activity);
			hbuilder.append (this.enrolment);
			hbuilder.append (this.ip);
			hbuilder.append (this.time);
			hbuilder.append (this.reference);

			result = hbuilder.toHashCode ();
		}

		return result;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
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
	 * be used by a <code>DataStore</code> when the <code>LogEntry</code>
	 * instance is loaded, or by the <code>LogEntryBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>LogEntry</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	@Override
	public Action getAction ()
	{
		return this.action;
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  action The <code>Action</code>, not null
	 */

	protected void setAction (final Action action)
	{
		assert action != null : "action is NULL";

		this.action = action;
	}

	/**
	 * Get the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @return A reference to the associated <code>Activity</code> object.
	 */

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * Set the <code>Activity</code>  upon which the logged action was
	 * performed.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected void setActivity (final Activity activity)
	{
		assert activity != null : "activity is NULL";

		this.activity = activity;
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	@Override
	public SubActivity getSubActivity ()
	{
		return (this.reference != null) ? this.reference.getSubActivity () : null;
	}

	/**
	 * Get the <code>Course</code> for which the action was logged.
	 *
	 * @return A reference to the associated <code>Course</code>
	 */

	@Override
	public Course getCourse ()
	{
		return this.activity.getCourse ();
	}

	/**
	 * Get the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.   This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	protected void setEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		this.enrolment = enrolment;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * action.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	@Override
	public String getIPAddress ()
	{
		return this.ip;
	}

	/**
	 * Set the Internet Protocol Address which is associated with the logged
	 * <code>Action</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  ip A <code>String</code> containing the IP Address
	 */

	protected void setIPAddress (final String ip)
	{
		this.ip = ip;
	}

	/**
	 * Get the reference to the <code>SubActivity</code> for the
	 * <code>LogEntry</code>.  Some <code>LogEntry</code> instances will record
	 * an <code>Action</code> upon a <code>SubActivity</code>, rather than the
	 * <code>Activity</code> itself.  This method gets the
	 * <code>LogReference</code> instance containing the
	 * </code>SubActivity</code>.
	 *
	 * @return The <code>LogReference</code> instance
	 */

	public LogReference getReference ()
	{
		return this.reference;
	}

	/**
	 * Set the reference to the <code>SubActivity</code> to the
	 * <code>LogEntry</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded, or by the <code>LogEntryBuilder</code> when the
	 * <code>LogEntry</code> is created.
	 *
	 * @param  reference The <code>LogReference</code> instance, not null
	 */

	protected void setReference (final LogReference reference)
	{
		assert reference != null : "reference is NULL";

		this.reference = reference;
	}

	/**
	 * Get the time of the logged <code>Action</code>.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	@Override
	public Date getTime ()
	{
		return (Date) this.time.clone ();
	}

	/**
	 * Set the time of the logged <code>Action</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>LogEntry</code>
	 * instance is loaded.
	 *
	 * @param  time The time, not null
	 */

	protected void setTime (final Date time)
	{
		assert time != null : "time is NULL";

		this.time = time;
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogEntry</code> instance
	 */

	@Override
	public String toString ()
	{
		String result = null;

		if (this.reference != null)
		{
			result = this.reference.toString ();
		}
		else
		{
			ToStringBuilder builder = new ToStringBuilder (this);

			builder.append ("enrolment", this.enrolment);
			builder.append ("action", this.action);
			builder.append ("activity", this.activity);
			builder.append ("time", this.time);
			builder.append ("ipaddress", this.ip);
			builder.append ("subactivity", this.reference);

			result = builder.toString ();
		}

		return result;
	}
}
