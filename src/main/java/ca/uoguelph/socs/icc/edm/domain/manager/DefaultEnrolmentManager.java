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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentManager;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

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

	private static final class Factory implements ManagerFactory<EnrolmentManager>
	{
		/**
		 * Create an instance of the <code>DefaultEnrolmentManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultEnrolmentManager</code> will operate, not
		 *                   null
		 * @return           The <code>DefaultEnrolmentManager</code>
		 */

		@Override
		public EnrolmentManager create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultEnrolmentManager (datastore);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (EnrolmentManager.class, DefaultEnrolmentManager.class, new Factory ());
	}

	/** The logger */
	private final Logger log;

	/**
	 * Create the <code>DefaultEnrolmentManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>DefaultEnrolmentManager</code> will operate, not
	 *                   null
	 */

	public DefaultEnrolmentManager (final DataStore datastore)
	{
		super (Enrolment.class, datastore);

		this.log = LoggerFactory.getLogger (EnrolmentManager.class);
	}

	/**
	 * Retrieve an <code>Enrolment</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to retrieve, not null
	 *
	 * @return           A reference to the <code>Enrolment</code> in the
	 *                   <code>DataStore</code>, may be null
	 */

	@Override
	public Enrolment fetch (final Enrolment enrolment)
	{
		this.log.trace ("Fetching Enrolment with the same identity as: {}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ();
		}

		Enrolment result = enrolment;

		if (! (this.fetchQuery ()).contains (enrolment))
		{
			result = null; // this.fetchByName (enrolment.getName ());
		}

		return result;
	}

	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the underlying
	 * data-store for the given role.
	 *
	 * @param  role The role for which the enrolments should be retrieved, not
	 *              null
	 * @return      A list of enrolment objects.
	 */

	public List<Enrolment> fetchAllForRole (final Role role)
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
	 * Add a grade, for the specified activity to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  activity  The <code>Activity</code> with which to associate the
	 *                   grade, not null
	 * @param  grade     The value for the grade, not null
	 */

	public void addGrade (final Enrolment enrolment, final Activity activity, final Integer grade)
	{
	}

	/**
	 * Add a grade to the <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to add, not null
	 */

	public void addGrade (final Enrolment enrolment, final Grade grade)
	{
	}

	/**
	 * Remove a grade from an <code>Enrolment</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> object to modify, not null
	 * @param  grade     The <code>Grade</code> to remove, not null
	 */

	public void removeGrade (final Enrolment enrolment, final Grade grade)
	{
	}
}
