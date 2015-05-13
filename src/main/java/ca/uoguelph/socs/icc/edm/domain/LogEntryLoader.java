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

/**
 * Load <code>LogEntry</code> instances from the <code>DataStore</code>.  This
 * interface extends <code>ElementLoader</code> with the extra functionality
 * required to handle <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogEntryBuilder
 */

public interface LogEntryLoader extends ElementLoader<LogEntry>
{
	/**
	 * Retrieve a list of <code>LogEntry</code> objects, which are associated
	 * with the specified course, from the <code>DataStore</code>.
	 *
	 * @param  course The <code>Course</code> for which the list of
	 *                <code>LogEntry</code> objects should be retrieved, not
	 *                null
	 *
	 * @return        A list of <code>LogEntry</code> objects
	 */

	public abstract List<LogEntry> fetchAllforCourse (Course course);
}
