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

package ca.uoguelph.socs.icc.edm.loader;

import java.util.Date;
import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

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
	 * Create the <code>LogEntryLoader</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public LogEntryLoader (final DomainModel model)
	{
		super (LogEntry.class, model);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	public LogEntry fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (LogEntry.SELECTOR_ID)
			.setValue (LogEntry.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<LogEntry> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (LogEntry.SELECTOR_ALL)
			.queryAll ();
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

		return this.getQuery (LogEntry.SELECTOR_COURSE)
			.setValue (LogEntry.COURSE, course)
			.queryAll ();
	}
}
