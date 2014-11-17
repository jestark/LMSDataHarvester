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

public final class EnrolmentManager extends Manager<Enrolment>
{
	/**
	 * Domain Model Type constant.  Used by the domain model to determine which
	 * implementation classes to use, and for instantiation of this manager.
	 */

	public static final DomainModelType TYPE = DomainModelType.ENROLMENT;

	/**
	 * Get an instance of the EnrolmentManager for the specified domain model.
	 *
	 * @param model The instance of the Domain model for which the EnrolmentManager
	 * is to be retrieved.
	 * @return The EnrolmentManager instance for the specified domain model.
	 * 
	 * @throws IllegalArguementException If the domain model is null.
	 */

	public static EnrolmentManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException ();
		}

		return (EnrolmentManager) model.getManager (EnrolmentManager.TYPE);
	}

	/**
	 * Create the Enrolment manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this Enrolment manager.
	 */

	protected EnrolmentManager (DomainModel model)
	{
		super (model, EnrolmentManager.TYPE);
	}

	/**
	 * Retrieve a list of enrolment objects from the underlying datastore for
	 * the given role.
	 *
	 * @param role The role for which the enrolments should be retrieved.
	 * @return A list of enrolment ojects.
	 */

	public List<Enrolment> fetchAllForRole (Role role)
	{
		return null;
	}
	
	/**
	 * Set the final grade for the specified enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The value for the final grade.
	 */

	public void setFinalGrade (Enrolment enrolment, Integer grade)
	{
	}

	/**
	 * Remove the final grade from the specified enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 */

	public void clearFinalGrade (Enrolment enrolment)
	{
	}

	/**
	 * Set the usable flag to the specified value on the given enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param usable The new value for the usable flag.
	 */

	public void setUsable (Enrolment enrolment, Boolean usable)
	{
	}

	/**
	 * Add a grade, for the specified activity to the enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param activity The Activity with which to associate the grade.
	 * @param grade The value for the grade.
	 */

	public void addGrade (Enrolment enrolment, Activity activity, Integer grade)
	{
	}
	
	/**
	 * Add a grade to the enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The grade to add.
	 */

	public void addGrade (Enrolment enrolment, Grade grade)
	{
	}

	/**
	 * Remove a grade from an enrolment.
	 *
	 * @param enrolment The enrolment object to modify.
	 * @param grade The grade to remove.
	 */

	public void removeGrade (Enrolment enrolment, Grade grade)
	{
	}
}
