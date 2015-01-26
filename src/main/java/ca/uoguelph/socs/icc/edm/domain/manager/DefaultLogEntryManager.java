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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.factory.LogEntryFactory;

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

	private static final class DefaultLogEntryManagerFactory implements ManagerFactory<LogEntryManager>
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
	private final Log log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		(LogEntryFactory.getInstance ()).registerManager (DefaultLogEntryManager.class, new DefaultLogEntryManagerFactory ());
	}

	/**
	 * Create the <code>LogEntryManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>LogEntryManager</code> is to be created, not null
	 */

	protected DefaultLogEntryManager (DomainModel model)
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
	 * Set the IP Address for a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry<code> to modify, not null
	 * @param  ip    The value for the new IP Address
	 */

	public void setIPAddress (LogEntry entry, String ip)
	{
	}
}
