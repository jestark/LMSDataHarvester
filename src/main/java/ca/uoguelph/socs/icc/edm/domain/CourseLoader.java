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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Load <code>Course</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Course</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class CourseLoader extends AbstractLoader<Course>
{
	/**
	 * Get an instance of the <code>CourseLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>CourseLoader</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	public static CourseLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, Course.class, CourseLoader::new);
	}

	/**
	 * Create the <code>CourseLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public CourseLoader (final DataStore datastore)
	{
		super (Course.class, datastore);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	public Course fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (Course.SELECTOR_ID)
			.setValue (Course.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<Course> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (Course.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve a course from the underlying data-store based on its name and
	 * time of offering.
	 *
	 * @param  name                  The name of the course, not null
	 * @param  semester              The <code>Semester</code> of offering, not
	 *                               null
	 * @param  year                  The year of offering, not null
	 *
	 * @return                       The <code>Course</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
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

		return this.getQuery (Course.SELECTOR_OFFERING)
			.setValue (Course.SEMESTER, semester)
			.setValue (Course.YEAR, year)
			.setValue (Course.NAME, name)
			.query ();
	}
}
