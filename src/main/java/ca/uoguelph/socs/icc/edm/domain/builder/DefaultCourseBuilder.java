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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 * Default implementation of the <code>CourseBuilder</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultCourseBuilder extends AbstractBuilder<Course, CourseElementFactory> implements CourseBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultCourseBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Course, CourseBuilder>
	{
		/**
		 * Create the <code>CourseBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>CourseManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the
		 *                 <code>CourseManager</code> instance, not null
		 *
		 * @return         The <code>CourseBuilder</code>
		 */

		@Override
		public CourseBuilder create (final ManagerProxy<Course> manager)
		{
			return new DefaultCourseBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The name of the course */
	private String name;

	/** The <code>Semester</code> of offering */
	private Semester semester;

	/** The year of offering */
	private Integer year;

	/**
	 * static initializer to register the <code>DefaultCourseBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (CourseBuilder.class, DefaultCourseBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultCourseBuilder</code>.
	 *
	 * @param  manager The <code>CourseManager</code> which the
	 *                 <code>CourseBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>
	 */

	protected DefaultCourseBuilder (final ManagerProxy<Course> manager)
	{
		super (Course.class, CourseElementFactory.class, manager);

		this.log = LoggerFactory.getLogger (DefaultCourseBuilder.class);
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
			result = (getFactory (CourseElementFactory.class, this.manager.getElementImplClass (Course.class))).create (this.name, this.semester, this.year);
		}

		return result;
	}

	@Override
	protected void postInsert ()
	{
		// Course is a root element, so do nothing
	}

	@Override
	protected void postRemove ()
	{
		// Course is a root element, so do nothing
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
	 * @param  Course                   The <code>Course</code> to load into the
	 *                                  <code>CourseBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Course</code> instance to be loaded
	 *                                  are not valid
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
	 * @param  name                     The name of the <code>Course</code>, not
	 *                                  null
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
	 * Get the <code>Semester</code> in which the <code>Course</code> was offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was offered.
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
