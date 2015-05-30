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

public final class DefaultCourseBuilder extends AbstractBuilder<Course> implements CourseBuilder
{
	/** The name of the course */
	private String name;

	/** The <code>Semester</code> of offering */
	private Semester semester;

	/** The year of offering */
	private Integer year;

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
		super (Course.class, datastore);
	}

	@Override
	protected Course buildElement ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The course's name is not set");
			throw new IllegalStateException ("name not set");
		}

		if (this.semester == null)
		{
			this.log.error ("Can not build: The course's semester of offering is not set");
			throw new IllegalStateException ("semester not set");
		}

		if (this.year == null)
		{
			this.log.error ("Can not build: The course's year of offering is not set");
			throw new IllegalStateException ("year not set");
		}

		Course result = this.element;

		if ((this.element == null) || (! this.name.equals (this.element.getName ())) || (! this.semester.equals (this.element.getSemester ())) || (! this.year.equals (this.element.getYear ())))
		{
//			result = this.factory.create (this.name, this.semester, this.year);
		}

		return result;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.name = null;
		this.semester = null;
		this.year = null;
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
		this.log.trace ("Load Course: {}", course);

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
		return this.name;
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

	@Override
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
	 * @return This <code>CourseBuilder</code>
	 */

	@Override
	public CourseBuilder setSemester (final Semester semester)
	{
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

	@Override
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

	@Override
	public CourseBuilder setYear (final Integer year)
	{
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
