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
 * Factory interface to create new <code>Activity</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>Activity</code>
 * domain model interface.  It also provides the functionality required to set
 * the <code>DataStore</code> ID for the <code>Activity</code> instance, as
 * well as adding and removing any dependant <code>Grade</code> and
 * <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     NamedActivityElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityBuilder
 */

public interface ActivityElementFactory extends ElementFactory<Activity>
{
	/**
	 * Create a new <code>Activity</code> instance.
	 * 
	 * @param  type    The <code>ActivityType</code> of the <code>Activity</code>,
	 *                 not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  stealth Indicator if the <code>Activity</code> was added by the
	 *                 system, not null
	 *
	 * @return         The new <code>Activity</code> instance
	 */

	public abstract Activity create (ActivityType type, Course course, Boolean stealth);

	/**
	 * Add the instance specific data to the activity.  Note that the data to be
	 * added must not already be a part of another <code>Activity</code>, and the
	 * <code>Activity</code> must not already have data associated with it.
	 *
	 * @param  instance The <code>Activity</code> to which the data is to be added, not null
	 * @param  data     The data to add to the <code>Activity</code>, not null
	 */

	public abstract void setInstaceData (Activity instance, Activity data);

	/**
	 * Add the specified <code>Grade</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> to which the
	 *                  <code>Grade</code> is to be added, not null
	 * @param  grade    The <code>Grade</code> to add to the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added to the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean addGrade (Activity activity, Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> from which the
	 *                  <code>Grade</code> is to be removed, not null
	 * @param  grade    The <code>Grade</code> to remove from the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean removeGrade (Activity activity, Grade grade);

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
