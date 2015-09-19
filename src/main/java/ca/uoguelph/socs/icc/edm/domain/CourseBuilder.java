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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>Course</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Course</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Course
 */

public final class CourseBuilder implements Builder<Course>
{
	/** The Logger */
	private final Logger log;

	/** Helper to operate on <code>Course</code> instances*/
	private final DataStoreProxy<Course> courseProxy;

	/** The <code>DataStore</code> id number for the <code>Course</code> */
	private Long id;

	/** The name of the <code>Course</code> */
	private String name;

	/** The <code>Semester</code> of offering */
	private Semester semester;

	/** The year of offering */
	private Integer year;

	/**
	 * Create the <code>CourseBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected CourseBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.courseProxy = DataStoreProxy.getInstance (Course.class, Course.SELECTOR_OFFERING, datastore);

		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;
	}

	/**
	 * Create an instance of the <code>Course</code>.
	 *
	 * @return                       The new <code>Course</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Course build ()
	{
		this.log.trace ("build:");

		if (this.name == null)
		{
			this.log.error ("Attempting to create an Course without a name");
			throw new IllegalStateException ("name is NULL");
		}

		if (this.semester == null)
		{
			this.log.error ("Attempting to create an Course without a semester");
			throw new IllegalStateException ("semester is NULL");
		}

		if (this.year == null)
		{
			this.log.error ("Attempting to create an Course without a year");
			throw new IllegalStateException ("year is NULL");
		}

		Course result = this.courseProxy.create ();
		result.setId (this.id);
		result.setName (this.name);
		result.setSemester (this.semester);
		result.setYear (this.year);

		return this.courseProxy.insert (result);
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>CourseBuilder</code>
	 */

	public CourseBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;

		return this;
	}

	/**
	 * Load a <code>Course</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Course</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Course</code> instance to be
	 *                                  loaded are not valid
	 */

	public CourseBuilder load (final Course course)
	{
		this.log.trace ("load course={}", course);

		if (course == null)
		{
			this.log.error ("Attempting to load a NULL Course");
			throw new NullPointerException ();
		}

		this.id = course.getId ();
		this.setName (course.getName ());
		this.setSemester (course.getSemester ());
		this.setYear (course.getYear ());

		return this;
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Course</code>.
	 *
	 * @param  name                     The name of the <code>Course</code>,
	 *                                  not null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If the name is empty
	 */

	public CourseBuilder setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.name = name;

		return this;
	}

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public Semester getSemester ()
	{
		return this.semester;
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 *
	 * @return          This <code>CourseBuilder</code>
	 */

	public CourseBuilder setSemester (final Semester semester)
	{
		this.log.trace ("setSemester: semester={}", semester);

		if (semester == null)
		{
			this.log.error ("semester is NULL");
			throw new NullPointerException ("semester is NULL");
		}

		this.semester = semester;

		return this;
	}

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public Integer getYear ()
	{
		return this.year;
	}

	/**
	 * Set the year in which the <code>Course</code> was offered.
	 *
	 * @param  year                     The year of offering, not null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If the year is negative
	 */

	public CourseBuilder setYear (final Integer year)
	{
		this.log.trace ("setYear: year={}", year);

		if (year == null)
		{
			this.log.error ("year is NULL");
			throw new NullPointerException ("year is NULL");
		}

		if (year < 0)
		{
			this.log.error ("Year is negative");
			throw new IllegalArgumentException ("Year is negative");
		}

		this.year = year;

		return this;
	}
}
