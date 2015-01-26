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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;

import ca.uoguelph.socs.icc.edm.domain.factory.LogEntryFactory;

import ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager;
import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 * <code>LogEntryManager</code> implementation for Moodle. This class decorates
 * the <code>DefaultLogEntryManager</code> and overrides the fetch methods to
 * add an additional step to load any associated pieces of activity data.  This
 * class works around limitations imposed by the by the Moodle database due to
 * it's lack of proper normalization.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     MoodleActivityManager
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager
 */

public final class MoodleLogEntryManager extends AbstractManager<LogEntry> implements LogEntryManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>MoodleLogEntryManager</code>.
	 */

	private static final class MoodleLogEntryManagerFactory implements ManagerFactory<LogEntryManager>
	{
		/**
		 * Create an instance of the <code>MoodleLogEntryManager</code>.
		 *
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>MoodleLogEntryManager</code>
		 * @return       The <code>MoodleLogEntryManager</code>
		 */

		@Override
		public LogEntryManager create (DomainModel model)
		{
			return new MoodleLogEntryManager (model);
		}
	}

	/** The log */
	private final Logger log;

	/** Reference to the <code>DefaultLogEntryManager</code> */
	private final LogEntryManager manager;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		(LogEntryFactory.getInstance ()).registerManager (MoodleLogEntryManager.class, new MoodleLogEntryManagerFactory ());
	}

	/**
	 * Create the <code>MoodleLogEntryManager</code>.
	 *
	 * @param  model The <code>DomainModel</code> instance for this manager, not
	 *               null
	 */

	public MoodleLogEntryManager (DomainModel model)
	{
		super (LogEntry.class, model);

		this.log = LoggerFactory.getLogger (MoodleLogEntryManager.class);
		this.manager = new DefaultLogEntryManager (model);
	}

		/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	public LogEntry fetchById (Long id)
	{
		this.log.trace ("Fetch Log Entry (with activity data) with ID: {}", id);

		LogEntry entry = this.manager.fetchById (id);

		return entry;
	}

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	public List<LogEntry> fetchAll ()
	{
		this.log.trace ("Fetch all Log Entries (with activity data)");

		List<LogEntry> entries = this.manager.fetchAll ();

		return entries;
	}

	/**
	 * Retrieve a list of <code>LogEntry</code> objects, which are associated with
	 * the specified course, from the underlying data-store.
	 *
	 * @param  course The <code>Course</code> for which the list of
	 *                <code>LogEntry</code> objects should be retrieved, not null
	 * @return        A list of <code>LogEntry</code> objects
	 */

	public List<LogEntry> fetchAllforCourse (Course course)
	{
		this.log.trace ("Fetch all Log Entries (with activity data) for course: {}", course);

		List<LogEntry> entries = this.manager.fetchAllforCourse (course);

		return entries;
	}

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to insert into the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   inserted, <code>false</code> otherwise, not null
	 * @return           A reference to the inserted entity
	 */

	public LogEntry insert (LogEntry entity, Boolean recursive)
	{
		return this.manager.insert (entity, recursive);
	}

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to remove from the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   removed, <code>false</code> otherwise, not null
	 */

	public void remove (LogEntry entity, Boolean recursive)
	{
		this.manager.remove (entity, recursive);
	}

	/**
	 * Set the time on a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to modify, not null
	 * @param  time  The new value for the time
	 */

	public void setTime (LogEntry entry, Date time)
	{
		this.manager.setTime (entry, time);
	}

	/**
	 * Set the IP Address for a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry<code> to modify, not null
	 * @param  ip    The value for the new IP Address
	 */

	public void setIPAddress (LogEntry entry, String ip)
	{
		this.manager.setIPAddress (entry, ip);
	}
}
