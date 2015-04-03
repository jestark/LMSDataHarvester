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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
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

	private static final class Factory implements ManagerFactory<CourseManager>
	{
		/**
		 * Create an instance of the <code>MoodleCourseManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultCourseManager</code> will operate,
		 *                   not null
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
		AbstractManager.registerManager (CourseManager.class, MoodleCourseManager.class, new Factory ());
	}

	/**
	 * Create the <code>MoodleCourseManager</code>.
	 *
	 * @param  datastore The <code>DataStore</code> upon which the
	 *                   <code>DefaultCourseManager</code> will operate, not
	 *                   null
	 */

	public MoodleCourseManager (final DataStore datastore)
	{
		super (Course.class, datastore);

		this.log = LoggerFactory.getLogger (MoodleCourseManager.class);
		this.manager = new DefaultCourseManager (datastore);
	}

	protected Course processCourse (final Course course)
	{
		this.log.trace ("processCourse: course={}", course);

		assert course != null : "course is NULL";

		ActivityManager manager = this.getManager (Activity.class, ActivityManager.class);

		assert manager instanceof MoodleActivityManager : "MoodleCourseManager MUST be used with MoodleActivityManager";

		((MoodleActivityManager) manager).processActivities (course.getActivities ());

		return course;
	}

	protected List<Course> processCourses (final List<Course> courses)
	{
		this.log.trace ("processCourses: courses={}", courses);

		assert courses != null : "courses is NULL";

		for (Course course : courses)
		{
			this.processCourse (course);
		}

		return courses;
	}

	/**
	 * Get an instance of the <code>CourseBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>CourseBuilder</code> instance
	 */

	@Override
	public CourseBuilder getBuilder ()
	{
		return this.getBuilder (CourseBuilder.class);
	}

	/**
	 * Retrieve a <code>Course</code> from the <code>DataStore</code> based on
	 * its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Course</code> to retrieve, not null
	 *
	 * @return    The requested <code>Course</code>
	 */

	public Course fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		return this.processCourse (this.manager.fetchById (id));
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Course</code> instances
	 * from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Course</code> instances
	 */

	public List<Course> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.processCourses (this.manager.fetchAll ());
	}

	/**
	 * Retrieve a <code>List</code> of <code>Course</code> instances from the
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

		return this.processCourses (this.manager.fetchAllForOffering (semester, year));
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
	 * @return          A <code>List</code> of <code>Course</code> instances
	 */

	public List<Course> fetchAllForOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("fetchAllForOffering: name={}, semester={}, year={}", name, semester, year);

		return this.processCourses (this.manager.fetchAllForOffering (name, semester, year));
	}

	/**
	 * Retrieve a <code>Course</code> from the <code>DataStore</code> based on
	 * its name and time of offering.
	 *
	 * @param  name     The name of the <code>Course</code>, not null
	 * @param  semester The <code>Semester</code> of offering, not null
	 * @param  year     The year of offering, not null
	 * @return          The requested <code>Course</code>
	 */

	public Course fetchByOffering (final String name, final Semester semester, final Integer year)
	{
		this.log.trace ("FetchByOffering: name={}, semester={}, year={}", name, semester, year);

		return this.processCourse (this.manager.fetchByOffering (name, semester, year));
	}
}
