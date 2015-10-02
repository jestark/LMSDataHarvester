/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.resolver;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class EnrolmentConverter
{
	/** Non-existent users should be mapped to this username */
	private static final String NULL_USERNAME = "@@NONE@@";

	/** The log */
	private final Logger log;

	/** cache of <code>User</code> instances */
	private final Map<Long, User> cache;

	/** <code>Query</code> for the source <code>DomainModel</code> */
	private final Query<User> sourceQuery;

	/** <code>Query</code> for the destination <code>DomainModel</code> */
	private final Query<User> destQuery;

	/**
	 * Create the <code>EnrolmentConverter</code>
	 *
	 * @param  dest   The destination <code>DomainModel</code>, not null
	 * @param  source The source <code>DomainModel</code>, not null
	 */

	public EnrolmentConverter (final DomainModel dest, final DomainModel source)
	{
		this.log = LoggerFactory.getLogger (EnrolmentConverter.class);

		if (dest == null)
		{
			throw new NullPointerException ("dest is NULL");
		}

		if (source == null)
		{
			throw new NullPointerException ("source is NULL");
		}

		this.sourceQuery = source.getQuery (User.class, User.SELECTOR_ID);
		this.destQuery = dest.getQuery (User.class, User.SELECTOR_USERNAME);

		this.cache = new HashMap<> ();
	}

	/**
	 * Retrieve the <code>Enrolment</code> from the destination
	 * <code>DomainModel</code>, which is associated with the specified user id
	 * number and <code>Course</code>.  The user id number should identify a
	 * <code>User</code> instance in the source <code>DomainModel</code>.
	 *
	 * @param  userId                The <code>DataStore</code> ID for the
	 *                               <code>User</code>, not null
	 * @param  course                The <code>Course</code>, not null
	 *
	 * @return                       The <code>Enrolment</code>, may be null
	 * @throws IllegalStateException if the <code>User</code> does not exist in
	 *                               the <code>DataStore</code>
	 * @throws IllegalStateException if the <code>User</code> does not have an
	 *                               <code>Enrolment</code> for the
	 *                               <code>Course</code>
	 */

	public Enrolment convert (final Long userId, final Course course)
	{
		this.log.trace ("convert: userId={}, course={}", userId, course);

		if (userId == null)
		{
			throw new NullPointerException ("id is NULL");
		}

		if (course == null)
		{
			throw new NullPointerException ("course is NULL");
		}

		if (! this.cache.containsKey (userId))
		{
			String username = EnrolmentConverter.NULL_USERNAME;

			if (userId > 0)
			{
				User sUser = this.sourceQuery.setValue (User.ID, userId)
					.query ();

				if (sUser == null)
				{
					this.log.error ("Source Domain Model does not contain a user with the ID: {}", userId);
					throw new IllegalArgumentException ("user does not exist");
				}
			}

			User user = this.destQuery.setValue (User.USERNAME, username)
				.query ();

			if (user == null)
			{
				this.log.error ("User does not exist in the destination database: {}", user);
				throw new IllegalStateException ("User does not exist in the destination database");
			}

			this.cache.put (userId, user);
		}

		return this.cache.get (userId)
			.getEnrolment (course);
	}
}
