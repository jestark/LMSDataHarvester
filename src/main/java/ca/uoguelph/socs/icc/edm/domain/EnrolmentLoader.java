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
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Load <code>Enrolment</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Enrolment</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class EnrolmentLoader extends AbstractLoader<Enrolment>
{
	/**
	 * Get an instance of the <code>EnrolmentLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>EnrolmentLoader</code>
	 */

	public EnrolmentLoader getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return new EnrolmentLoader (model.getDataStore ());
	}

	/**
	 * Create the <code>EnrolmentLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public EnrolmentLoader (final DataStore datastore)
	{
		super (Enrolment.class, datastore);
	}

	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the
	 * <code>DataStore</code> for the specified <code>Role</code>.
	 *
	 * @param  role The <code>Role</code> for which the <code>List</code> of
	 *              <code>Enrolment</code> instances should be retrieved, not
	 *              null
	 * @return      A <code>List</code> of <code>Enrolment</code> instances
	 */

	public List<Enrolment> fetchAllForRole (final Role role)
	{
		this.log.trace ("fetchingAllForRole: role={}", role);

		if (role == null)
		{
			this.log.error ("The specified Role is NULL");
			throw new NullPointerException ();
		}

		Query<Enrolment> query = this.fetchQuery (Enrolment.Selectors.ROLE);
		query.setProperty (Enrolment.Properties.ROLE, role);

		return query.queryAll ();
	}
}
