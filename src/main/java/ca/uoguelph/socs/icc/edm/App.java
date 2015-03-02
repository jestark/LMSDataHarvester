package ca.uoguelph.socs.icc.edm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserManager;

import ca.uoguelph.socs.icc.edm.domain.database.UserDatabaseFactory;
import ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleDatabaseFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreTransaction;

public class App
{
	public static final Logger log = LoggerFactory.getLogger (App.class);

	public static void loadClass (String name)
	{
		try
		{
			App.log.debug ("Loading Class: {}", name);
			Class.forName (name);
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println (ex);
			throw new RuntimeException (ex.getMessage ());
		}
	}

	public static void init ()
	{
//		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleDBLogEntry");
//		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleEnrolmentUserData");

		// Default ElementBuilder Implementations
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActionBuilder");
//		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityTypeBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultCourseBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultGradeBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultUserBuilder");

		// Default ElementManager implementations
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityTypeManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultRoleManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultUserManager");

		// IdGenerator implementations
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator");

		// Core Element implementations
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActionData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.CourseData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.EnrolmentData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.GradedActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.LogData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.RoleData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData");

		// Moodle Activity Data Classes
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Assign");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Book");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.BookChapter");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.BookChapterLog");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Checklist");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Choice");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Feedback");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Folder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Forum");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumDiscussion");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumDiscussionLog");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumPost");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumPostLog");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Label");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Lesson");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.LessonPage");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.LessonPageLog");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Page");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Quiz");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Resource");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Scheduler");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.URL");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Workshop");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.WorkshopSubmission");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.WorkshopSubmissionLog");
	}

    public static void main(String[] args) throws Exception
    {
		DomainModel moodledb = null;
		DomainModel coursedb = null;

		App.init ();

		try
		{
			moodledb = (MoodleDatabaseFactory.getInstance ()).createDomainModel ();
			coursedb = (UserDatabaseFactory.getInstance ()).createDomainModel ();

			UserManager umanager = moodledb.getUserManager ();

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");
			System.out.println (umanager.fetchByUsername ("starkj"));

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");

			DataStoreTransaction trans = coursedb.getTransaction ();
			trans.begin ();

			RoleManager rmanager = coursedb.getRoleManager ();
			RoleBuilder rbuilder = rmanager.getBuilder ();

			rbuilder.setName ("student");
			Role role = rbuilder.build ();

			trans.commit ();

			System.out.println (role);
		}
		finally
		{
			if (moodledb != null)
			{
				moodledb.close ();
			}

			if (coursedb != null)
			{
				coursedb.close ();
			}
		}
    }
}
