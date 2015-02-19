/* Copyright (C) 2014 James E. Stark
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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.LogEntryElementFactory;

/**
 * Implementation of the <code>LogEntry</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>LogEntry</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>LogEntry</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.LogEntryManager
 */

public class LogData extends AbstractElement implements LogEntry, Serializable
{
	private static final class Factory implements LogEntryElementFactory
	{
		@Override
		public LogEntry create (Action action, Activity activity, Enrolment enrolment, String ip, Date time)
		{
			return new LogData (action, activity, enrolment, ip, time);
		}

		@Override
		public void setId (LogEntry entry, Long id)
		{
			((LogData) entry).setId (id);
		}
	}

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

	/** The Sub-activity which is associated with the log entry */
	private LogReference<? extends ActivityGroupMember> reference;

	static
	{
		AbstractElement.registerElement (LogEntry.class, LogData.class, DefaultLogEntryBuilder.class, new Factory ());
	}

	public LogData ()
	{
		this.id = null;
		this.ip = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.reference = null;
		this.time = null;
	}

	public LogData (Action action, Activity activity, Enrolment enrolment, String ip, Date time)
	{
		this ();

		this.action = action;
		this.activity = activity;
		this.enrolment = enrolment;
		this.ip = ip;
		this.time = time;
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof LogData)
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

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1093;
		final int mult = 887;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.action);
		hbuilder.append (this.activity);
		hbuilder.append (this.enrolment);
		hbuilder.append (this.ip);
		hbuilder.append (this.time);
		hbuilder.append (this.reference);

		return hbuilder.toHashCode ();
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	protected void setEnrolment (Enrolment enrolment)
	{
		this.enrolment = enrolment;
	}

	public Activity getActivitydb ()
	{
		return this.activity;
	}

	protected void setActivitydb (Activity activity)
	{
		this.activity = activity;
	}

	public LogReference<? extends ActivityGroupMember> getReference ()
	{
		return this.reference;
	}

	protected void setReference (LogReference<? extends ActivityGroupMember> reference)
	{
		this.reference = reference;
	}

	@Override
	public Action getAction ()
	{
		return this.action;
	}

	protected void setAction (Action action)
	{
		this.action = action;
	}

	@Override
	public Date getTime ()
	{
		return (Date) this.time.clone ();
	}

	protected void setTime (Date time)
	{
		this.time = time;
	}

	public String getIPAddress ()
	{
		return this.ip;
	}

	protected void setIPAddress (String ip)
	{
		this.ip = ip;
	}

	@Override
	public Activity getActivity ()
	{
		Activity result = this.activity;

		if (this.reference != null)
		{
			result = this.reference.getActivity ();
		}

		return result;
	}

	@Override
	public Course getCourse ()
	{
		return this.activity.getCourse ();
	}

	@Override
	public String toString ()
	{
		String result = new String (this.enrolment + ": " + this.getActivity () + ", " + this.action + ", " + this.time);

		if (this.ip != null)
		{
			result = new String (result + ", " + this.ip);
		}

		return result;
	}
}
