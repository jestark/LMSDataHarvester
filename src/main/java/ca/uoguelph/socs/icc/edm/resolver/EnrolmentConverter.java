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
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class EnrolmentConverter
{
	/** The username for the NULL (unknown) <code>User</code> */
	private static final String NULL_USER_USERNAME = "@@NONE@@";

	/** The first name for the null (unknown) <code>User</code> */
	private static final String NULL_USER_FIRSTNAME = "NULL";

	/** The last name for the null (unknown) <code>User</code> */
	private static final String NULL_USER_LASTNAME = "USER";

	/** The name of the <code>Role</code> to assign to unknown <code>user</code> instances */
	private static final String UNKNOWN_ROLE_NAME = "UNKNOWN";

	/** The log */
	private final Logger log;

	/** cache of <code>User</code> instances */
	private final Map<Long, User> cache;

	/** Role for unknown <code>Enrolment</code> instances */
	private final Role unknownRole;

	/** Builder to create missing <code>Enrolment</code> instances*/
	private final EnrolmentBuilder eBuilder;

	/** Builder to create missing <code>User</code> instances*/
	private final UserBuilder uBuilder;

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

		this.unknownRole = Role.builder (dest)
			.setName (EnrolmentConverter.UNKNOWN_ROLE_NAME)
			.build ();

		this.eBuilder = Enrolment.builder (dest);
		this.uBuilder = User.builder (dest);

		this.sourceQuery = source.getQuery (User.class, User.SELECTOR_ID);
		this.destQuery = dest.getQuery (User.class, User.SELECTOR_USERNAME);

		this.cache = new HashMap<> ();
	}

	private User nullUser (final Course course)
	{
		this.log.trace ("nullUser: course={}", course);

		assert course != null : "course is NULL";

		return this.uBuilder.clear ()
			.setFirstname (EnrolmentConverter.NULL_USER_FIRSTNAME)
			.setLastname (EnrolmentConverter.NULL_USER_LASTNAME)
			.setUsername (EnrolmentConverter.NULL_USER_USERNAME)
			.addEnrolment (this.eBuilder.clear ()
					.setRole (this.unknownRole)
					.setCourse (course)
					.setUsable (false)
					.build ())
			.build ();
	}

	private User importUser (final User user, final Course course)
	{
		this.log.trace ("importUser: User={}, course={}", user, course);

		assert user != null : "user is NULL";
		assert course != null : "course is NULL";

		return this.uBuilder.load (user)
			.addEnrolment (this.eBuilder.clear ()
					.setRole (this.unknownRole)
					.setCourse (course)
					.setUsable (false)
					.build ())
			.build ();
	}

	private User loadUser (final Long userId, final Course course)
	{
		this.log.trace ("loadUser: userId={}, course={}", userId, course);

		assert userId != null : "userId is NULL";
		assert course != null : "course is NULL";
		assert userId > 0 : "userId must be greater than 0";

		User result = null;

		User sUser = this.sourceQuery.setValue (User.ID, userId)
			.query ();

		if (sUser != null)
		{
			result = this.destQuery.setValue (User.USERNAME, sUser.getUsername ())
				.query ();

			if (result == null)
			{
				this.log.warn ("Creating Enrolment for non-existent user: {}", sUser);
				result = this.importUser (sUser, course);
			}
			else
			{
				this.log.debug ("Loaded enrolment for user: {}", result);
			}
		}
		else
		{
			this.log.warn ("Creating Enrolment for non-existent NULL user");
			result = this.nullUser (course);
		}

		return result;
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
			this.cache.put (userId, (userId > 0)
					? this.loadUser (userId, course)
					: this.nullUser (course));
		}

		return this.cache.get (userId)
			.getEnrolment (course);
	}
}
