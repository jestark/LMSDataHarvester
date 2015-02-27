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

/**
 * Manage <code>Enrolment</code> instances in the <code>DataStore</code>.  This
 * interface extends <code>ElementManager</code> with the extra functionality
 * required to handle <code>Enrolment</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     EnrolmentBuilder
 */

public interface EnrolmentManager extends ElementManager<Enrolment>
{
	/**
	 * Get an instance of the <code>EnrolmentBuilder</code> interface, suitable for use
	 * with the <code>DataStore</code>.
	 *
	 * @return An <code>EnrolmentBuilder</code> instance
	 */

	public abstract EnrolmentBuilder getBuilder ();

	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the
	 * <code>DataStore</code> for the specified <code>Role</code>.
	 *
	 * @param  role The <code>Role</code> for which the <code>List</code> of
	 *              <code>Enrolment</code> instances should be retrieved, not
	 *              null
	 * @return      A <code>List</code> of <code>Enrolment</code> instances
	 */

	public abstract List<Enrolment> fetchAllForRole (Role role);

	/**
	 * Add a grade, for the specified activity to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  activity  The <code>Activity</code> with which to associate the
	 *                   grade, not null
	 * @param  grade     The value for the grade, not null
	 */

	public abstract void addGrade (Enrolment enrolment, Activity activity, Integer grade);

	/**
	 * Add a grade to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to add, not null
	 */

	public abstract void addGrade (Enrolment enrolment, Grade grade);

	/**
	 * Remove a grade from an <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to remove, not null
	 */

	public abstract void removeGrade (Enrolment enrolment, Grade grade);
}
