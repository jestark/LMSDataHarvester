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
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>CourseBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultCourseBuilder extends AbstractBuilder<Course, Course.Properties> implements CourseBuilder
{
	/**
	 * static initializer to register the <code>DefaultCourseBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultCourseBuilder.class, DefaultCourseBuilder::new);
	}

	/**
	 * Create the <code>DefaultCourseBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Course</code> instance will be inserted
	 */

	protected DefaultCourseBuilder (final DataStore datastore)
	{
		super (Course.Properties.class, datastore);
	}

	/**
	 * Load a <code>Course</code> instance into the <code>CourseBuilder</code>.
	 * This method resets the <code>CourseBuilder</code> and initializes all of
	 * its parameters from the specified <code>Course</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  course                   The <code>Course</code> to load into
	 *                                  the <code>CourseBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Course</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Course course)
	{
		this.log.trace ("load course={}", course);

		super.load (course);
		this.setName (course.getName ());
		this.setSemester (course.getSemester ());
		this.setYear (course.getYear ());
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	@Override
	public String getName ()
	{
		return this.getPropertyValue (Course.Properties.NAME);
	}

	/**
	 * Set the name of the <code>Course</code>.
	 *
	 * @param  name                     The name of the <code>Course</code>,
	 *                                  not null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
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

		this.setPropertyValue (Course.Properties.NAME, name);

		return this;
	}

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	@Override
	public Semester getSemester ()
	{
		return this.getPropertyValue (Course.Properties.SEMESTER);
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 *
	 * @return This <code>CourseBuilder</code>
	 */

	@Override
	public CourseBuilder setSemester (final Semester semester)
	{
		this.log.trace ("setSemester: semester={}", semester);

		if (semester == null)
		{
			this.log.error ("semester is NULL");
			throw new NullPointerException ("semester is NULL");
		}

		this.setPropertyValue (Course.Properties.SEMESTER, semester);

		return this;
	}

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	@Override
	public Integer getYear ()
	{
		return this.getPropertyValue (Course.Properties.YEAR);
	}

	/**
	 * Set the year in which the <code>Course</code> was offered.
	 *
	 * @param  year                     The year of offering, not null
	 *
	 * @return                          This <code>CourseBuilder</code>
	 * @throws IllegalArgumentException If the year is negative
	 */

	@Override
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

		this.setPropertyValue (Course.Properties.YEAR, year);

		return this;
	}
}
