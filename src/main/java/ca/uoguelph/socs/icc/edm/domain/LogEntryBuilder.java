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

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

/**
 * Create new <code>LogEntry</code> instances.  This class implements
 * <code>Builder</code>, adding the functionality required to create
 * <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogEntry
 */

public final class LogEntryBuilder implements Builder<LogEntry>
{
	/** The Logger */
	private final Logger log;

	/** Helper to substitute <code>Action</code> instances */
	private final Retriever<Action> actionRetriever;

	/** Helper to substitute <code>Action</code> instances */
	private final Retriever<Activity> activityRetriever;

	/** Helper to substitute <code>Enrolment</code> instances */
	private final Retriever<Enrolment> enrolmentRetriever;

	/** Helper to substitute <code>Network</code> instances */
	private final Retriever<Network> networkRetriever;

	/** Helper to operate on <code>LogEntry</code> instances */
	private final Persister<LogEntry> persister;

	/** Method reference to the constructor of the implementation class */
	private final Supplier<LogEntry> supplier;

	/** The loaded or previously created <code>LogEntry</code> */
	private LogEntry entry;

	/** The <code>DataStore</code> ID number for the <code>LogEntry</code> */
	private Long id;

	/** The associated <code>Action</code> */
	private Action action;

	/** The associated <code>Activity</code> */
	private Activity activity;

	/** The associated <code>Enrolment</code> */
	private Enrolment enrolment;

	/** The associated <code>Network</code> */
	private Network network;

	/** The time that the entry was logged */
	private Date time;

	/** The <code>LogReferenceBuilder</code> */
	private LogReferenceBuilder referenceBuilder;

	/**
	 * Create the <code>LogEntryBuilder</code>.
	 *
	 * @param  supplier           Method reference to the constructor of the
	 *                            implementation class, not null
	 * @param  persister          The <code>Persister</code> used to store the
	 *                            <code>LogEntry</code>, not null
	 * @param  actionRetriever    <code>Retriever</code> for
	 *                            <code>Action</code> instances, not null
	 * @param  activityRetriever  <code>Retriever</code> for
	 *                            <code>Activity</code> instances, not null
	 * @param  enrolmentRetriever <code>Retriever</code> for
	 *                            <code>Enrolment</code> instances, not null
	 * @param  networkRetriever   <code>Retriever</code> for
	 *                            <code>Network</code> instances, not null
	 */

	protected LogEntryBuilder (final Supplier<LogEntry> supplier, final Persister<LogEntry> persister, final Retriever<Action> actionRetriever, final Retriever<Activity> activityRetriever, final Retriever<Enrolment> enrolmentRetriever, final Retriever<Network> networkRetriever)
	{
		assert persister != null : "persister is NULL";
		assert actionRetriever != null : "actionRetriever is NULL";
		assert activityRetriever != null : "activityRetriever is NULL";
		assert enrolmentRetriever != null : "enrolmentRetriever is NULL";
		assert networkRetriever != null : "networkRetriever is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.actionRetriever = actionRetriever;
		this.activityRetriever = activityRetriever;
		this.enrolmentRetriever = enrolmentRetriever;
		this.networkRetriever = networkRetriever;
		this.persister = persister;
		this.supplier = supplier;

		this.entry = null;
		this.id = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.network = null;
		this.time = null;
		this.referenceBuilder = null;
	}

	/**
	 * Create an instance of the <code>LogEntry</code>.
	 *
	 * @return                       The new <code>LogEntry</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public LogEntry build ()
	{
		this.log.trace ("build:");

		if (this.action == null)
		{
			this.log.error ("Attempting to create an LogEntry without an Action");
			throw new IllegalStateException ("action is NULL");
		}

		if (this.activity == null)
		{
			this.log.error ("Attempting to create an LogEntry without an Activity");
			throw new IllegalStateException ("activity is NULL");
		}

		if (this.enrolment == null)
		{
			this.log.error ("Attempting to create an LogEntry without an Enrolment");
			throw new IllegalStateException ("enrolment is NULL");
		}

		if (this.time == null)
		{
			this.log.error ("Attempting to create an LogEntry without a time");
			throw new IllegalStateException ("time is NULL");
		}

		LogEntry result = this.supplier.get ();
		result.setId (this.id);
		result.setAction (this.action);
		result.setActivityReference (this.activity.getReference ());
		result.setEnrolment (this.enrolment);
		result.setNetwork (this.network);
		result.setTime (this.time);

		this.entry = this.persister.insert (this.entry, result);

		// Create the reference
		if (this.referenceBuilder != null)
		{
			this.referenceBuilder.setEntry (this.entry)
				.build ();
		}

		return this.entry;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>LogEntryBuilder</code>
	 */

	public LogEntryBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.network = null;
		this.time = null;
		this.referenceBuilder = null;

		return this;
	}

	/**
	 * Load a <code>LogEntry</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>LogEntry</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  entry                    The <code>LogEntry</code>, not null
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>LogEntry</code> instance to be
	 *                                  loaded are not valid
	 */

	public LogEntryBuilder load (final LogEntry entry)
	{
		this.log.trace ("load: entry={}", entry);

		if (entry == null)
		{
			this.log.error ("Attempting to load a NULL LogEntry");
			throw new NullPointerException ();
		}

		this.entry = entry;
		this.id = entry.getId ();
		this.setAction (entry.getAction ());
		this.setActivity (entry.getActivity ());
		this.setEnrolment (entry.getEnrolment ());
		this.setNetwork (entry.getNetwork ());
		this.setTime (entry.getTime ());

		if (entry.getReference () != null)
		{
			this.referenceBuilder = null;
		}

		return this;
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * activity.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	public Action getAction ()
	{
		return this.action;
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.
	 *
	 * @param  action                   The <code>Action</code>, not null
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException if the <code>Action</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public LogEntryBuilder setAction (final Action action)
	{
		this.log.trace ("setAction: action={}", action);

		if (action == null)
		{
			this.log.error ("Action is NULL");
			throw new NullPointerException ("Action is NULL");
		}

		this.action = this.actionRetriever.fetch (action);

		if (this.action == null)
		{
			this.log.error ("The specified Action does not exist in the DataStore");
			throw new IllegalArgumentException ("Action is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>Activity</code> upon which the logged <code>Action</code>
	 * was performed.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * Set the <code>Activity</code> upon which the logged <code>Action</code>
	 * was performed.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public LogEntryBuilder setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Activity is NULL");
			throw new NullPointerException ("Activity is NULL");
		}

		this.activity = activityRetriever.fetch (activity);

		if (this.activity == null)
		{
			this.log.error ("The specified Activity does not exist in the DataStore");
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged action.
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException if the <code>Enrolment</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public LogEntryBuilder setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Enrolment is NULL");
			throw new NullPointerException ("Enrolment is NULL");
		}

		this.enrolment = this.enrolmentRetriever.fetch (enrolment);

		if (this.enrolment == null)
		{
			this.log.error ("The specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>Network</code> from which the logged <code>Action</code>
	 * originated.
	 *
	 * @return The associated <code>Network</code>
	 */

	public Network getNetwork ()
	{
		return this.network;
	}

	/**
	 * Set the <code>Network</code> from which the logged <code>Action</code>
	 * originated.
	 *
	 * @param  network                  The <code>Network</code>, not null
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException if the specified <code>Network</code>
	 *                                  does not exist in the
	 *                                  <code>Datastore</code>
	 */

	public LogEntryBuilder setNetwork (final Network network)
	{
		this.log.trace ("setNetwork: network={}", network);

		if (network == null)
		{
			throw new NullPointerException ();
		}

		this.network = this.networkRetriever.fetch (network);

		if (this.network == null)
		{
			this.log.error ("The specified Network does not exist in the DataStore");
			throw new IllegalArgumentException ("Network is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return The associated <code>SubActivity</code>, may be null
	 */

	public SubActivity getSubActivity ()
	{
		return (this.referenceBuilder != null) ? this.referenceBuilder.getSubActivity () : null;
	}

	/**
	 * Set the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.  The <code>Subactivity</code> is
	 * optional, so it may be null.
	 *
	 * @param  subActivity              The <code>SubActivity</code>
	 *
	 * @return                          This <code>LogEntryBuilder</code>
	 * @throws IllegalArgumentException if the <code>SubActivity</code> is not
	 *                                  in the <code>DataStore</code>
	 */

	public LogEntryBuilder setSubActivity (final SubActivity subActivity)
	{
		this.log.trace ("setSubActivity: subActivity={}", subActivity);

		if (subActivity != null)
		{
//			this.subActivity = DataStoreProxy.getInstance (SubActivity.class,
//					subActivity.getClass (),
//					SubActivity.SELECTOR_ID,
//					datastore)
//				.fetch (subActivity);
//
//			if (this.subActivity == null)
//			{
//				this.log.error ("The specified SubActivity does not exist in the DataStore");
//				throw new IllegalArgumentException ("SubActivity is not in the DataStore");
//			}
		}
		else
		{
			this.referenceBuilder = null;
		}

		return this;
	}

	/**
	 * Get the time of the logged <code>Action</code>.
	 *
	 * @return The associated time
	 */

	public Date getTime ()
	{
		return (Date) this.time.clone ();
	}

	/**
	 * Set the time of the logged <code>Action</code>.
	 *
	 * @param  time The time
	 *
	 * @return      This <code>LogEntryBuilder</code>
	 */

	public LogEntryBuilder setTime (final Date time)
	{
		this.log.trace ("setTime: time={}", time);

		if (time == null)
		{
			this.time = new Date ();
		}
		else
		{
			this.time = (Date) time.clone ();
		}

		return this;
	}
}
