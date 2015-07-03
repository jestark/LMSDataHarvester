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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

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
	 * Get an instance of the <code>UserLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>UserLoader</code>
	 */

	public UserLoader getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return new UserLoader (model.getDataStore ());
	}

	/**
	 * Create the <code>DefaultUserLoader</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	public UserLoader (final DataStore datastore)
	{
		super (User.class, datastore);
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified ID
	 * number, from the <code>DataStore</code>.
	 *
	 * @param  idnumber The ID number of the <code>User</code> to retrieve, not
	 *                  null
	 *
	 * @return          The <code>User</code> object associated with the ID
	 *                  number
	 */

	public User fetchByIdNumber (final Integer idnumber)
	{
		this.log.trace ("fetchByIdNumber: idnumber={}", idnumber);

		if (idnumber == null)
		{
			this.log.error ("The specified User ID number is NULL");
			throw new NullPointerException ();
		}

		Query<User> query = this.fetchQuery (User.Selectors.IDNUMBER);
		query.setProperty (User.Properties.IDNUMBER, idnumber);

		return query.query ();
	}

	/**
	 * Retrieve a single <code>User</code> object, with the specified username,
	 * from the <code>DataStore</code>.
	 *
	 * @param  username The username of the entry to retrieve, not null
	 *
	 * @return          The <code>User</code> object associated with the
	 *                  username
	 */

	public User fetchByUsername (final String username)
	{
		this.log.trace ("fetchByUsername: username={}", username);

		if (username == null)
		{
			this.log.error ("The specified user name is NULL");
			throw new NullPointerException ();
		}

		Query<User> query = this.fetchQuery (User.Selectors.USERNAME);
		query.setProperty (User.Properties.USERNAME, username);

		return query.query ();
	}

	/**
	 * Retrieve the <code>User</code> instance which is associated with the
	 * specified <code>Enrolment</code> from the <code>DataStore</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> associated with the
	 *                   <code>User</code> instance to be retrieved, not null
	 *
	 * @return           The <code>User</code> instance associated with the
	 *                   <code>Enrolment</code>
	 */

	public User fetchByEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("fetchByEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified enrolment is NULL");
			throw new NullPointerException ();
		}

//		Query<User> query = this.fetchQuery ("enrolment");
//		query.setProperty (query.getProperty ("enrolment", Enrolment.class), enrolment);

		return null; // query.query ();
	}
}
