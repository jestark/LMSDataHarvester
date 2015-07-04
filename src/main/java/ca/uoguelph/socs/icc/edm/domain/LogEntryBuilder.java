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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Date;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>LogEntry</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogEntry
 */

public final class LogEntryBuilder extends AbstractBuilder<LogEntry>
{
	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static LogEntryBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, LogEntry.class, LogEntryBuilder::new);
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>LogEntry</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  entry                 The <code>LogEntry</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static LogEntryBuilder getInstance (final DataStore datastore, LogEntry entry)
	{
		assert datastore != null : "datastore is NULL";
		assert entry != null : "entry is NULL";

		LogEntryBuilder builder = LogEntryBuilder.getInstance (datastore);
		builder.load (entry);

		return builder;
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static LogEntryBuilder getInstance (final DomainModel model)
	{
		return LogEntryBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>LogEntry</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  entry                 The <code>LogEntry</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static LogEntryBuilder getInstance (final DomainModel model, LogEntry entry)
	{
		if (entry == null)
		{
			throw new NullPointerException ("entry is NULL");
		}

		LogEntryBuilder builder = LogEntryBuilder.getInstance (model);
		builder.load (entry);

		return builder;
	}

	/**
	 * Create the <code>LogEntryBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected LogEntryBuilder (final DataStore datastore, final Builder<LogEntry> builder)
	{
		super (datastore, builder);
	}

	/**
	 * Load a <code>LogEntry</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>LogEntry</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  entry                    The <code>LogEntry</code>, not null
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

		this.builder.setProperty (LogEntry.ID, entry.getId ());
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	public Action getAction ()
	{
		return this.builder.getPropertyValue (LogEntry.ACTION);
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.
	 *
	 * @param  action                   The <code>Action</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Action</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public void setAction (final Action action)
	{
		this.log.trace ("setAction: action={}", action);

		if (action == null)
		{
			this.log.error ("Action is NULL");
			throw new NullPointerException ("Action is NULL");
		}

		if (! this.datastore.contains (action))
		{
			this.log.error ("The specified Action does not exist in the DataStore");
			throw new IllegalArgumentException ("Action is not in the DataStore");
		}

		this.builder.setProperty (LogEntry.ACTION, action);
	}

	/**
	 * Get the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @return A reference to the associated <code>Activity</code> object.
	 */

	public Activity getActivity ()
	{
		return this.builder.getPropertyValue (LogEntry.ACTIVITY);
	}

	/**
	 * Set the <code>Activity</code> upon which the logged action was
	 * performed.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public void setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Activity is NULL");
			throw new NullPointerException ("Activity is NULL");
		}

		if (! this.datastore.contains (activity))
		{
			this.log.error ("The specified Activity does not exist in the DataStore");
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}

		this.builder.setProperty (LogEntry.ACTIVITY, activity);
	}

	/**
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged action.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	public Enrolment getEnrolment ()
	{
		return this.builder.getPropertyValue (LogEntry.ENROLMENT);
	}

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Enrolment</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public void setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Enrolment is NULL");
			throw new NullPointerException ("Enrolment is NULL");
		}

		if (! this.datastore.contains (enrolment))
		{
			this.log.error ("The specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment is not in the DataStore");
		}

		this.builder.setProperty (LogEntry.ENROLMENT, enrolment);
	}

	/**
	 * Get the time of the logged action.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	public Date getTime ()
	{
		return this.builder.getPropertyValue (LogEntry.TIME);
	}

	/**
	 * Set the time of the logged <code>Action</code>.
	 *
	 * @param  time The time
	 */

	public void setTime (final Date time)
	{
		this.log.trace ("setTime: time={}", time);

		if (time == null)
		{
			this.builder.setProperty (LogEntry.TIME, new Date ());
		}
		else
		{
			this.builder.setProperty (LogEntry.TIME, time);
		}
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * action.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	public String getIPAddress ()
	{
		return this.builder.getPropertyValue (LogEntry.IPADDRESS);
	}

	/**
	 * Set the Internet Protocol Address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @param  ipaddress A <code>String</code> containing the IP Address
	 */

	public void setIPAddress (final String ipaddress)
	{
		this.log.trace ("setIPAddress: ipaddress={}", ipaddress);

		this.builder.setProperty (LogEntry.IPADDRESS, ipaddress);
	}
}
