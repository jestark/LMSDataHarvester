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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Semester;

/**
 * Factory interface to create new <code>Course</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>Course</code> domain
 * model interface.  It also provides the functionality required to set the
 * <code>DataStore</code> ID for the <code>Course</code> instance, as well as
 * adding and removing any associated <code>Activity</code> and
 * <code>Enrolment</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.CourseBuilder
 */

public interface CourseElementFactory extends ElementFactory<Course>
{
	/**
	 * Create a new <code>Course</code> instance.
	 *
	 * @param  name     The name of the <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 *
	 * @return          The new <code>Course</code> instance
	 */

	public abstract Course create (String name, Semester semester, Integer year);

	/**
	 * Add the specified <code>Activity</code> to the specified <code>Course</code>.
	 *
	 * @param  course    The <code>Course</code> to which the <code>Activity</code>
	 *                   is to be added, not null
	 * @param  enrolment The <code>Enrolment</code> to add to the
	 *                   <code>Course</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Activity</code> was
	 *                   successfully added to the <code>Course</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean addActivity (Activity activity);

	/**
	 * Remove the specified <code>Activity</code> from the specified <code>Course</code>.
	 *
	 * @param  course    The <code>Course</code> from which the <code>Activity</code>
	 *                   is to be removed, not null
	 * @param  enrolment The <code>Enrolment</code> to remove from the
	 *                   <code>Course</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Activity</code> was
	 *                   successfully removed from the <code>Course</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean removeActivity (Activity activity);

	/**
	 * Add the specified <code>Enrolment</code> to the specified <code>Course</code>.
	 *
	 * @param  course    The <code>Course</code> to which the <code>Enrolment</code>
	 *                   is to be added, not null
	 * @param  enrolment The <code>Enrolment</code> to add to the
	 *                   <code>User</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added to the <code>Course</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean addEnrolemnt (Enrolment enrolment);

	/**
	 * Remove the specified <code>Enrolment</code> from the specified
	 * <code>Course</code>.
	 *
	 * @param  course    The <code>Course</code> from which the
	 *                   <code>Enrolment</code> is to be removed, not null
	 * @param  enrolment The <code>Enrolment</code> to remove from the
	 *                   <code>User</code>, not null
	 *
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed from the <code>Course</code>,
	 *                   <code>False</code> otherwise
	 */

	public abstract boolean removeEnrolemnt (Enrolment enrolment);
}
