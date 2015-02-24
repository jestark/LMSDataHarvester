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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreProfile;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreTransaction;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DomainModel
{
	/** The data store which contains all of the data */
	private final DataStore datastore;

	/** The log */
	private final Logger log;

	/**
	 * Create the <code>DomainModel</code>
	 *
	 * @param  datastore The <code>DataStore</code> which contains all of the data
	 *                   represented by this <code>DomainModel</code>, not null
	 */

	public DomainModel (DataStore datastore)
	{
		this.datastore = datastore;

		this.log = LoggerFactory.getLogger (DomainModel.class);
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
		return (this.getProfile()).isMutable ();
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

	public DataStoreProfile getProfile ()
	{
		return this.datastore.getProfile ();
	}

	/**
	 * Get an instance of the specified <code>ElementManager</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @param  element               The <code>Element</code> interface class,
	 *                               not null
	 * @param  manager               The <code>ElementManager</code> interface
	 *                               class, not null
	 *
	 * @return                       A reference to the <code>ActionManager</code>
	 * @throws IllegalStateException If the <code>Action</code> interface is not
	 *                               available for the <code>DomainModel</code>
	 */

	public <T extends ElementManager<U>, U extends Element> T getManager (Class<U> element, Class<T> manager)
	{
		this.log.trace ("get Manager instance {} for element {}", manager, element);

		if (element == null)
		{
			this.log.error ("Attempting to get a manager instance for a NULL element");
			throw new NullPointerException ();
		}

		if (manager == null)
		{
			this.log.error ("Attempting to get a NULL manager instance");
			throw new NullPointerException ();
		}

		if (! (this.datastore.getProfile ()).isAvailable (element))
		{
			this.log.error ("");
			throw new IllegalStateException ("");
		}

		return AbstractManager.getInstance (element, manager, this);
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
		return this.getManager (Action.class, ActionManager.class);
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
		return this.getManager (Activity.class, ActivityManager.class);
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
		return this.getManager (ActivitySource.class, ActivitySourceManager.class);
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
		return this.getManager (ActivityType.class, ActivityTypeManager.class);
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
		return this.getManager (Course.class, CourseManager.class);
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
		return this.getManager (Enrolment.class, EnrolmentManager.class);
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
		return this.getManager (LogEntry.class, LogEntryManager.class);
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
		return this.getManager (Role.class, RoleManager.class);
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
		return this.getManager (User.class, UserManager.class);
	}
}
