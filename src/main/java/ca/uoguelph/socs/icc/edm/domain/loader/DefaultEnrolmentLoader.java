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

import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentLoader;
import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Default implementation of the <code>EnrolmentLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultEnrolmentLoader extends AbstractLoader<Enrolment> implements EnrolmentLoader
{
	/**
	 * Static initializer to register the <code>EnrolmentLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (Enrolment.class, DefaultEnrolmentLoader::new);
	}

	/**
	 * Create the <code>DefaultEnrolmentLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>DefaultEnrolmentLoader</code> will operate,
	 *                   not null
	 */

	public DefaultEnrolmentLoader (final DataStore datastore)
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

	@Override
	public List<Enrolment> fetchAllForRole (final Role role)
	{
		this.log.trace ("fetchingAllForRole: role={}", role);

		if (role == null)
		{
			this.log.error ("The specified Role is NULL");
			throw new NullPointerException ();
		}

		Query<Enrolment> query = this.fetchQuery ("role");
		query.setProperty (Enrolment.Properties.ROLE, role);

		return query.queryAll ();
	}
}
