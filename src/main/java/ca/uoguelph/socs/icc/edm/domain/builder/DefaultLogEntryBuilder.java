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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

public final class DefaultLogEntryBuilder extends AbstractBuilder<LogEntry> implements LogEntryBuilder
{
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
	 * static initializer to register the <code>DefaultLogEntryBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultLogEntryBuilder.class, DefaultLogEntryBuilder::new);
	}

	/**
	 * Create the <code>DefaultLogEntryBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>LogEntry</code> instance will be inserted
	 */

	protected DefaultLogEntryBuilder (final DataStore datastore)
	{
		super (LogEntry.class, datastore);
	}

	@Override
	protected LogEntry buildElement ()
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

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.ipaddress = null;
		this.time = new Date ();
	}

	/**
	 * Load a <code>LogEntry</code> instance into the
	 * <code>LogEntryBuilder</code>.  This method resets the
	 * <code>LogEntryBuilder</code> and initializes all of its parameters from
	 * the specified <code>LogEntry</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  entry                    The <code>LogEntry</code> to load into
	 *                                  the <code>LogEntryBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>LogEntry</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final LogEntry entry)
	{
		this.log.trace ("Load LogEntry: {}", entry);

		super.load (entry);
		this.setAction (entry.getAction ());
		this.setActivity (entry.getActivity ());
		this.setEnrolment (entry.getEnrolment ());
		this.setIPAddress (entry.getIPAddress ());
		this.setTime (entry.getTime ());

		// reference ??
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

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

	/**
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged action.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

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

	/**
	 * Get the time of the logged action.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

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

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * action.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	@Override
	public String getIPAddress ()
	{
		return this.ipaddress;
	}

	@Override
	public LogEntryBuilder setIPAddress (final String ipaddress)
	{
		this.ipaddress = ipaddress;

		return this;
	}
}
