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
 * @author James E. Stark
 * @version 1.0
 */

public interface EnrolmentManager extends Manager<Enrolment>
{
	/**
	 * Retrieve a list of enrolment objects from the underlying datastore for
	 * the given role.
	 *
	 * @param role The role for which the enrolments should be retrieved.
	 * @return A list of enrolment ojects.
	 */

	public abstract List<Enrolment> fetchAllForRole (Role role);
	
	/**
	 * Set the final grade for the specified enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The value for the final grade.
	 */

	public abstract void setFinalGrade (Enrolment enrolment, Integer grade);

	/**
	 * Remove the final grade from the specified enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 */

	public abstract void clearFinalGrade (Enrolment enrolment);

	/**
	 * Set the usable flag to the specified value on the given enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param usable The new value for the usable flag.
	 */

	public abstract void setUsable (Enrolment enrolment, Boolean usable);

	/**
	 * Add a grade, for the specified activity to the enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param activity The Activity with which to associate the grade.
	 * @param grade The value for the grade.
	 */

	public abstract void addGrade (Enrolment enrolment, Activity activity, Integer grade);
	
	/**
	 * Add a grade to the enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The grade to add.
	 */

	public abstract void addGrade (Enrolment enrolment, Grade grade);

	/**
	 * Remove a grade from an enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The grade to remove.
	 */

	public abstract void removeGrade (Enrolment enrolment, Grade grade);
}
