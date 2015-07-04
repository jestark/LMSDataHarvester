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
		builder.setEntry (Action.class, ActionData.class, NullIdGenerator.class);
		builder.setEntry (Activity.class, ActivityInstance.class, NullIdGenerator.class);
		builder.setEntry (ActivitySource.class, ActivitySourceData.class, NullIdGenerator.class);
		builder.setEntry (ActivityType.class, ActivityTypeData.class, NullIdGenerator.class);
		builder.setEntry (Course.class, CourseData.class, NullIdGenerator.class);
		builder.setEntry (Enrolment.class, EnrolmentData.class, NullIdGenerator.class);
		builder.setEntry (Grade.class, GradedActivity.class, NullIdGenerator.class);
		builder.setEntry (LogEntry.class, MoodleLogData.class, NullIdGenerator.class);
		builder.setEntry (Role.class, RoleData.class, NullIdGenerator.class);
		builder.setEntry (User.class, UserData.class, NullIdGenerator.class);
	}
}
