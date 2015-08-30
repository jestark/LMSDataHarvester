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

package ca.uoguelph.socs.icc.edm.domain.element;

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
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

public class MoodleLogData extends LogEntry
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

	private Long user;
	private Course course;
	private String module;
	private Long activityId;
	private String actionName;
	private String info;

	/**
	 * Static initializer to register the <code>LogData</code> class with the
	 * factories.
	 */

	static
	{
		Implementation.getInstance (LogEntry.class, MoodleLogData.class, MoodleLogData::new);
	}

	protected MoodleLogData ()
	{
		this.id = null;
		this.time = null;
		this.user = null;
		this.ip = null;
		this.course = null;
		this.module = null;
		this.activity = null;
		this.action = null;
		this.info = null;
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
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged action.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	@Override
	public Enrolment getEnrolment ()
	{
		return null;
	}

	@Override
	protected void setEnrolment (Enrolment enrolment)
	{
		throw new UnsupportedOperationException ();
	}

	public Long getUser ()
	{
		return this.user;
	}

	protected void setUser (final Long user)
	{
		this.user = user;
	}

	/**
	 * Get the <code>Course</code> for which the action was logged.
	 *
	 * @return A reference to the associated <code>Course</code>
	 */

	@Override
	public Course getCourse ()
	{
		return this.course;
	}

	protected void setCourse (final Course course)
	{
		this.course = course;
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
		return null;
	}

	@Override
	protected void setActivity (Activity activity)
	{
		throw new UnsupportedOperationException ();
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
		return null;
	}

	public String getModule ()
	{
		return this.module;
	}

	protected void setModule (final String module)
	{
		this.module = module;
	}

	public Long getActivityId ()
	{
		return this.activityId;
	}

	protected void setActivityId (final Long activityId)
	{
		this.activityId = activityId;
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
		return null;
	}

	@Override
	protected void setAction (Action action)
	{
		throw new UnsupportedOperationException ();
	}

	public String getActionName ()
	{
		return this.actionName;
	}

	protected void setActionName (final String actionName)
	{
		this.actionName = actionName;
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

	protected void setTime (final Date time)
	{
		this.time = time;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * action.
	 *
	 * @return A <code>String</code> containing the IP address, may be null
	 */

	public String getIPAddress ()
	{
		return this.ip;
	}

	protected void setIPAddress (final String ip)
	{
		this.ip = ip;
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
		return null;
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
		throw new UnsupportedOperationException ();
	}

	public String getInfo ()
	{
		return this.info;
	}

	protected void setInfo (final String info)
	{
		this.info = info;
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
		return null;
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

		throw new UnsupportedOperationException ();
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
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.enrolment);
		builder.append ("action", this.action);
		builder.append ("activity", this.activity);
		builder.append ("time", this.time);
		builder.append ("ipaddress", this.ip);
		builder.append ("user", this.user);
		builder.append ("course", this.course);
		builder.append ("module", this.module);
		builder.append ("activityId", this.activityId);
		builder.append ("actionName", this.actionName);
		builder.append ("info", this.info);

		return builder.toString ();
	}
}
