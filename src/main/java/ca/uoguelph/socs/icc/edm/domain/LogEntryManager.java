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

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;

/**
 *
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class LogEntryManager extends AbstractManager<LogEntry>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>LogEntryManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>LogEntryManager</code>is to be retrieved, not null
	 * @return       The <code>LogEntryManager</code> instance for the specified
	 *               domain model
	 * @see    DomainModel#getManager
	 */

	public static LogEntryManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (LogEntryManager.class);
	}

	/**
	 * Create the <code>LogEntryManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>LogEntryManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected LogEntryManager (DomainModel model)
	{
		super (model);

		this.log = LogFactory.getLog (LogEntryManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>LogEntryBuilder</code>
	 */

	public LogEntryBuilder getBuilder ()
	{
		return (LogEntryBuilder) this.fetchBuilder ();
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
		return null;
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
	 * Set the IP Address for a speccified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry<code> to modify, not null
	 * @param  ip    The value for the new IP Address
	 */

	public void setIPAddress (LogEntry entry, String ip)
	{
	}
}
