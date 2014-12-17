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

package ca.uoguelph.socs.icc.edm.datastore;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.ActionData;
import ca.uoguelph.socs.icc.edm.domain.ActivityInstance;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.CourseData;
import ca.uoguelph.socs.icc.edm.domain.UserEnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.GradedActivity;
import ca.uoguelph.socs.icc.edm.domain.LogData;
import ca.uoguelph.socs.icc.edm.domain.RoleData;
import ca.uoguelph.socs.icc.edm.domain.UserData;

public final class UserDatabaseFactory extends DatabaseFactory
{
	private static UserDatabaseFactory instance;

	static
	{
		UserDatabaseFactory.instance = null;
	}

	public UserDatabaseFactory getInstance ()
	{
		if (UserDatabaseFactory.instance ==  null)
		{
			UserDatabaseFactory.instance = new UserDatabaseFactory ();
		}

		return UserDatabaseFactory.instance;
	}

	private UserDatabaseFactory ()
	{
		super ("userdb");
	}

	protected void buildProfile (DomainModelBuilder builder)
	{
		builder.setEntry (Action.class, true, ActionData.class, NullIdGenerator.class);
		builder.setEntry (Activity.class, true, ActivityInstance.class, NullIdGenerator.class);
		builder.setEntry (ActivitySource.class, true, ActivitySourceData.class, NullIdGenerator.class);
		builder.setEntry (ActivityType.class, true, ActivityTypeData.class, NullIdGenerator.class);
		builder.setEntry (Course.class, true, CourseData.class, NullIdGenerator.class);
		builder.setEntry (Enrolment.class, true, UserEnrolmentData.class, NullIdGenerator.class);
		builder.setEntry (Grade.class, true, GradedActivity.class, NullIdGenerator.class);
		builder.setEntry (LogEntry.class, true, LogData.class, NullIdGenerator.class);
		builder.setEntry (Role.class, true, RoleData.class, NullIdGenerator.class);
		builder.setEntry (User.class, true, UserData.class, NullIdGenerator.class);
	}
}
