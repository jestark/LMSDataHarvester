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

import java.util.List;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface EnrolmentManager extends ElementManager<Enrolment>
{
	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the underlying
	 * data-store for the given role.
	 *
	 * @param  role The role for which the enrolments should be retrieved, not
	 *              null
	 * @return      A list of enrolment objects.
	 */

	public abstract List<Enrolment> fetchAllForRole (Role role);

	/**
	 * Set the final grade for the specified <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The value for the final grade, not null
	 */

	public abstract void setFinalGrade (Enrolment enrolment, Integer grade);

	/**
	 * Remove the final grade from the specified <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 */

	public abstract void clearFinalGrade (Enrolment enrolment);

	/**
	 * Set the usable flag to the specified value on the given
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  usable    The new value for the usable flag, not null
	 */

	public abstract void setUsable (Enrolment enrolment, Boolean usable);

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
