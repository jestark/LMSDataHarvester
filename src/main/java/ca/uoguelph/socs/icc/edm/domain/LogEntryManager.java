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

public final class LogEntryManager extends AbstractManager<LogEntry>
{
	/**
	 * Create the LogEntry manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this LogEntry manager.
	 */

	protected LogEntryManager (DomainModel model)
	{
		super (model);
	}

	/**
	 * Retrieve a list of log entries, which are associated with the specified
	 * course, from the underlying datastore.
	 *
	 * @param course The course for which the log enties should be retrieved.
	 * @return A list of LogEntry objects.
	 */

	public List<LogEntry> fetchAllforCourse (Course course)
	{
		return null;
	}

	/**
	 * Set the time on a specified LogEntry.
	 *
	 * @param entry The LogEntry to modify.
	 * @param time the new value for the time.
	 */

	public void setTime (LogEntry entry, Date time)
	{
	}

	/**
	 * Set the IP Address for a speccified LogEntry.
	 *
	 * @param entry The LogEntry to modify.
	 * @param ip The value for the new IP Address.
	 */

	public void setIPAddress (LogEntry entry, String ip)
	{
	}
}
