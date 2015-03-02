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

/**
 * Create new <code>Course</code> instances.  This interface extends the
 * <code>ElementBuilder</code> interface with the functionality required to
 * create <code>Course</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface CourseBuilder extends ElementBuilder<Course>
{
	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public abstract String getName ();
	
	/**
	 * Set the name of the <code>Course</code>.
	 *
	 * @param  name                     The name of the <code>Course</code>, not
	 *                                  null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	public abstract CourseBuilder setName (String name);
	
	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public abstract Semester getSemester ();
	
	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was offered.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 *
	 * @return This <code>CourseBuilder</code>
	 */

	public abstract CourseBuilder setSemester (Semester semester);
	
	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public abstract Integer getYear ();
	
	/**
	 * Set the year in which the <code>Course</code> was offered.
	 *
	 * @param  year The year of offering, not null
	 *
	 * @return This <code>CourseBuilder</code>
	 */

	public abstract CourseBuilder setYear (Integer year);
}
