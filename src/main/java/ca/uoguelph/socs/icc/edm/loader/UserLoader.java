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
import ca.uoguelph.socs.icc.edm.domain.User;

/**
 * Load <code>User</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>User</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class UserLoader extends AbstractLoader<User>
{
	/**
	 * Create the <code>UserLoader</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public UserLoader (final DomainModel model)
	{
		super (User.class, model);
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

	public User fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (User.SELECTOR_ID)
			.setValue (User.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<User> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (User.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified username,
	 * from the <code>DataStore</code>.
	 *
	 * @param  username              The username of the entry to retrieve, not
	 *                               null
	 *
	 * @return                       The <code>User</code> instance associated
	 *                               with the username
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public User fetchByUsername (final String username)
	{
		this.log.trace ("fetchByUsername: username={}", username);

		if (username == null)
		{
			this.log.error ("The specified user name is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (User.SELECTOR_USERNAME)
			.setValue (User.USERNAME, username)
			.query ();
	}

	/**
	 * Retrieve the <code>User</code> instance which is associated with the
	 * specified <code>Enrolment</code> from the <code>DataStore</code>.
	 *
	 * @param  enrolment             The <code>Enrolment</code> associated with
	 *                               the <code>User</code> instance to be
	 *                               retrieved, not null
	 *
	 * @return                       The <code>User</code> instance associated
	 *                               with the <code>Enrolment</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public User fetchByEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("fetchByEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified enrolment is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (User.SELECTOR_ENROLMENTS)
			.setValue (User.ENROLMENTS, enrolment)
			.query ();
	}
}
