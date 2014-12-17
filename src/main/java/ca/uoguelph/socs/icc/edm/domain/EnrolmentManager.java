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
 * @see     Enrolment
 * @see     Grade
 */

public final class EnrolmentManager extends AbstractManager<Enrolment>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>EnrolmentManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>EnrolmentManager</code>is to be retrieved, not null
	 * @return       The <code>CourseManager</code> instance for the
	 *               specified domain model
	 * @see    DomainModel#getManager
	 */

	public static EnrolmentManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (EnrolmentManager.class);
	}

	/**
	 * Create the <code>EnrolmentManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>EnrolmentManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected EnrolmentManager (DomainModel model)
	{
		super (model);

		this.log = LogFactory.getLog (EnrolmentManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>EnrolmentBuilder</code>
	 */

	public EnrolmentBuilder getBuilder ()
	{
		return (EnrolmentBuilder) this.fetchBuilder ();
	}

	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the underlying
	 * data-store for the given role.
	 *
	 * @param  role The role for which the enrolments should be retrieved, not
	 *              null
	 * @return      A list of enrolment objects.
	 */

	public List<Enrolment> fetchAllForRole (Role role)
	{
		return null;
	}

	/**
	 * Set the final grade for the specified <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The value for the final grade, not null
	 */

	public void setFinalGrade (Enrolment enrolment, Integer grade)
	{
	}

	/**
	 * Remove the final grade from the specified <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 */

	public void clearFinalGrade (Enrolment enrolment)
	{
	}

	/**
	 * Set the usable flag to the specified value on the given
	 * <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  usable    The new value for the usable flag, not null
	 */

	public void setUsable (Enrolment enrolment, Boolean usable)
	{
	}

	/**
	 * Add a grade, for the specified activity to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  activity  The <code>Activity</code> with which to associate the
	 *                   grade, not null
	 * @param  grade     The value for the grade, not null
	 */

	public void addGrade (Enrolment enrolment, Activity activity, Integer grade)
	{
	}

	/**
	 * Add a grade to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to add, not null
	 */

	public void addGrade (Enrolment enrolment, Grade grade)
	{
	}

	/**
	 * Remove a grade from an <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to remove, not null
	 */

	public void removeGrade (Enrolment enrolment, Grade grade)
	{
	}
}
