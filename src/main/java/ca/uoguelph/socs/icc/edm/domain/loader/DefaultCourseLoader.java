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

package ca.uoguelph.socs.icc.edm.domain.loader;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseLoader;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Default implementation of the <code>CourseLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultCourseLoader extends AbstractLoader<Course> implements CourseLoader
{
	/**
	 * Static initializer to register the <code>CourseLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (Course.class, DefaultCourseLoader::new);
	}

	/**
	 * Create the <code>CourseLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>CourseLoader</code> will operate, not null
	 */

	public DefaultCourseLoader (final DataStore datastore)
	{
		super (Course.class, datastore);
	}

	/**
	 *  Retrieve a <code>List</code> of <code>Course</code> instances from the
	 * <code>DataStore</code> based on the time of offering.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 *
	 * @return          A <code>List</code> of <code>Course</code> instances
	 */

	@Override
	public List<Course> fetchAllForOffering (final Semester semester, final Integer year)
	{
		this.log.trace ("fetchAllForOffering: semester={}, year={}", semester, year);

		if (semester == null)
		{
			this.log.error ("The specified semester is NULL");
			throw new NullPointerException ();
		}

		if (year == null)
		{
			this.log.error ("The specified year is NULL");
			throw new NullPointerException ();
		}

		Query<Course> query = this.fetchQuery ("allsemester");
		query.setParameter ("semester", semester);
		query.setParameter ("year", year);

		return query.queryAll ();
	}

	/**
	 * Retrieve a <code>List</code> of <code>Course</code> instances from the
	 * <code>DataStore</code> based on the time of offering, and a regular
	 * expression matching the courses name.
	 *
	 * @param  name     The regular expression to match against the name of the
	 *                  <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 *
	 * @return          A <code>List</code> of <code>Course</code> instances
	 */

	@Override
	public List<Course> fetchAllForOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("fetchAllForOffering: name={}, semester={}, year={}", name, semester, year);

		if (semester == null)
		{
			this.log.error ("The specified semester is NULL");
			throw new NullPointerException ();
		}

		if (year == null)
		{
			this.log.error ("The specified year is NULL");
			throw new NullPointerException ();
		}

		if (name == null)
		{
			this.log.error ("The specified Course name is NULL");
			throw new NullPointerException ();
		}

		Query<Course> query = this.fetchQuery ("alloffering");
		query.setParameter ("semester", semester);
		query.setParameter ("year", year);
		query.setParameter ("name", name);

		return query.queryAll ();
	}

	/**
	 * Retrieve a <code>Course</code> from the <code>DataStore</code> based on
	 * its name and time of offering.
	 *
	 * @param  name     The name of the <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 *
	 * @return          A single <code>Course</code> object
	 */

	@Override
	public Course fetchByOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("fetchAllForOffering: name={}, semester={}, year={}", name, semester, year);

		if (semester == null)
		{
			this.log.error ("The specified semester is NULL");
			throw new NullPointerException ();
		}

		if (year == null)
		{
			this.log.error ("The specified year is NULL");
			throw new NullPointerException ();
		}

		if (name == null)
		{
			this.log.error ("The specified Course name is NULL");
			throw new NullPointerException ();
		}

		Query<Course> query = this.fetchQuery ("offering");
		query.setParameter ("semester", semester);
		query.setParameter ("year", year);
		query.setParameter ("name", name);

		return query.query ();
	}
}
