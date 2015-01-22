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

import ca.uoguelph.socs.icc.edm.domain.core.ActionData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance;
import ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.core.CourseData;
import ca.uoguelph.socs.icc.edm.domain.core.EnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.core.GradedActivity;
import ca.uoguelph.socs.icc.edm.domain.core.LogData;
import ca.uoguelph.socs.icc.edm.domain.core.RoleData;
import ca.uoguelph.socs.icc.edm.domain.core.UserData;

import ca.uoguelph.socs.icc.edm.domain.DomainModelBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModelType;
import ca.uoguelph.socs.icc.edm.domain.idgenerator.NullIdGenerator;

public final class CourseDatabaseFactory extends DatabaseFactory
{
	private static CourseDatabaseFactory instance;

	static
	{
		CourseDatabaseFactory.instance = new CourseDatabaseFactory ();
	}

	public static CourseDatabaseFactory getInstance ()
	{
		return CourseDatabaseFactory.instance;
	}

	private CourseDatabaseFactory ()
	{
		super ("userdb");
	}

	protected void buildProfile (DomainModelBuilder builder)
	{
		builder.setEntry (DomainModelType.ACTION, true, ActionData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.ACTIVITY, true, ActivityInstance.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.ACTIVITYSOURCE, true, ActivitySourceData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.ACTIVITYTYPE, true, ActivityTypeData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.COURSE, true, CourseData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.ENROLMENT, true, EnrolmentData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.GRADE, true, GradedActivity.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.LOGENTRY, true, LogData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.ROLE, true, RoleData.class, NullIdGenerator.class);
		builder.setEntry (DomainModelType.USER, false, UserData.class, NullIdGenerator.class);
	}
}
