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

package ca.uoguelph.socs.icc.edm;

import java.io.File;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.ParentActivity;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.Semester;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.element.ActionData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityReferenceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.element.CourseData;
import ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;
import ca.uoguelph.socs.icc.edm.domain.element.GradeData;
import ca.uoguelph.socs.icc.edm.domain.element.LogData;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleActivityReference;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleActivityType;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;
import ca.uoguelph.socs.icc.edm.domain.element.NetworkData;
import ca.uoguelph.socs.icc.edm.domain.element.RoleData;
import ca.uoguelph.socs.icc.edm.domain.element.UserData;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Book;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.BookChapter;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Forum;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumDiscussion;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumPost;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Lesson;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.LessonPage;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Workshop;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmission;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.InsertProcessor;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.datastore.jpa.JPADataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.memory.MemDataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator;
import ca.uoguelph.socs.icc.edm.moodle.ActionMatcher;
import ca.uoguelph.socs.icc.edm.moodle.Extractor;

public class App
{
	/** The URL of the source database */
	public static final String SOURCE_URL;

	/** The username for the source database */
	public static final String SOURCE_USERNAME;

	/** The password for the source database */
	public static final String SOURCE_PASSWORD;

	/** The URL of the destination database */
	public static final String DEST_URL;

	/** The username for the destination database */
	public static final String DEST_USERNAME;

	/** The password for the destination database */
	public static final String DEST_PASSWORD;

	/** The name of the file holding all of the instructor registrations */
	public static final String INSTRUCTOR_LIST;

	/** The name of the file holding all of the TA registrations */
	public static final String TA_LIST;

	/** The name of the file holding all of the student registrations */
	public static final String STUDENT_LIST;

	static
	{
		SOURCE_URL = "jdbc:postgresql://DATABASE_HOST/DATABASE";
		SOURCE_USERNAME = "";
		SOURCE_PASSWORD = "";

		DEST_URL = "jdbc:postgresql://DATABASE_HOST/DATABASE";
		DEST_USERNAME = "PASSWORD";
		DEST_PASSWORD = "PASSWORD";

		INSTRUCTOR_LIST = "INSTRUCTORLIST.csv";
		TA_LIST = "TALIST.csv";
		STUDENT_LIST = "STUDENTLIST.csv";
	}

    public static void main(final String[] args) throws Exception
    {
		final Profile destProfile = Profile.builder ()
				.setName ("coursedb")
				.setMutable (true)
				.setParameter ("javax.persistence.jdbc.url", DEST_URL)
				.setParameter ("javax.persistence.jdbc.user", DEST_USERNAME)
				.setParameter ("javax.persistence.jdbc.password", DEST_PASSWORD)
				.setElement (Action.class, ActionData.class)
				.setElement (Activity.class, GenericActivity.class)
				.setElement (ActivityReference.class, ActivityReferenceData.class)
				.setElement (ActivitySource.class, ActivitySourceData.class)
				.setElement (ActivityType.class, ActivityTypeData.class)
				.setElement (Course.class, CourseData.class)
				.setElement (Enrolment.class, EnrolmentData.class)
				.setElement (Grade.class, GradeData.class)
				.setElement (LogEntry.class, LogData.class)
				.setElement (Network.class, NetworkData.class)
				.setElement (Role.class, RoleData.class)
				.setElement (User.class, UserData.class)
				.setGenerator (Element.class, NullIdGenerator.class)
				.build ();

		final Profile sourceProfile = Profile.builder ()
				.setName ("moodledb")
				.setParameter ("javax.persistence.jdbc.url", SOURCE_URL)
				.setParameter ("javax.persistence.jdbc.user", SOURCE_USERNAME)
				.setParameter ("javax.persistence.jdbc.password", SOURCE_PASSWORD)
				.setElement (ActivityReference.class, MoodleActivityReference.class)
				.setElement (ActivityType.class, ActivityTypeData.class)
				.setElement (Course.class, CourseData.class)
				.setElement (Enrolment.class, EnrolmentData.class)
				.setElement (LogEntry.class, MoodleLogData.class)
				.setElement (User.class, UserData.class)
				.setGenerator (Element.class, SequentialIdGenerator.class)
				.build ();

		final Profile scratchProfile = Profile.builder ()
				.setName ("mem")
				.setMutable (true)
				.setElement (Action.class, ActionData.class)
				.setElement (Activity.class, GenericActivity.class)
				.setElement (ActivityReference.class, ActivityReferenceData.class)
				.setElement (ActivitySource.class, ActivitySourceData.class)
				.setElement (ActivityType.class, ActivityTypeData.class)
				.setElement (Course.class, CourseData.class)
				.setElement (Enrolment.class, EnrolmentData.class)
				.setElement (Grade.class, GradeData.class)
				.setElement (LogEntry.class, LogData.class)
				.setElement (Network.class, NetworkData.class)
				.setElement (Role.class, RoleData.class)
				.setElement (User.class, UserData.class)
				.setGenerator (Element.class, SequentialIdGenerator.class)
				.setGenerator (Enrolment.class, RandomIdGenerator.class)
				.build ();

		DomainModel scratch = null;
		DomainModel coursedb = null;
		Extractor extractor = null;

		try
		{
			extractor = Extractor.create (JPADataStore.create (sourceProfile))
				.addMatcher (ActionMatcher.create (Book.class, BookChapter.class, "add chapter"))
				.addMatcher (ActionMatcher.create (Book.class, BookChapter.class, "print chapter"))
				.addMatcher (ActionMatcher.create (Book.class, BookChapter.class, "update chapter"))
				.addMatcher (ActionMatcher.create (Book.class, BookChapter.class, "view chapter"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumDiscussion.class, "add discussion"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumDiscussion.class, "move discussion"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumDiscussion.class, "update discussion"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumDiscussion.class, "view discussion"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumPost.class, "add post"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumPost.class, "update post"))
				.addMatcher (ActionMatcher.create (Forum.class, ForumPost.class, "delete chapter"))
				.addMatcher (ActionMatcher.create (Lesson.class, LessonPage.class, "view"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "update example assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "add example assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "update reference assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "add reference assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "view example"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "update example"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "add example"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "update assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "add assessment"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "view submission"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "update submission"))
				.addMatcher (ActionMatcher.create (Workshop.class, WorkshopSubmission.class, "add submission"))
				.setCourse (5L)
				.addRegistration ("admin", "admin")
				.addRegistrations ("instructor", new File (INSTRUCTOR_LIST))
				.addRegistrations ("ta", new File (TA_LIST))
				.addRegistrations ("student", new File (STUDENT_LIST));

			scratch = extractor.extract (MemDataStore.create (scratchProfile));

/*			coursedb.getTransaction ().begin ();

			InsertProcessor processor = coursedb.getProcessor ();

			App.log.info ("Copying Activities from scratch to coursedb");
			processor.processElements (course.getActivities ());

			List<User> allUsers = scratch.getQuery (User.class, User.SELECTOR_ALL)
				.queryAll ()
				.stream ()
				.filter (u -> u.getEnrolment (course) != null)
				.sorted ((u1, u2) -> App.compareUsers (u1, u2, course))
				.collect (Collectors.toList ());

			processor.processElements (allUsers.stream ()
					.map (u -> u.getEnrolment (course))
					.sorted ((e1, e2) -> e1.getId ().compareTo (e2.getId ()))
					.collect (Collectors.toList ()));

			App.log.info ("Committing Transaction data to the database");
			coursedb.getTransaction ().commit ();
			coursedb.getTransaction ().begin ();

			App.log.info ("Copying Users from scratch to coursedb");
			processor.processElements (allUsers);

			App.log.info ("Copying Activities from scratch to coursedb");
			processor.processElements (course.getActivities ());

			App.log.info ("Copying Log Entries from scratch to coursedb");

			int i = 0;

			for (LogEntry le : log)
			{
				processor.processElement (le);
				i ++;
			}

			App.log.info ("Processing deferred items");
			processor.processQueue ();

			App.log.info ("Writing User data to the database");
			coursedb.getTransaction ().commit ();
*/		}
		finally
		{
			if (extractor != null)
			{
				extractor.close ();
			}

			if (coursedb != null)
			{
				coursedb.close ();
			}
		}
	}
}
