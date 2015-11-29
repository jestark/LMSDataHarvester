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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.util.Date;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

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
 */

public class LogData extends LogEntry
{
	/**
	 * <code>Builder</code> for <code>LogData</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.LogEntry.Builder
	 */

	public static final class Builder extends LogEntry.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
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

		private Builder (
				final Persister<LogEntry> persister,
				final Retriever<Action> actionRetriever,
				final Retriever<Activity> activityRetriever,
				final Retriever<Enrolment> enrolmentRetriever,
				final Retriever<Network> networkRetriever)
		{
			super (persister, actionRetriever, activityRetriever, enrolmentRetriever, networkRetriever);
		}

		/**
		 * Create an instance of the <code>LogEntry</code>.
		 *
		 * @return The new <code>LogEntry</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected LogEntry createElement ()
		{
			this.log.trace ("createElement");

			return new LogData (this);
		}

		/**
		 * Get the <code>ActivityReference</code> with which the
		 * <code>LogEntry</code> is associated.  This method exists for the
		 * benefit of the <code>LogEntry</code> implementation.
		 *
		 * @return the <code>ActivityReference</code>
		 */

		@Override
		@CheckReturnValue
		protected ActivityReference getActivityReference ()
		{
			return super.getActivityReference ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the log entry */
	private @Nullable Long id;

	/** The enrolment which generated the log entry */
	private Enrolment enrolment;

	/** The activity which is associated with the log entry */
	private ActivityReference activity;

	/** The logged action, which was performed on the associated activity */
	private Action action;

	/** The originating <code>Network</code> for the logged action */
	private Network network;

	/** The time at which the action was performed */
	private Date time;

	/** The <code>SubActivity</code> which is associated with the log entry */
	private @Nullable LogReference reference;

	/**
	 * Create the <code>LogEntry</code> with null values.
	 */

	protected LogData ()
	{
		this.id = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.network = null;
		this.reference = null;
		this.time = new Date ();
	}

	/**
	 * Create an <code>LogEntry</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected LogData (final Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.action = Preconditions.checkNotNull (builder.getAction (), "action");
		this.activity = Preconditions.checkNotNull (builder.getActivityReference (), "activity");
		this.enrolment = Preconditions.checkNotNull (builder.getEnrolment (), "enrolment");
		this.network = Preconditions.checkNotNull (builder.getNetwork (), "network");
		this.time = Preconditions.checkNotNull (builder.getTime (), "time");
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>LogEntry</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
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
		return this.propagateDomainModel (this.action);
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.  This method is intended to be used to initialize
	 * a new <code>LogEntry</code> instance.
	 *
	 * @param  action The <code>Action</code>, not null
	 */

	@Override
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
		return this.getActivityReference ().getActivity ();
	}

	/**
	 * Get the <code>ActivityReference</code> for the <code>Activity</code>
	 * upon which the logged <code>Action</code> was performed.
	 *
	 * @return The <code>ActivityReference</code>
	 */

	@Override
	protected ActivityReference getActivityReference ()
	{
		return this.propagateDomainModel (this.activity);
	}

	/**
	 * Set the <code>ActivityReference</code> upon which the logged action was
	 * performed.  This method is intended to be used to initialize a new
	 * <code>LogEntry</code> instance.
	 *
	 * @param  activity The <code>ActivityReference</code>, not null
	 */

	@Override
	protected void setActivityReference (final ActivityReference activity)
	{
		assert activity != null : "activity is NULL";

		this.activity = activity;
	}

	/**
	 * Get the <code>Course</code> for which the action was logged.
	 *
	 * @return A reference to the associated <code>Course</code>
	 */

	@Override
	public Course getCourse ()
	{
		return this.getActivity ().getCourse ();
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
		return this.propagateDomainModel (this.enrolment);
	}

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.  This method is intended to be used to
	 * initialize a new <code>LogEntry</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	@Override
	protected void setEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		this.enrolment = enrolment;
	}

	/**
	 * Get the <code>Network</code> from which the logged <code>Action</code>
	 * originated.
	 *
	 * @return The <code>Network</code>
	 */

	@Override
	public Network getNetwork ()
	{
		return this.propagateDomainModel (this.network);
	}

	/**
	 * Set the <code>Network</code> from which the logged <code>Action</code>
	 * originated.  This method is intended to be used to initialize a new
	 * <code>LogEntry</code> instance.
	 *
	 * @param  network The <code>Network</code>, not null
	 */

	@Override
	protected void setNetwork (final Network network)
	{
		assert network != null : "network is NULL";

		this.network = network;
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

	@Override
	@CheckReturnValue
	protected LogReference getReference ()
	{
		return (this.reference != null) ? this.propagateDomainModel (this.reference) : null;
	}

	/**
	 * Set the reference to the <code>SubActivity</code> to the
	 * <code>LogEntry</code>.  This method is intended to be used to initialize
	 * a new <code>LogEntry</code> instance.
	 *
	 * @param  reference The <code>LogReference</code> instance, not null
	 */

	@Override
	protected void setReference (final @Nullable LogReference reference)
	{
		this.reference = reference;
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	@Override
	@CheckReturnValue
	public SubActivity getSubActivity ()
	{
		return (this.reference != null) ? this.getReference ().getSubActivity () : null;
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
	 * to be used to initialize a new <code>LogEntry</code> instance.
	 *
	 * @param  time The time, not null
	 */

	@Override
	protected void setTime (final Date time)
	{
		assert time != null : "time is NULL";

		this.time = time;
	}
}
