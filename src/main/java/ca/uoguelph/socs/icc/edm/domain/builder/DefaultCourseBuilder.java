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

public final class DefaultCourseBuilder extends AbstractBuilder<Course> implements CourseBuilder
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

	protected DefaultCourseBuilder (final ManagerProxy<Course> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultCourseBuilder.class);
	}

	@Override
	public Course build ()
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

		return null; //this.factory.create (this.name, this.semester, this.year);
	}

	@Override
	public void clear ()
	{
		this.name = null;
		this.semester = null;
		this.year = null;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	@Override
	public CourseBuilder setName (final String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		this.name = name;

		return this;
	}

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

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

	@Override
	public Integer getYear ()
	{
		return this.year;
	}

	@Override
	public CourseBuilder setName (final Integer year)
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
