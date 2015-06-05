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

package ca.uoguelph.socs.icc.edm.domain.database;

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

import ca.uoguelph.socs.icc.edm.domain.element.ActionData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.element.CourseData;
import ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;
import ca.uoguelph.socs.icc.edm.domain.element.GradedActivity;
import ca.uoguelph.socs.icc.edm.domain.element.LogData;
import ca.uoguelph.socs.icc.edm.domain.element.RoleData;
import ca.uoguelph.socs.icc.edm.domain.element.UserData;

import ca.uoguelph.socs.icc.edm.domain.loader.DefaultActionLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultActivityLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultActivitySourceLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultActivityTypeLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultCourseLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultEnrolmentLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultLogEntryLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultRoleLoader;
import ca.uoguelph.socs.icc.edm.domain.loader.DefaultUserLoader;

import  ca.uoguelph.socs.icc.edm.domain.DomainModelBuilder;
import  ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator;
import  ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator;

public final class UserDatabaseFactory extends DatabaseFactory
{
	private static UserDatabaseFactory instance;

	static
	{
		UserDatabaseFactory.instance = new UserDatabaseFactory ();
	}

	public static UserDatabaseFactory getInstance ()
	{
		return UserDatabaseFactory.instance;
	}

	private UserDatabaseFactory ()
	{
		super ("coursedb");
	}

	protected void buildProfile (DomainModelBuilder builder)
	{
		builder.setEntry (Action.class, true, ActionData.class, NullIdGenerator.class, DefaultActionLoader.class);
		builder.setEntry (Activity.class, true, GenericActivity.class, NullIdGenerator.class, DefaultActivityLoader.class);
		builder.setEntry (ActivitySource.class, true, ActivitySourceData.class, NullIdGenerator.class, DefaultActivitySourceLoader.class);
		builder.setEntry (ActivityType.class, true, ActivityTypeData.class, NullIdGenerator.class, DefaultActivityTypeLoader.class);
		builder.setEntry (Course.class, true, CourseData.class, NullIdGenerator.class, DefaultCourseLoader.class);
		builder.setEntry (Enrolment.class, true, EnrolmentData.class, RandomIdGenerator.class, DefaultEnrolmentLoader.class);
		builder.setEntry (Grade.class, true, GradedActivity.class, NullIdGenerator.class, null);
		builder.setEntry (LogEntry.class, true, LogData.class, NullIdGenerator.class, DefaultLogEntryLoader.class);
		builder.setEntry (Role.class, true, RoleData.class, NullIdGenerator.class, DefaultRoleLoader.class);
		builder.setEntry (User.class, true, UserData.class, NullIdGenerator.class, DefaultUserLoader.class);
	}
}
