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
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.CourseLoader;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>CourseLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultCourseLoader extends AbstractLoader<Course> implements CourseLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultCourseLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<CourseLoader>
	{
		/**
		 * Create an instance of the <code>DefaultCourseLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultCourseLoader</code> will operate,
		 *                   not null
		 *
		 * @return           The <code>DefaultCourseLoader</code>
		 */

		@Override
		public CourseLoader create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultCourseLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (CourseLoader.class, DefaultCourseLoader.class, new Factory ());
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

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("semester", semester);
		params.put ("year", year);

		return (this.fetchQuery ()).queryAll ("allsemester", params);
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

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("semester", semester);
		params.put ("year", year);
		params.put ("name", name);

		return (this.fetchQuery ()).queryAll ("alloffering", params);
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

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("semester", semester);
		params.put ("year", year);
		params.put ("name", name);

		return (this.fetchQuery ()).query ("offering", params);
	}
}
