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

import java.util.List;
import java.util.Set;

/**
 * A representation of the content of a <code>Course</code>.  All of the
 * content in a <code>Course</code> is represented by a <code>Set</code> of
 * <code>Activity</code> instances.  <code>Activity</code> instances will exist
 * for assignments, quizzes, forums and lessons among many other things.  Some
 * of these will be graded and some will not be graded.  Graded
 * <code>Activity</code> instances will have <code>Grade</code> instances
 * associated with them.
 * <p>
 * Since the <code>Activity</code> interface represents all of the content of a
 * <code>Course</code>, its implementation can be complex.  Each instance of the
 * <code>Activity</code> interface must contain the <code>ActviityType</code>, the
 * associated <code>Course</code>, <code>Grade</code>, and <code>LogEntry</code>
 * instances.  Depending on the <code>ActivityType</code> an <code>Activity</code>
 * instance can contain additional data including, but not limited to,
 * sub-activities.  All sub-activities must implement the <code>Activity</code>
 * interface, but may return local data where necessary.
 * <p>
 * Instances of the <code>Activity</code> interface have strong dependencies on
 * the associated instances of the <code>ActivityType</code> and
 * <code>Course</code> interfaces.  If an instance of one of these interfaces is
 * deleted, then the associated instance of the <code>Activity</code> interface
 * and its dependants must me deleted as well.  Similarly, instances of the
 * <code>Grade</code> and <code>LogEntry</code> interfaces, along with any
 * sub-activities have a strong dependency on the associated instance of the
 * <code>Activity</code> interface, and must be deleted along with the instance
 * of the <code>Activity</code> interface.
 * <p>
 * Once created, with the exception of adding and removing <code>Grade</code>
 * and <code>LogEntry</code> instances (and sub-activities if they exist),
 * <code>Activity</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityBuilder
 * @see     ActivityManager
 */

public interface Activity extends Element
{
	/**
	 * Get the name of the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances have names.  For those <code>Activity</code> instances which do
	 * not have names, the name of the associated <code>ActivityType</code> will
	 * be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	public abstract String getName();

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public abstract ActivityType getType();

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse ();

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances are graded.  If the <code>Activity</code> does is not graded
	 * then the <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	public abstract Set<Grade> getGrades ();

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances which
	 * act upon the <code>Activity</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();
}
