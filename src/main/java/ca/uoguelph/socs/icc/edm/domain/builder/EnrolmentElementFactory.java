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

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Factory interface to create new <code>Enrolment</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>Enrolment</code>
 * domain model interface.  It also provides the functionality required to set
 * the <code>DataStore</code> ID for the <code>Enrolment</code> instance, as
 * well as adding and removing any dependant <code>Grade</code> and
 * <code>LogEntry</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder
 */

public interface EnrolmentElementFactory extends ElementFactory<Enrolment>
{
	/**
	 * Create a new <code>Enrolment</code> instance.
	 *
	 * @param  user   The <code>User</code> enrolled in the <code>Course</code>,
	 *                not null
	 * @param  course The <code>Course</code> in which the <code>User</code> is
	 *                enrolled, not null
	 * @param  role   The <code>Role</code> of the <code>User</code> in the
	 *                <code>Course</code>, not null
	 * @param  grade  The final grade assigned to the <code>User</code> in the
	 *                <code>Course</code>
	 * @param  usable Indication if the <code>User</code> has consented to their
	 *                data being used for research
	 *
	 * @return        The new <code>Enrolment</code> instance
	 */

	public abstract Enrolment create (User user, Course course, Role role, Integer grade, Boolean usable);

	/**
	 * Add the specified <code>Grade</code> to the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to which the
	 *                   <code>Grade</code> is to be added, not null
	 * @param  grade     The <code>Grade</code> to add to the
	 *                   <code>Enrolment</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Grade</code> was
	 *                   successfully added to the <code>Enrolment</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean addGrade (Enrolment enrolment, Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> from which the
	 *                   <code>Grade</code> is to be removed, not null
	 * @param  grade     The <code>Grade</code> to remove from the
	 *                   <code>Enrolment</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Grade</code> was
	 *                   successfully removed from the <code>Enrolment</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean removeGrade (Enrolment enrolment, Grade grade);

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to which the
	 *                   <code>LogEntry</code> is to be added, not null
	 * @param  entry     The <code>LogEntry</code> to add to the
	 *                   <code>Enrolment</code>, not null
	 *
	 * @return           <code>True</code> if the <code>LogEntry</code> was
	 *                   successfully added to the <code>Enrolment</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean addLogEntry (Enrolment enrolment, LogEntry entry);

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> from which the
	 *                   <code>LogEntry</code> is to be removed, not null
	 * @param  entry     The <code>LogEntry</code> to remove from the
	 *                   <code>Enrolment</code>, not null
	 *
	 * @return           <code>True</code> if the <code>LogEntry</code> was
	 *                   successfully removed from the <code>Enrolment</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean removeLogEntry (Enrolment enrolment, LogEntry entry);
}
