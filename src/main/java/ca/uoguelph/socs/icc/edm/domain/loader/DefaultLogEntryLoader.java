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

package ca.uoguelph.socs.icc.edm.domain.loader;

import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>LogEntryLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultLogEntryLoader extends AbstractLoader<LogEntry> implements LogEntryLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultLogEntryLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<LogEntryLoader>
	{
		/**
		 * Create an instance of the <code>DefaultLogEntryLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultLogEntryLoader</code> will operate,
		 *                   not null
		 * @return           The <code>DefaultLogEntryLoader</code>
		 */

		@Override
		public LogEntryLoader create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultLogEntryLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (LogEntryLoader.class, DefaultLogEntryLoader.class, new Factory ());
	}

	/**
	 * Create the <code>LogEntryLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>LogEntryLoader</code> will operate, not null
	 */

	public DefaultLogEntryLoader (final DataStore datastore)
	{
		super (LogEntry.class, datastore);
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
