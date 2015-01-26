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

/**
 *
 *
 * @author James E. Stark
 * @version 1.0
 */

public interface LogEntryManager extends ElementManager<LogEntry>
{
	/**
	 * Retrieve a list of <code>LogEntry</code> objects, which are associated with
	 * the specified course, from the underlying data-store.
	 *
	 * @param  course The <code>Course</code> for which the list of
	 *                <code>LogEntry</code> objects should be retrieved, not null
	 * @return        A list of <code>LogEntry</code> objects
	 */

	public List<LogEntry> fetchAllforCourse (Course course);

	/**
	 * Set the time on a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry</code> to modify, not null
	 * @param  time  The new value for the time
	 */

	public void setTime (LogEntry entry, Date time);

	/**
	 * Set the IP Address for a specified <code>LogEntry</code>.
	 *
	 * @param  entry The <code>LogEntry<code> to modify, not null
	 * @param  ip    The value for the new IP Address
	 */

	public void setIPAddress (LogEntry entry, String ip);
}
