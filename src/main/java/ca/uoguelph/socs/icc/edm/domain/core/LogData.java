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
import ca.uoguelph.socs.icc.edm.domain.factory.LogEntryFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager;

public class LogData implements LogEntry, Serializable
{
	private static final class LogDataFactory implements LogEntryElementFactory
	{
		@Override
		public LogEntry create (Action action, Activity activity, Enrolment enrolment, String ip, Date time)
		{
			LogData entry = new LogData ();

			entry.setAction (action);
			entry.setActivitydb (activity);
			entry.setEnrolment (enrolment);
			entry.setIPAddress (ip);
			entry.setTime (time);

			return entry;
		}

		@Override
		public void setId (LogEntry entry, Long id)
		{
			((LogData) entry).setId (id);
		}
	}

	private static final long serialVersionUID = 1L;

	private Long id;
	private Enrolment enrolment;
	private Activity activity;
	private Action action;
	private Date time;
	private String ip;
	private LogReference<? extends ActivityGroupMember> reference;

	static
	{
		(LogEntryFactory.getInstance ()).registerElement (LogData.class, DefaultLogEntryManager.class, DefaultLogEntryBuilder.class, new LogDataFactory ());
	}

	protected LogData ()
	{
		this.id = null;
		this.ip = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.reference = null;
		this.time = null;
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
