/* Copyright (C) 2014 James E. Stark
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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Course
 */

public final class CourseManager extends DomainModelManager<Course>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>CourseManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>CourseManager</code>is to be retrieved, not null
	 * @return       The <code>CourseManager</code> instance for the 
	 *               specified domain model
	 * @see    DomainModel#getManager
	 */

	public static CourseManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (CourseManager.class);
	}

	/**
	 * Create the <code>CourseManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>CourseManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected CourseManager (DomainModel model, DataStoreQuery<Course> query)
	{
		super (model, query);

		this.log = LogFactory.getLog (UserManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>CourseBuilder</code>
	 */

	public CourseBuilder getBuilder ()
	{
		return (CourseBuilder) this.builder;
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
		return null;
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
		return null;
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
		return null;
	}
}
