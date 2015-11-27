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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
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
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Moodle specific implementation of the <code>LogEntry</code> interface.  This
 * class attempts to shoe-horn the moodle log data into something that resembles
 * a <code>LogEntry</code>.  Unfortunately, due to the way that Moodle stores
 * it's data the result is that all of the <code>LogEntry</code> properties
 * except time are unimplementated, with additional translation being required
 * to convert the Moodle log data to an actual <code>LogEntry</code>.
 */

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
		return null;
	}

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.  This operation is unsupported since the Moodle
	 * database does not have <code>Action</code> instances
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
	 * performed.  Since the Moodle database tracks <code>Activity</code>
	 * references in ways that violate referential integrity, and are generally
	 * un-mappable, this operation is unsupported.
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
	 * logged.  This method is intended to be used to initialize a new
	 * <code>MoodleLogData</code> instance.
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
	 * performed the logged action.  Since the moodle database has a different
	 * concept of an <code>Enrolment</code> than the <code>LogEnty</code>
	 * interface, this operation is unsupported.
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
	 * originated.  This operation is unsupported since the Moodle database has
	 * no concept of a <code>Network</code>.
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
	@CheckReturnValue
	public LogReference getReference ()
	{
		return null;
	}

	/**
	 * Set the reference to the <code>SubActivity</code> to the
	 * <code>LogEntry</code>.  Moodle stores references to the
	 * <code>SubActivity</code> in a text field, which requires a fair amount of
	 * translation processing.  Hense, this operation is unsupported
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
	 * to be used to initialize a new <code>LogEntry</code> instance.
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
	 * Set the name of the logged <code>Action</code>. This method is intended
	 * to be used to initialize a new <code>LogEntry</code> instance.
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
	 * Set the ID number of the <code>Activity</code>.  This method is intended
	 * to be used to initialize a new <code>LogEntry</code> instance.
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
	 * Set the "info" string for the <code>LogEntry</code>.  This method is
	 * intended to be used to initialize a new <code>LogEntry</code> instance.
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
	 * logged <code>Action</code> was performed.  This method is intended to be
	 * used to initialize a new <code>LogEntry</code> instance.
	 *
	 * @return The name of the module
	 */

	public String getModule ()
	{
		return this.module;
	}

	/**
	 * Set the name of the module (<code>ActivityType</code>) upon which the
	 * logged <code>Action</code> was performed.  This method is intended to be
	 * used to initialize a new <code>LogEntry</code> instance.
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
	 * <code>Action</code>.  This method is intended to be used to initialize a
	 * new <code>LogEntry</code> instance.
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
	 * Set the URL associated with the <code>LogEntry</code>.  This method is
	 * intended to be used to initialize a new <code>LogEntry</code> instance.
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
		return MoreObjects.toStringHelper (this)
			.add ("action", this.action)
			.add ("activityId", this.activityId)
			.add ("course", this.course)
			.add ("module", this.module)
			.add ("userId", this.userId)
			.add ("time", this.time)
			.add ("ipAddress", this.ipAddress)
			.add ("info", this.info)
			.add ("url", this.url)
			.toString ();
	}
}
