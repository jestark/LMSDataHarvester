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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Course
 */

public final class DefaultCourseManager extends AbstractManager<Course> implements CourseManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultCourseManager</code>.
	 */

	private static final class Factory implements ManagerFactory<CourseManager>
	{
		/**
		 * Create an instance of the <code>DefaultCourseManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultCourseManager</code> will operate, not null
		 * @return           The <code>DefaultCourseManager</code>
		 */

		@Override
		public CourseManager create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultCourseManager (datastore);
		}
	}

	/** The logger */
	private final Logger log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (CourseManager.class, DefaultCourseManager.class, new Factory ());
	}

	/**
	 * Create the <code>CourseManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>CourseManager</code> will operate, not null
	 */

	public DefaultCourseManager (final DataStore datastore)
	{
		super (Course.class, datastore);

		this.log = LoggerFactory.getLogger (CourseManager.class);
	}

	/**
	 * Retrieve a list of courses from the underlying data-store based on the
	 * time of offering.
	 *
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 * @return          A list of <code>Course</code> objects
	 */

	public List<Course> fetchAllForOffering (final Semester semester, final Integer year)
	{
		this.log.trace ("Fetching all courses offered in year {} and semester {}", year, semester);

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
	 * Retrieve a list of courses from the underlying data-store based on the
	 * time of offering, and a regular expression matching the courses name.
	 *
	 * @param  name     The regular expression to match against the courses name,
	 *                  not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 * @return          A list of <code>Course</code> objects
	 */

	public List<Course> fetchAllForOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("Fetching all courses offered in year {} and semester {}, with name {}", year, semester, name);

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
	 * Retrieve a course from the underlying data-store based on its name and
	 * time of offering.
	 *
	 * @param  name     The name of the course, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 * @return          A single <code>Course</code> object
	 */

	public Course fetchByOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("Fetching course: {} (offered in year {} and semester {})", name, year, semester);

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
