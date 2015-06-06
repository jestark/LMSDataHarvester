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

public final class DefaultLogEntryBuilder extends AbstractBuilder<LogEntry, LogEntry.Properties> implements LogEntryBuilder
{
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
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>LogEntry</code> instance will be inserted
	 */

	protected DefaultLogEntryBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
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
		this.log.trace ("load: entry={}", entry);

		if (entry == null)
		{
			this.log.error ("Attempting to load a NULL LogEntry");
			throw new NullPointerException ();
		}

		super.load (entry);
		this.setAction (entry.getAction ());
		this.setActivity (entry.getActivity ());
		this.setEnrolment (entry.getEnrolment ());
		this.setIPAddress (entry.getIPAddress ());
		this.setTime (entry.getTime ());

		// reference ??

		this.setPropertyValue (LogEntry.Properties.ID, entry.getId ());
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
		return this.getPropertyValue (Action.class, LogEntry.Properties.ACTION);
	}

	@Override
	public LogEntryBuilder setAction (final Action action)
	{
		this.log.trace ("setAction: action={}", action);

		if (action == null)
		{
			this.log.error ("Action is NULL");
			throw new NullPointerException ("Action is NULL");
		}

		this.setPropertyValue (LogEntry.Properties.ACTION, action);

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
		return this.getPropertyValue (Activity.class, LogEntry.Properties.ACTIVITY);
	}

	@Override
	public LogEntryBuilder setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Activity is NULL");
			throw new NullPointerException ("Activity is NULL");
		}

		this.setPropertyValue (LogEntry.Properties.ACTIVITY, activity);

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
		return this.getPropertyValue (Enrolment.class, LogEntry.Properties.ENROLMENT);
	}

	@Override
	public LogEntryBuilder setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Enrolment is NULL");
			throw new NullPointerException ("Enrolment is NULL");
		}

		this.setPropertyValue (LogEntry.Properties.ENROLMENT, enrolment);

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
		return this.getPropertyValue (Date.class, LogEntry.Properties.TIME);
	}

	@Override
	public LogEntryBuilder setTime (final Date time)
	{
		this.log.trace ("setTime: time={}", time);

		if (time == null)
		{
			this.setPropertyValue (LogEntry.Properties.TIME, new Date ());
		}
		else
		{
			this.setPropertyValue (LogEntry.Properties.IPADDRESS, time);
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
		return this.getPropertyValue (String.class, LogEntry.Properties.IPADDRESS);
	}

	@Override
	public LogEntryBuilder setIPAddress (final String ipaddress)
	{
		this.log.trace ("setIPAddress: ipaddress={}", ipaddress);

		this.setPropertyValue (LogEntry.Properties.IPADDRESS, ipaddress);

		return this;
	}
}
