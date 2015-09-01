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

import java.io.Serializable;
import java.util.Date;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

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

public class LogData extends LogEntry implements Serializable
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

	/** The originating <code>Network</code> for the logged action */
	private Network network;

	/** The time at which the action was performed */
	private Date time;

	/** The <code>SubActivity</code> which is associated with the log entry */
	private LogReference reference;

	/**
	 * Static initializer to register the <code>LogData</code> class with the
	 * factories.
	 */

	static
	{
		Implementation.getInstance (LogEntry.class, LogData.class, LogData::new);
	}

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
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
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

	@Override
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
		return this.propagateDomainModel (this.action);
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
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
		return this.propagateDomainModel (this.activity);
	}

	/**
	 * Set the <code>Activity</code>  upon which the logged action was
	 * performed.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	@Override
	protected void setActivity (final Activity activity)
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
	 * performed the logged action.   This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
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
	 * originated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
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
	public LogReference getReference ()
	{
		return (this.reference != null) ? this.propagateDomainModel (this.reference) : null;
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

	@Override
	protected void setReference (final LogReference reference)
	{
		assert reference != null : "reference is NULL";

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
	 * to be used by a <code>DataStore</code> when the <code>LogEntry</code>
	 * instance is loaded.
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
