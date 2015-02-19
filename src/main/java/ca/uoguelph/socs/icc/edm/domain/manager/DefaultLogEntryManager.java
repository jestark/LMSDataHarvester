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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 *
 *
 * @author James E. Stark
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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultLogEntryManager</code>
		 * @return       The <code>DefaultLogEntryManager</code>
		 */

		@Override
		public LogEntryManager create (DomainModel model)
		{
			return new DefaultLogEntryManager (model);
		}
	}

	/** The logger */
	private final Logger log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (LogEntry.class, LogEntryManager.class, DefaultLogEntryManager.class, new Factory ());
	}

	/**
	 * Create the <code>LogEntryManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>LogEntryManager</code> is to be created, not null
	 */

	public DefaultLogEntryManager (DomainModel model)
	{
		super (LogEntry.class, model);

		this.log = LoggerFactory.getLogger (LogEntryManager.class);
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
		this.log.trace ("Fetching all Log Entries for course: {}", course);

		if (course == null)
		{
			this.log.error ("The specified Course is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("course", course);

		return (this.fetchQuery ()).queryAll ("course", params);
	}

	/**
	 * Set the time on a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to modify, not null
	 * @param  time  The new value for the time
	 */

	public void setTime (LogEntry entry, Date time)
	{
	}

	/**
	 * Set the IP Address for a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry<code> to modify, not null
	 * @param  ip    The value for the new IP Address
	 */

	public void setIPAddress (LogEntry entry, String ip)
	{
	}
}
