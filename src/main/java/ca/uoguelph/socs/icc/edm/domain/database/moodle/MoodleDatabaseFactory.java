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

import ca.uoguelph.socs.icc.edm.domain.element.ActionData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityInstance;
import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.element.CourseData;
import ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.element.GradedActivity;
import ca.uoguelph.socs.icc.edm.domain.element.RoleData;
import ca.uoguelph.socs.icc.edm.domain.element.UserData;

import ca.uoguelph.socs.icc.edm.domain.ActionLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityLoader;
import ca.uoguelph.socs.icc.edm.domain.CourseLoader;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentLoader;
import ca.uoguelph.socs.icc.edm.domain.LogEntryLoader;
import ca.uoguelph.socs.icc.edm.domain.RoleLoader;
import ca.uoguelph.socs.icc.edm.domain.UserLoader;

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
		builder.setEntry (Action.class, false, ActionData.class, NullIdGenerator.class, ActionLoader.class);
		builder.setEntry (Activity.class, true, ActivityInstance.class, NullIdGenerator.class, ActivityLoader.class);				// FIXME
		builder.setEntry (ActivitySource.class, false, ActivitySourceData.class, NullIdGenerator.class, ActivitySourceLoader.class);
		builder.setEntry (ActivityType.class, true, ActivityTypeData.class, NullIdGenerator.class, ActivityTypeLoader.class);
		builder.setEntry (Course.class, true, CourseData.class, NullIdGenerator.class, CourseLoader.class);							// FIXME
		builder.setEntry (Enrolment.class, true, EnrolmentData.class, NullIdGenerator.class, EnrolmentLoader.class);
		builder.setEntry (Grade.class, true, GradedActivity.class, NullIdGenerator.class, null);
		builder.setEntry (LogEntry.class, true, MoodleLogData.class, NullIdGenerator.class, LogEntryLoader.class);					// FIXME
		builder.setEntry (Role.class, true, RoleData.class, NullIdGenerator.class, RoleLoader.class);
		builder.setEntry (User.class, true, UserData.class, NullIdGenerator.class, UserLoader.class);
	}
}
