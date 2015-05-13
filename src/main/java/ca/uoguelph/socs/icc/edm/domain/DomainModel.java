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
	 * @param  datastore The <code>DataStore</code> which contains all of the
	 *                   data represented by this <code>DomainModel</code>,
	 *                   not null
	 */

	public DomainModel (final DataStore datastore)
	{
		this.datastore = datastore;

		this.log = LoggerFactory.getLogger (DomainModel.class);
	}

	/**
	 * Determine if the <code>DomainModel</code> is mutable.
	 *
	 * @return <code>True</code> if the <code>DomainModel</code> is mutable,
	 *         <code>False</code> otherwise
	 */

	public Boolean isMutable ()
	{
		return (this.getProfile()).isMutable ();
	}

	/**
	 * Determine if the underlying <code>DataStore</code> is open.
	 *
	 * @return <code>True</code> if the underlying <code>DataStore</code> is
	 *         open, <code>False</code> otherwise
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
	 * Test an instance of an <code>Element</code> to determine if a reference
	 * to that <code>Element</code> instance exists in the
	 * <code>DataStore</code>.  The exact behaviour of this method is
	 * determined by the implementation of the <code>DataStore</code>.
	 * <p>
	 * If the <code>Element</code> instance was created by the current instance
	 * of the <code>DataStore</code> then this method, should return
	 * <code>True</code>.  Otherwise, this method should return
	 * <code>False</code>, even if an identical <code>Element</code> instance
	 * exists in the <code>DataStore</code>.  Use the <code>fetch</code> method
	 * to retrieve an instance from the <code>DataStore</code>, if one exists.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code>
	 *                  instance contains a reference to the
	 *                  <code>Element</code>, <code>False</code> otherwise
	 */

//	public abstract boolean contains (T element);

	/**
	 * Insert the specified <code>Element</code> instance into the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to insert, not null
	 *
	 * @return         A reference to the <code>Element</code> instance in the
	 *                 <code>DataStore</code>
	 */

//	public abstract T insert (T element);

	/**
	 * Remove the specified <code>Element</code> instance from the
	 * <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> to remove, not null
	 */

//	public abstract void remove (T element);

	/**
	 * Get an instance of the specified <code>ElementManager</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @param  element               The <code>Element</code> interface class,
	 *                               not null
	 * @param  manager               The <code>ElementManager</code> interface
	 *                               class, not null
	 *
	 * @return                       A reference to the
	 *                               <code>ActionManager</code>
	 * @throws IllegalStateException If the <code>Action</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public <T extends ElementManager<U>, U extends Element> T getManager (final Class<U> element, final Class<T> manager)
	{
		this.log.trace ("getManager element={} manager={}", element, manager);

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
			this.log.error ("No ElementManager instance available for {}", manager.getSimpleName ());
			throw new IllegalStateException ("No available ElementManager instances");
		}

		return AbstractManager.getInstance (element, manager, this.datastore);
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActionBuilder</code> instance
	 */

//	public abstract ActionBuilder getBuilder ();

	/**
	 * Get the <code>ActionManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActionManager</code>
	 * @throws IllegalStateException If the <code>Action</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public ActionManager getActionManager ()
	{
		return this.getManager (Action.class, ActionManager.class);
	}

		/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  builder The <code>ActivityBuilder</code> interface of the
	 *                 builder to be returned, not null
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

//	public abstract <T extends ActivityBuilder> T getBuilder (Class<T> builder, ActivityType type);

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  type The <code>ActivityType</code> of the <code>Activity</code>
	 *              to be created by the <code>ActivityBuilder</code>
	 *
	 * @return      An <code>ActivityBuilder</code> instance
	 */

//	public abstract ActivityBuilder getBuilder (ActivityType type);

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  builder  The <code>SubActivityBuilder</code> interface of
	 *                  the builder to be returned, not null
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

//	public abstract <T extends SubActivityBuilder> T getBuilder (Class<T> builder, Activity activity);

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

//	public abstract SubActivityBuilder getBuilder (Activity activity);

	/**
	 * Get the <code>ActivityManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActivityManager</code>
	 * @throws IllegalStateException If the <code>Activity</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public ActivityManager getActivityManager ()
	{
		return this.getManager (Activity.class, ActivityManager.class);
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivitySourceBuilder</code> instance
	 */

//	public abstract ActivitySourceBuilder getBuilder ();

	/**
	 * Get the <code>ActivitySourceManager</code> for the
	 * <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>ActivitySourceManager</code>
	 * @throws IllegalStateException If the <code>ActivitySource</code>
	 *                               interface is not available for the
	 *                               <code>DomainModel</code>
	 */

	public ActivitySourceManager getActivitySourceManager ()
	{
		return this.getManager (ActivitySource.class, ActivitySourceManager.class);
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivityTypeBuilder</code> instance
	 */

//	public abstract ActivityTypeBuilder getBuilder ();

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
	 * Get an instance of the <code>CourseBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>CourseBuilder</code> instance
	 */

//	public abstract CourseBuilder getBuilder ();

	/**
	 * Get the <code>CourseManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>CourseManager</code>
	 * @throws IllegalStateException If the <code>Course</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public CourseManager getCourseManager ()
	{
		return this.getManager (Course.class, CourseManager.class);
	}

	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>EnrolmentBuilder</code> instance
	 */

//	public abstract EnrolmentBuilder getBuilder ();

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
	 * Get an instance of the <code>LogEntryBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>LogEntryBuilder</code> instance
	 */

//	public abstract LogEntryBuilder getBuilder ();

	/**
	 * Get the <code>LogEntryManager</code> for the <code>DomainModel</code>.
	 *
	 * @return                       A reference to the
	 *                               <code>LogEntryManager</code>
	 * @throws IllegalStateException If the <code>LogEntry</code> interface is
	 *                               not available for the
	 *                               <code>DomainModel</code>
	 */

	public LogEntryManager getLogEntryManager ()
	{
		return this.getManager (LogEntry.class, LogEntryManager.class);
	}

	/**
	 * Get an instance of the <code>RoleBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>RoleBuilder</code> instance
	 */

//	public abstract RoleBuilder getBuilder ();

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
	 * Get an instance of the <code>UserBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>UserBuilder</code> instance
	 */

	public abstract UserBuilder getBuilder ();

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
