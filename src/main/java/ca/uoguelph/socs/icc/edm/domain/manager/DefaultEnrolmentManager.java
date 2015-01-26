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

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentManager;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultEnrolmentManager extends AbstractManager<Enrolment> implements EnrolmentManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultEnrolmentManager</code>.
	 */

	private static final class DefaultEnrolmentManagerFactory implements ManagerFactory<EnrolmentManager>
	{
		/**
		 * Create an instance of the <code>DefaultEnrolmentManager</code>.
		 *
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultEnrolmentManager</code>
		 * @return       The <code>DefaultEnrolmentManager</code>
		 */

		@Override
		public EnrolmentManager create (DomainModel model)
		{
			return new DefaultEnrolmentManager (model);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		(EnrolmentFactory.getInstance ()).registerManager (DefaultEnrolmentManager.class, new DefaultEnrolmentManagerFactory ());
	}

	/** The logger */
	private final Logger log;

	/**
	 * Create the <code>DefaultEnrolmentManager</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>DefaultEnrolmentManager</code> is to be created, not
	 *               null
	 */

	public DefaultEnrolmentManager (DomainModel model)
	{
		super (Enrolment.class, model);

		this.log = LoggerFactory.getLogger (EnrolmentManager.class);
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
		this.log.trace ("Fetching all Enrolments with Role: {}", role);

		if (role == null)
		{
			this.log.error ("The specified Role is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("role", role);

		return (this.fetchQuery ()).queryAll ("role", params);
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
