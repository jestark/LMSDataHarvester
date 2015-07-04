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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Date;
import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Load <code>LogEntry</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class LogEntryLoader extends AbstractLoader<LogEntry>
{
	/**
	 * Get an instance of the <code>LogEntryLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>LogEntryLoader</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	public static LogEntryLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, LogEntry.class, LogEntryLoader::new);
	}

	/**
	 * Create the <code>LogEntryLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public LogEntryLoader (final DataStore datastore)
	{
		super (LogEntry.class, datastore);
	}

	/**
	 * Retrieve a list of <code>LogEntry</code> instances, which are associated
	 * with the specified <code>Course</code>, from the <code>DataStore</code>.
	 *
	 * @param  course                The <code>Course</code>, not null
	 *
	 * @return                       A <code>List</code> of
	 *                               <code>LogEntry</code> instances
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public List<LogEntry> fetchAllforCourse (final Course course)
	{
		this.log.trace ("fetchAllForCourse: course={}", course);

		if (course == null)
		{
			this.log.error ("The specified Course is NULL");
			throw new NullPointerException ();
		}

		if (! this.datastore.isOpen ())
		{
			this.log.error ("Attempting to Query a closed datastore");
			throw new IllegalStateException ("datastore is closed");
		}

		Query<LogEntry> query = this.fetchQuery (LogEntry.SELECTOR_COURSE);
		query.setProperty (LogEntry.COURSE, course);

		return query.queryAll ();
	}
}
