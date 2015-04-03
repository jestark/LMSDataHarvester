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

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;
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

	private static final class Factory implements ManagerFactory<LogEntryManager>
	{
		/**
		 * Create an instance of the <code>MoodleLogEntryManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *               <code>DefaultLogEntryManager</code> will operate, not
		 *               null
		 *
		 * @return       The <code>MoodleLogEntryManager</code>
		 */

		@Override
		public LogEntryManager create (final DataStore datastore)
		{
			return new MoodleLogEntryManager (datastore);
		}
	}

	/** Reference to the <code>DefaultLogEntryManager</code> */
	private final LogEntryManager manager;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (LogEntryManager.class, MoodleLogEntryManager.class, new Factory ());
	}

	/**
	 * Create the <code>MoodleLogEntryManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>LogEntryManager</code> will operate, not
	 *                   null
	 */

	public MoodleLogEntryManager (final DataStore datastore)
	{
		super (LogEntry.class, datastore);

		this.manager = new DefaultLogEntryManager (datastore);
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>LogEntryBuilder</code> instance
	 */

	@Override
	public LogEntryBuilder getBuilder ()
	{
		return this.getBuilder (LogEntryBuilder.class);
	}

	/**
	 * Retrieve an <code>LogEntry</code> from the <code>DataStore</code> based
	 * on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> idenifier of the
	 *            <code>LogEntry</code> to retrieve, not null
	 *
	 * @return    The requested <code>LogEntry</code>
	 */

	public LogEntry fetchById (final Long id)
	{
		this.log.trace ("Fetch Log Entry (with activity data) with ID: {}", id);

		LogEntry entry = this.manager.fetchById (id);

		return entry;
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>LogEntry</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public List<LogEntry> fetchAll ()
	{
		this.log.trace ("Fetch all Log Entries (with activity data)");

		List<LogEntry> entries = this.manager.fetchAll ();

		return entries;
	}

	/**
	 * Retrieve a list of <code>LogEntry</code> instances, which are associated
	 * with the specified <code>Course</code>, from the <code>DataStore</code>.
	 *
	 * @param  course The <code>Course</code> for which the <code>List</code>
	 *                of <code>LogEntry</code> instances should be retrieved,
	 *                not null
	 *
	 * @return        A <code>List</code> of <code>LogEntry</code> instances
	 */

	public List<LogEntry> fetchAllforCourse (final Course course)
	{
		this.log.trace ("FetchAllForCourse: course={}", course);

		List<LogEntry> entries = this.manager.fetchAllforCourse (course);

		return entries;
	}
}
