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

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.factory.CourseFactory;

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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>MoodleCourseManager</code>
		 * @return       The <code>MoodleCourseManager</code>
		 */

		@Override
		public CourseManager create (DomainModel model)
		{
			return new MoodleCourseManager (model);
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
		(CourseFactory.getInstance ()).registerManager (MoodleCourseManager.class, new MoodleCourseManagerFactory ());
	}

	/**
	 * Create the <code>MoodleCourseManager</code>.
	 *
	 * @param  model The <code>DomainModel</code> instance for this manager, not
	 *               null
	 */

	public MoodleCourseManager (DomainModel model)
	{
		super (Course.class, model);

		this.log = LoggerFactory.getLogger (MoodleCourseManager.class);
		this.manager = new DefaultCourseManager (model);
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	public Course fetchById (Long id)
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

	public List<Course> fetchAllForOffering (Semester semester, Integer year)
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

	public List<Course> fetchAllForOffering (String name, Semester semester, Integer year)
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

	public Course fetchByOffering (String name, Semester semester, Integer year)
	{
		this.log.trace ("Fetch Course (with Activity Data) with name {} offered in semester {} and year {}", name, semester, year);

		Course course = this.manager.fetchByOffering (name, semester, year);

		return course;
	}

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to insert into the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   inserted, <code>false</code> otherwise, not null
	 * @return           A reference to the inserted entity
	 */

	public Course insert (Course entity, Boolean recursive)
	{
		return this.manager.insert (entity, recursive);
	}

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to remove from the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   removed, <code>false</code> otherwise, not null
	 */

	public void remove (Course entity, Boolean recursive)
	{
		this.manager.remove (entity, recursive);
	}
}
