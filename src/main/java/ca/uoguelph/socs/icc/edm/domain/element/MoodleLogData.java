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
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;
import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

import ca.uoguelph.socs.icc.edm.domain.resolver.Resolver;

public class MoodleLogData extends LogEntry
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the log entry */
	private Long id;

	/** The logged <code>Action</code> */
	private Action action;

	/** The associated <code>Activity</code> */
	private Activity activity;

	/** The <code>Enrolment</code> which generated the log entry */
	private Enrolment enrolment;

	/** The associated <code>Network</code> */
	private Network network;

	/** The reference to the logged <code>SubActivity</code> */
	private LogReference reference;

	/** The time at which the action was performed */
	private Date time;

	/** The <code>ActivityType</code> for the associated <code>Activity</code> */
	private ActivityType type;

	/** The <code>User</code> which created the <code>LogEntry</code> */
	private User userElement;

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

	private Action buildAction (final DataStore datastore, final String name)
	{
		if (this.getAction () == null)
		{
			this.setAction (datastore.getQuery (Action.class, Action.SELECTOR_NAME)
					.setProperty (Action.NAME, name)
					.query ());

			if (this.getAction () == null)
			{
				this.action = Action.builder (datastore)
					.setName (name)
					.build ();
			}
		}

		return this.action;
	}

	private Activity buildActivity (final DataStore datastore, final String module)
	{
		if (this.getActivity () == null)
		{
			this.activity = datastore.getQuery (Activity.class,
					GenericActivity.class,
					Selector.getInstance ("instance", true,
						Activity.TYPE, Activity.COURSE))
				.setProperty (Activity.TYPE, this.loadActivityType (module))
				.setProperty (Activity.COURSE, this.getCourse ())
				.query ();

			if (this.activity == null)
			{
				this.activity = Activity.builder (datastore, this.loadActivityType (module))
					.setCourse (this.course)
					.build ();
			}
		}

		return this.activity;
	}

	private Network buildNetwork (final DataStore datastore, final String ipAddress)
	{
		if (this.getNetwork () == null)
		{
			String name = Resolver.getInstance ().getOrgName (ipAddress);

			this.setNetwork (datastore.getQuery (Network.class, Network.SELECTOR_NAME)
					.setProperty (Network.NAME, name)
					.query ());

			if (this.getNetwork () == null)
			{
				this.setNetwork (Network.builder (datastore)
						.setName (name)
						.build ());
			}
		}

		return this.getNetwork ();
	}

	private Activity loadActivity (final Long id, final String type)
	{
		if (id != 0)
		{
			this.activity = this.getDataStore ()
				.getQuery (Activity.class,
						Activity.getActivityClass (this.loadActivityType (type)),
						Activity.SELECTOR_ID)
				.setProperty (Activity.ID, id)
				.query ();
		}

		return this.activity;
	}

	private ActivityType loadActivityType (final String name)
	{
		if (this.type != null)
		{
			this.type = this.getDataStore ()
				.getQuery (ActivityType.class, ActivityType.SELECTOR_NAME)
				.setProperty (ActivityType.NAME, this.module)
				.query ();
		}

		return this.type;
	}

	private Enrolment loadEnrolment (final DataStore datastore, final Long userid)
	{
		if (this.enrolment == null)
		{
			if (this.loadUser (userid) != null)
			{
				User u = datastore.getQuery (User.class, User.SELECTOR_USERNAME)
					.setAllProperties (this.userElement)
					.query ();

				if (u != null)
				{
					this.enrolment = u.getEnrolment (this.course);
				}
				else
				{
					throw new IllegalStateException ("User does not exist in the destination database");
				}
			}
			else
			{
				throw new IllegalStateException ("User does not exist");
			}
		}

		return this.enrolment;
	}

	private SubActivity loadSubActivity (final SubActivity subActivity)
	{
		return null;
	}

	private User loadUser (final Long id)
	{
		if (this.userElement == null)
		{
			this.userElement = this.getDataStore ()
				.getQuery (User.class, User.SELECTOR_ID)
				.setProperty (User.ID, id)
				.query ();
		}

		return this.userElement;
	}

	/**
	 * Get an <code>LogEntryBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>LogEntryBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>LogEntry</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>LogEntryBuilder</code>
	 */

	@Override
	public LogEntryBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return LogEntry.builder (this.getDataStore ())
			.setAction (this.buildAction (datastore, this.actionName))
			.setActivity (this.buildActivity (datastore, this.module))
			.setEnrolment (this.loadEnrolment (datastore, this.user))
			.setNetwork (this.buildNetwork (datastore, this.ip))
			.setSubActivity (this.loadSubActivity (this.getSubActivity ()))
			.setTime (this.time);
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
		return this.action;
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
		return (this.activity != null) ? this.activity :
			this.loadActivity (this.activityId, this.module);
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
		return this.enrolment;
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
		return this.network;
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
		return this.reference;
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

	/**
	 * Get the name of the logged <code>Action</code>.
	 *
	 * @return The name of the logged <code>Action</code>
	 */

	public String getActionName ()
	{
		return this.actionName;
	}

	/**
	 * Set the name of the logged <code>Action</code>.
	 *
	 * @param  actionName The name of the logged <code>Action</code>, not null
	 */

	protected void setActionName (final String actionName)
	{
		assert ip != null : "ip is NULL";

		this.actionName = actionName;
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
		assert ip != null : "ip is NULL";

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
		assert ip != null : "ip is NULL";

		this.info = info;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @return A <code>String</code> containing the IP address
	 */

	public String getIPAddress ()
	{
		return this.ip;
	}

	/**
	 * Get the Internet Protocol address which is associated with the logged
	 * <code>Action</code>.
	 *
	 * @param  ip The IP Address, not null
	 */

	protected void setIPAddress (final String ip)
	{
		assert ip != null : "ip is NULL";

		this.ip = ip;
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
		assert ip != null : "ip is NULL";

		this.module = module;
	}

	/**
	 * Get the ID number of the <code>User</code> which performed the logged
	 * <code>Action</code>.
	 *
	 * @return The ID number
	 */

	public Long getUser ()
	{
		return this.user;
	}

	/**
	 * Set the ID number of the <code>User</code> which performed the logged
	 * <code>Action</code>.
	 *
	 * @param  user The ID number of the <code>User</code>, not null
	 */

	protected void setUser (final Long user)
	{
		assert ip != null : "ip is NULL";

		this.user = user;
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
