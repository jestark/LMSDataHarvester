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

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

public class MoodleLogData extends LogEntry
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the log entry */
	private Long id;

	/** The name of the <code>Action</code> */
	private String action;

	/** The <code>DataStore</code> id of the associated <code>Activity</code> */
	private Long activityId;

	/** The Associated <code>Course</code> */
	private Course course;

	/** The time at which the action was performed */
	private Long time;

	/** The name of the module */
	private String module;

	/** The <code>DataStore</code> id of the associated <code>User</code> */
	private Long userId;

	/** The originating IP Address for the logged action */
	private String ipAddress;

	/** The info String*/
	private String info;

	/** The URL contained in the <code>LogEntry</code> */
	private String url;

	/**
	 * Static initializer to register the <code>LogData</code> class with the
	 * factories.
	 */

	static
	{
		Implementation.getInstance (LogEntry.class, MoodleLogData.class, MoodleLogData::new);
	}

	/**
	 * Create the <code>MoodleLogData</code>
	 */

	protected MoodleLogData ()
	{
		this.id = null;
		this.action = null;
		this.activityId = null;
		this.course = null;
		this.time = null;
		this.userId = null;
		this.ipAddress = null;
		this.module = null;
		this.info = null;
		this.url = null;
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
		return null;
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

		throw new UnsupportedOperationException ();
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

	/**
	 * Get the <code>ActivityReference</code> for the <code>Activity</code>
	 * upon which the logged <code>Action</code> was performed.
	 *
	 * @return The <code>ActivityReference</code>
	 */

	@Override
	protected ActivityReference getActivityReference ()
	{
		return null;
	}

	/**
	 * Set the <code>ActivityReference</code> upon which the logged action was
	 * performed.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>ActivityReference</code>, not null
	 */

	@Override
	protected void setActivityReference (final ActivityReference activity)
	{
		assert activity != null : "activity is NULL";

		throw new UnsupportedOperationException ();
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

	/**
	 * Set the <code>Course</code> for which the <code>Action</code> was
	 * logged.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected void setCourse (final Course course)
	{
		assert course != null : "course is NULL";

		this.course = course;
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
		return null;
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

		throw new UnsupportedOperationException ();
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
		assert network != null : "network is NULL";

		throw new UnsupportedOperationException ();
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

	/**
	 * Get the time of the logged <code>Action</code>.
	 *
	 * @return A <code>Date</code> object containing the logged time
	 */

	@Override
	public Date getTime ()
	{
		return new Date (this.time * 1000);
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

		this.time = Long.valueOf (time.getTime () / 1000);
	}

	/**
	 * Get the name of the logged <code>Action</code>.
	 *
	 * @return The name of the logged <code>Action</code>
	 */

	public String getActionName ()
	{
		return this.action;
	}

	/**
	 * Set the name of the logged <code>Action</code>.
	 *
	 * @param  action The name of the logged <code>Action</code>, not null
	 */

	protected void setActionName (final String action)
	{
		assert action != null : "action is NULL";

		this.action = action;
	}

	/**
	 * Get the ID number of the <code>Activity</code>.
	 *
	 * @return The ID number of the <code>Activity</code>
	 */

	public Long getActivityId ()
	{
		return this.activityId;
	}

	/**
	 * Set the ID number of the <code>Activity</code>.
	 *
	 * @param  activityId The ID number of the <code>Activity</code>, not null
	 */

	protected void setActivityId (final Long activityId)
	{
		assert activityId != null : "activityId is NULL";

		this.activityId = activityId;
	}

	/**
	 * Get the "info" string for the <code>LogEntry</code>.
	 *
	 * @return The info string
	 */

	public String getInfo ()
	{
		return this.info;
	}

	/**
	 * Set the "info" string for the <code>LogEntry</code>.
	 *
	 * @param  info The info string, not null
	 */

	protected void setInfo (final String info)
	{
		assert info != null : "info is NULL";

		this.info = info;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @return A <code>String</code> containing the IP address
	 */

	public String getIpAddress ()
	{
		return this.ipAddress;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @param  ipAddress The IP Address, not null
	 */

	protected void setIpAddress (final String ipAddress)
	{
		assert ipAddress != null : "ipAddress is NULL";

		this.ipAddress = ipAddress;
	}

	/**
	 * Set the name of the module (<code>ActivityType</code>) upon which the
	 * logged <code>Action</code> was performed.
	 *
	 * @return The name of the module
	 */

	public String getModule ()
	{
		return this.module;
	}

	/**
	 * Set the name of the module (<code>ActivityType</code>) upon which the
	 * logged <code>Action</code> was performed.
	 *
	 * @param  module The name of the module, not null
	 */

	protected void setModule (final String module)
	{
		assert module != null : "module is NULL";

		this.module = module;
	}

	/**
	 * Get the ID number of the <code>User</code> which performed the logged
	 * <code>Action</code>.
	 *
	 * @return The ID number
	 */

	public Long getUserId ()
	{
		return this.userId;
	}

	/**
	 * Set the ID number of the <code>User</code> which performed the logged
	 * <code>Action</code>.
	 *
	 * @param  userId The ID number of the <code>User</code>, not null
	 */

	protected void setUserId (final Long userId)
	{
		assert userId != null : "userId is NULL";

		this.userId = userId;
	}

	/**
	 * Get the URL associated with the <code>LogEntry</code>.
	 *
	 * @return The URL
	 */

	public String getUrl ()
	{
		return this.url;
	}

	/**
	 * Set the URL associated with the <code>LogEntry</code>
	 *
	 * @param url The URL, not null
	 */

	protected void setUrl (final String url)
	{
		assert url != null : "url is NULL";

		this.url = url;
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

		builder.append ("action", this.action);
		builder.append ("activityId", this.activityId);
		builder.append ("course", this.course);
		builder.append ("module", this.module);
		builder.append ("userId", this.userId);
		builder.append ("time", this.time);
		builder.append ("ipAddress", this.ipAddress);
		builder.append ("info", this.info);
		builder.append ("url", this.url);

		return builder.toString ();
	}
}
