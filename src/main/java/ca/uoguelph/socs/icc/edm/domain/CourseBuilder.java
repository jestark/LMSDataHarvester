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

public final class CourseBuilder extends AbstractBuilder<Course>
{
	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>CourseBuilder</code> instance
	 */

	public static CourseBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new CourseBuilder (datastore, AbstractBuilder.getBuilder (datastore, datastore.getElementClass (Course.class)));
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Course</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  course    The <code>Course</code>, not null
	 *
	 * @return           The <code>CourseBuilder</code> instance
	 */

	public static CourseBuilder getInstance (final DataStore datastore, Course course)
	{
		assert datastore != null : "datastore is NULL";
		assert course != null : "course is NULL";

		CourseBuilder builder = CourseBuilder.getInstance (datastore);
		builder.load (course);

		return builder;
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>CourseBuilder</code> instance
	 */


	public static CourseBuilder getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return CourseBuilder.getInstance (model.getDataStore ());
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Course</code>.
	 *
	 * @param  model  The <code>DomainModel</code>, not null
	 * @param  course The <code>Course</code>, not null
	 *
	 * @return        The <code>CourseBuilder</code> instance
	 */

	public static CourseBuilder getInstance (final DomainModel model, Course course)
	{
		if (course == null)
		{
			throw new NullPointerException ("course is NULL");
		}

		CourseBuilder builder = CourseBuilder.getInstance (model);
		builder.load (course);

		return builder;
	}

	/**
	 * Create the <code>CourseBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected CourseBuilder (final DataStore datastore, final Builder<Course> builder)
	{
		super (datastore, builder);
	}

	@Override
	public void load (final Course course)
	{
		this.log.trace ("load course={}", course);

		if (course == null)
		{
			this.log.error ("Attempting to load a NULL Course");
			throw new NullPointerException ();
		}

		super.load (course);
		this.setName (course.getName ());
		this.setSemester (course.getSemester ());
		this.setYear (course.getYear ());

		this.builder.setProperty (Course.Properties.ID, course.getId ());
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Course.Properties.NAME);
	}

	/**
	 * Set the name of the <code>Course</code>.
	 *
	 * @param  name                     The name of the <code>Course</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
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

		this.builder.setProperty (Course.Properties.NAME, name);
	}

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	public Semester getSemester ()
	{
		return this.builder.getPropertyValue (Course.Properties.SEMESTER);
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 */

	public void setSemester (final Semester semester)
	{
		this.log.trace ("setSemester: semester={}", semester);

		if (semester == null)
		{
			this.log.error ("semester is NULL");
			throw new NullPointerException ("semester is NULL");
		}

		this.builder.setProperty (Course.Properties.SEMESTER, semester);
	}

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	public Integer getYear ()
	{
		return this.builder.getPropertyValue (Course.Properties.YEAR);
	}

	/**
	 * Set the year in which the <code>Course</code> was offered.
	 *
	 * @param  year                     The year of offering, not null
	 *
	 * @throws IllegalArgumentException If the year is negative
	 */

	public void setYear (final Integer year)
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

		this.builder.setProperty (Course.Properties.YEAR, year);
	}
}
