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

		Query<Course> query = this.fetchQuery (Course.Selectors.OFFERING);
		query.setProperty (Course.Properties.SEMESTER, semester);
		query.setProperty (Course.Properties.YEAR, year);
		query.setProperty (Course.Properties.NAME, name);

		return query.query ();
	}
}
