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

package ca.uoguelph.socs.icc.edm.loader;

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Role;

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
	 * Create the <code>EnrolmentLoader</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public EnrolmentLoader (final DomainModel model)
	{
		super (Enrolment.class, model);
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

	public Enrolment fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (Enrolment.SELECTOR_ID)
			.setValue (Enrolment.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<Enrolment> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (Enrolment.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve a list of <code>Enrolment</code> objects from the
	 * <code>DataStore</code> for the specified <code>Role</code>.
	 *
	 * @param  role                  The <code>Role</code>, not null
	 *
	 * @return                       A <code>List</code> of
	 *                               <code>Enrolment</code> instances
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public List<Enrolment> fetchAllForRole (final Role role)
	{
		this.log.trace ("fetchingAllForRole: role={}", role);

		if (role == null)
		{
			this.log.error ("The specified Role is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (Enrolment.SELECTOR_ROLE)
			.setValue (Enrolment.ROLE, role)
			.queryAll ();
	}
}