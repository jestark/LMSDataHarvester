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

package ca.uoguelph.socs.icc.edm.domain.builder;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

public final class DefaultLogEntryBuilder extends AbstractBuilder<LogEntry> implements LogEntryBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultLogEntryBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<LogEntry, LogEntryBuilder>
	{
		/**
		 * Create the <code>LogEntryBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>LogEntryManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>LogEntryManager</code> instance, not null
		 *
		 * @return         The <code>LogEntryBuilder</code>
		 */

		@Override
		public LogEntryBuilder create (final ManagerProxy<LogEntry> manager)
		{
			return new DefaultLogEntryBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The <code>Action</code> performed by the user */
	private Action action;

	/** The <code>Activity</code> upon which the user operated */
	private Activity activity;

	/** The <code>Enrolment</code> representing the user */
	private Enrolment enrolment;

	/** The time at which the user accessed the <code>Activity</code> */
	private Date time;

	/** The ip-address from with the user accessed the <code>Activity</code> */
	private String ipaddress;

	/**
	 * static initializer to register the <code>DefaultLogEntryBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (LogEntryBuilder.class, DefaultLogEntryBuilder.class, new Factory ());
	}

	protected DefaultLogEntryBuilder (final ManagerProxy<LogEntry> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultLogEntryBuilder.class);
	}

	@Override
	public LogEntry build ()
	{
		if (this.action == null)
		{
			this.log.error ("Can not build: The log entry's action is not set");
			throw new IllegalStateException ("action not set");
		}

		if (this.activity == null)
		{
			this.log.error ("Can not build: The log entry's activity is not set");
			throw new IllegalStateException ("activity not set");
		}

		if (this.enrolment == null)
		{
			this.log.error ("Can not build: The log entry's enrolment is not set");
			throw new IllegalStateException ("enrolment not set");
		}

		return null; //this.factory.create (this.action, this.activity, this.enrolment, this.ipaddress, this.time);
	}

	@Override
	public void clear ()
	{
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.ipaddress = null;
		this.time = new Date ();
	}

	@Override
	public Action getAction ()
	{
		return this.action;
	}

	@Override
	public LogEntryBuilder setAction (final Action action)
	{
		if (action == null)
		{
			this.log.error ("Action is NULL");
			throw new NullPointerException ("Action is NULL");
		}

		this.action = action;

		return this;
	}

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	@Override
	public LogEntryBuilder setActivity (final Activity activity)
	{
		if (activity == null)
		{
			this.log.error ("Activity is NULL");
			throw new NullPointerException ("Activity is NULL");
		}

		this.activity = activity;

		return this;
	}

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	@Override
	public LogEntryBuilder setEnrolment (final Enrolment enrolment)
	{
		if (enrolment == null)
		{
			this.log.error ("Enrolment is NULL");
			throw new NullPointerException ("Enrolment is NULL");
		}

		this.enrolment = enrolment;

		return this;
	}

	@Override
	public Date getTime ()
	{
		return this.time;
	}

	@Override
	public LogEntryBuilder setTime (final Date time)
	{
		if (time == null)
		{
			this.time = new Date ();
		}
		else
		{
			this.time = time;
		}

		return this;
	}

	@Override
	public String getIPAddress ()
	{
		return this.ipaddress;
	}

	@Override
	public LogEntryBuilder setIPAddress (final String ipaddress)
	{
		if (ipaddress != null)
		{
			// Validate the ip address (both v4 and v6)
		}

		this.ipaddress = ipaddress;

		return this;
	}
}
