/* Copyright (C) 2014. 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager;
import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 * <code>CourseManager</code> implementation for Moodle. This class decorates
 * the <code>DefaultCourseManager</code> and overrides the fetch methods to
 * add an additional step to load any associated pieces of activity data.  This
 * class works around limitations imposed by the by the Moodle database due to
 * it's lack of proper normalization.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     MoodleActivityManager
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager
 */


public final class MoodleCourseManager extends AbstractManager<Course> implements CourseManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>MoodleCourseManager</code>.
	 */

	private static final class MoodleCourseManagerFactory implements ManagerFactory<CourseManager>
	{
		/**
		 * Create an instance of the <code>MoodleCourseManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultCourseManager</code> will operate, not null
		 *
		 * @return       The <code>MoodleCourseManager</code>
		 */

		@Override
		public CourseManager create (final DataStore datastore)
		{
			return new MoodleCourseManager (datastore);
		}
	}

	/** The log */
	private final Logger log;

	/** Reference to the <code>DefaultCourseManager</code> */
	private final CourseManager manager;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (CourseManager.class, MoodleCourseManager.class, new MoodleCourseManagerFactory ());
	}

	/**
	 * Create the <code>MoodleCourseManager</code>.
	 *
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>DefaultCourseManager</code> will operate, not null
	 */

	public MoodleCourseManager (final DataStore datastore)
	{
		super (Course.class, datastore);

		this.log = LoggerFactory.getLogger (MoodleCourseManager.class);
		this.manager = new DefaultCourseManager (datastore);
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> interface, suitable for use
	 * with the <code>DataStore</code>.
	 *
	 * @return An <code>CourseBuilder</code> instance
	 */

	@Override
	public CourseBuilder getBuilder ()
	{
		return this.getBuilder (CourseBuilder.class);
	}

	/**
	 * Retrieve a <code>Course</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Course</code>.
	 *
	 * @param  course The <code>Course</code> to retrieve, not null
	 *
	 * @return        A reference to the <code>Course</code> in the
	 *                <code>DataStore</code>, may be null
	 */

	@Override
	public Course fetch (final Course course)
	{
		return this.manager.fetch (course);
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 *
	 * @return    The requested object.
	 */

	public Course fetchById (final Long id)
	{
		this.log.trace ("Fetch Course (with Activity Data) for ID: {}", id);

		Course course = this.manager.fetchById (id);

		return course;
	}

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	public List<Course> fetchAll ()
	{
		this.log.trace ("Fetch all Courses (with Activity Data)");

		List<Course> courses = this.manager.fetchAll ();

		return courses;
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
		this.log.trace ("Fetch all Courses (with Activity Data) offered in semester {} and year {}", semester, year);

		List<Course> courses = this.manager.fetchAllForOffering (semester, year);

		return courses;
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
		this.log.trace ("Fetch all Courses (with Activity Data) with name matching {} offered in semester {} and year {}", name, semester, year);

		List<Course> courses = this.manager.fetchAllForOffering (name, semester, year);

		return courses;
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
		this.log.trace ("Fetch Course (with Activity Data) with name {} offered in semester {} and year {}", name, semester, year);

		Course course = this.manager.fetchByOffering (name, semester, year);

		return course;
	}
}
