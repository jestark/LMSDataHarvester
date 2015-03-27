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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

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

import ca.uoguelph.socs.icc.edm.domain.core.ActionData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance;
import ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.core.CourseData;
import ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.core.GradedActivity;
import ca.uoguelph.socs.icc.edm.domain.core.LogData;
import ca.uoguelph.socs.icc.edm.domain.core.RoleData;
import ca.uoguelph.socs.icc.edm.domain.core.UserData;

import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityTypeManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultRoleManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultUserManager;

import ca.uoguelph.socs.icc.edm.domain.DomainModelBuilder;
import ca.uoguelph.socs.icc.edm.domain.database.DatabaseFactory;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator;

public final class MoodleDatabaseFactory extends DatabaseFactory
{
	private static MoodleDatabaseFactory instance;

	static
	{
		MoodleDatabaseFactory.instance = new MoodleDatabaseFactory ();
	}

	public static MoodleDatabaseFactory getInstance()
	{
		return MoodleDatabaseFactory.instance;
	}

	private MoodleDatabaseFactory ()
	{
		super ("moodledb");
	}

	protected void buildProfile (DomainModelBuilder builder)
	{
		builder.setEntry (Action.class, false, ActionData.class, NullIdGenerator.class, DefaultActionManager.class);
		builder.setEntry (Activity.class, true, ActivityInstance.class, NullIdGenerator.class, MoodleActivityManager.class);
		builder.setEntry (ActivitySource.class, false, ActivitySourceData.class, NullIdGenerator.class, DefaultActivitySourceManager.class);
		builder.setEntry (ActivityType.class, true, ActivityTypeData.class, NullIdGenerator.class, DefaultActivityTypeManager.class);
		builder.setEntry (Course.class, true, CourseData.class, NullIdGenerator.class, MoodleCourseManager.class);
		builder.setEntry (Enrolment.class, true, UserEnrolmentData.class, NullIdGenerator.class, DefaultEnrolmentManager.class);
		builder.setEntry (Grade.class, true, GradedActivity.class, NullIdGenerator.class, null);
		builder.setEntry (LogEntry.class, true, LogData.class, NullIdGenerator.class, DefaultLogEntryManager.class);
		builder.setEntry (Role.class, true, RoleData.class, NullIdGenerator.class, DefaultRoleManager.class);
		builder.setEntry (User.class, true, UserData.class, NullIdGenerator.class, DefaultUserManager.class);
	}
}
