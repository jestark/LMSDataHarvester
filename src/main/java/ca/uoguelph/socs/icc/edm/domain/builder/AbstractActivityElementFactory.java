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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

/**
 * Abstract factory interface for creating <code>Activity</code> instances.
 * This interface contains definitions for the methods that are common across
 * the <code>ElementFactory</code> implementations for <code>Activity</code>
 * and <code>ActivityGroupMember</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface AbstractActivityElementFactory extends ElementFactory<Activity>
{
	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> to which the
	 *                  <code>LogEntry</code> is to be added, not null
	 * @param  entry    The <code>LogEntry</code> to add to the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added to the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean addLogEntry (Activity activity, LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> from which the
	 *                  <code>LogEntry</code> is to be removed, not null
	 * @param  entry    The <code>LogEntry</code> to remove from the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed from the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean removeLogEntry (Activity activity, LogEntry entry);
}
