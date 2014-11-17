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

public final class CourseManager extends AbstractManager<Course>
{
	/**
	 * Create the Course manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this Course manager.
	 */

	protected CourseManager (DomainModel model)
	{
		super (model);
	}

	/**
	 * Retrieve a list of courses from the underlying datastore based on the
	 * time of offering.
	 *
	 * @param semester The Semester of offering
	 * @param year The year of offering
	 * @return A list of course objects.
	 */

	public List<Course> fetchAllForOffering (Semester semester, Integer year)
	{
		return null;
	}
	
	/**
	 * Retrieve a list of courses from the underlying datastore based on the 
	 * time of offering, and a regular expression matching the courses name.
	 *
	 * @param name The regular expression to match against the courses name.
	 * @param semester The Semester of offering
	 * @param year The year of offering
	 * @return A list of course objects.
	 */

	public List<Course> fetchAllForOffering (String name, Semester semester, Integer year)
	{
		return null;
	}
	
	/**
	 * Retrieve a course from the underlying datastore based on its name and 
	 * time of offering.
	 *
	 * @param name The name of the course
	 * @param semester The Semester of offering
	 * @param year The year of offering
	 * @return A single course object.
	 */

	public Course fetchByOffering (String name, Semester semester, Integer year)
	{
		return null;
	}
}
