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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>LogEntryManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultLogEntryManager extends AbstractManager<LogEntry> implements LogEntryManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultLogEntryManager</code>.
	 */

	private static final class Factory implements ManagerFactory<LogEntryManager>
	{
		/**
		 * Create an instance of the <code>DefaultLogEntryManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultLogEntryManager</code> will operate,
		 *                   not null
		 * @return           The <code>DefaultLogEntryManager</code>
		 */

		@Override
		public LogEntryManager create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultLogEntryManager (datastore);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (LogEntryManager.class, DefaultLogEntryManager.class, new Factory ());
	}

	/**
	 * Create the <code>LogEntryManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>LogEntryManager</code> will operate, not null
	 */

	public DefaultLogEntryManager (final DataStore datastore)
	{
		super (LogEntry.class, datastore);
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
	 * Retrieve a <code>LogEntry</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to retrieve, not null
	 *
	 * @return        A reference to the <code>LogEntry</code> in the
	 *                <code>DataStore</code>, may be null
	 */

	@Override
	public LogEntry fetch (final LogEntry entry)
	{
		this.log.trace ("fetch: entry={}", entry);

		if (entry == null)
		{
			this.log.error ("The specified LogEntry is NULL");
			throw new NullPointerException ();
		}

		LogEntry result = entry;

		if (! (this.fetchQuery ()).contains (entry))
		{
			result = null; // this.fetchByName (entry.getName ());
		}

		return result;
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
		this.log.trace ("fetchAllForCourse: course={}", course);

		if (course == null)
		{
			this.log.error ("The specified Course is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("course", course);

		return (this.fetchQuery ()).queryAll ("course", params);
	}
}
