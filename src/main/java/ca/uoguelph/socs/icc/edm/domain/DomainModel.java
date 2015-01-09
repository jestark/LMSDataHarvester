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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreTransaction;

import ca.uoguelph.socs.icc.edm.domain.factory.ActionFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivitySourceFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityTypeFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.CourseFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.LogEntryFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.QueryFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.RoleFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.UserFactory;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DomainModel
{
	/** The data store which contains all of the data */
	private final DataStore datastore;

	/** The profile */
	private final DomainModelProfile profile;

	/** The log */
	private final Log log;

	/**
	 * Create the <code>DomainModel</code>
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the data
	 *                   represented by this <code>DomainModel</code>, not null
	 * @param  profile   The configuration profile, not null
	 */

	public DomainModel (DataStore datastore, DomainModelProfile profile)
	{
		this.datastore = datastore;
		this.profile = profile;

		this.log = LogFactory.getLog (DomainModel.class);
	}

	protected DataStore getDataStore ()
	{
		return this.datastore;
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> is mutable,
	 *         <code>false</code> otherwise
	 */

	public Boolean isMutable ()
	{
		return this.profile.isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>true</code> if the underlying <code>DataStore</code> is open,
	 *         <code>false</code> otherwise
	 * @see    ca.uoguelph.socs.icc.edm.domain.datastore.DataStore#isOpen
	 */

	public Boolean isOpen ()
	{
		return this.datastore.isOpen ();
	}

	/**
	 * Close the connections to any underlying data sources.
	 */

	public void close ()
	{
		this.datastore.close ();
	}

	public DataStoreTransaction getTransaction ()
	{
		return this.datastore.getTransaction ();
	}

	public DomainModelProfile getProfile ()
	{
		return this.profile;
	}

	/**
	 * Get the <code>ActionManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the <code>ActionManager</code>
	 * @throws IllegalStateException If the <code>Action</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public ActionManager getActionManager ()
	{
		if (! this.profile.isAvailable (Action.class))
		{
			this.log.error ("Action interface is not represented by the data store");
			throw new IllegalStateException ("Action interface is not available");
		}

		return (ActionFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>ActivityManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActivityManager</code>
	 * @throws IllegalStateException If the <code>Activity</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public ActivityManager getActivityManager ()
	{
		if (! this.profile.isAvailable (Activity.class))
		{
			this.log.error ("Activity interface is not represented by the data store");
			throw new IllegalStateException ("Activity interface is not available");
		}

		return (ActivityFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>ActivitySourceManager</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActivitySourceManager</code>
	 * @throws IllegalStateException If the <code>ActivitySource</code> interface
	 *                               is not available for the
	 *                               <code>DomainModel</code>
	 */

	public ActivitySourceManager getActivitySourceManager ()
	{
		if (! this.profile.isAvailable (ActivitySource.class))
		{
			this.log.error ("ActivitySource interface is not represented by the data store");
			throw new IllegalStateException ("ActivitySource interface is not available");
		}

		return (ActivitySourceFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>ActivityTypeManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActivityTypeManager</code>
	 * @throws IllegalStateException If the <code>ActivityTypeManager</code>
	 *                               interface is not available for the
	 *                               <code>DomainModel</code>
	 */

	public ActivityTypeManager getActivityTypeManager ()
	{
		if (! this.profile.isAvailable (ActivityType.class))
		{
			this.log.error ("ActivityType interface is not represented by the data store");
			throw new IllegalStateException ("ActivityType interface is not available");
		}

		return (ActivityTypeFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>CourseManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the <code>CourseManager</code>
	 * @throws IllegalStateException If the <code>Course</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public CourseManager getCourseManager ()
	{
		if (! this.profile.isAvailable (Course.class))
		{
			this.log.error ("Course interface is not represented by the data store");
			throw new IllegalStateException ("Course interface is not available");
		}

		return (CourseFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>EnrolmentManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>EnrolmentManager</code>
	 * @throws IllegalStateException If the <code>Enrolment</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public EnrolmentManager getEnrolmentManager ()
	{
		if (! this.profile.isAvailable (Enrolment.class))
		{
			this.log.error ("Enrolment interface is not represented by the data store");
			throw new IllegalStateException ("Enrolment interface is not available");
		}

		return (EnrolmentFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>LogEntryManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>LogEntryManager</code>
	 * @throws IllegalStateException If the <code>LogEntry</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public LogEntryManager getLogEntryManager ()
	{
		if (! this.profile.isAvailable (LogEntry.class))
		{
			this.log.error ("LogEntry interface is not represented by the data store");
			throw new IllegalStateException ("LogEntry interface is not available");
		}

		return (LogEntryFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>RoleManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the <code>RoleManager</code>
	 * @throws IllegalStateException If the <code>Role</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public RoleManager getRoleManager ()
	{
		if (! this.profile.isAvailable (Role.class))
		{
			this.log.error ("Role interface is not represented by the data store");
			throw new IllegalStateException ("Role interface is not available");
		}

		return (RoleFactory.getInstance ()).createManager (this);
	}

	/**
	 * Get the <code>UserManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the <code>UserManager</code>
	 * @throws IllegalStateException If the <code>User</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public UserManager getUserManager ()
	{
		if (! this.profile.isAvailable (User.class))
		{
			this.log.error ("User interface is not represented by the data store");
			throw new IllegalStateException ("User interface is not available");
		}

		return (UserFactory.getInstance ()).createManager (this);
	}
}
