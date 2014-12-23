package ca.uoguelph.socs.icc.edm;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.UserManager;
import ca.uoguelph.socs.icc.edm.domain.database.UserDatabaseFactory;
import ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleDatabaseFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.UserFactory;

public class App
{
	static final Logger logger = LogManager.getLogger(App.class.getName());

	public static void loadClass (String name)
	{
		try
		{
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
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.UserBuilder");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActionData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.CourseData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.EnrolmentData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.GradedActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.LogData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.RoleData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData");
//		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleDBLogEntry");
//		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleEnrolmentUserData");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityTypeManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultRoleManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultUserManager");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleAssignActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleBookActivityChapter");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleBookActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleChecklistActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleChoiceActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleFeedbackActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleFolderActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleForumActivityDiscussion");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleForumActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleForumActivityPost");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleLabelActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleLessonActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleLessonActivityPage");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodlePageActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleQuizActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleResourceActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleSchedulerActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleURLActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleWorkshopActivity");
		App.loadClass ("ca.uoguelph.socs.icc.edm.domain.moodle.MoodleWorkshopActivitySubmission");
	}

    public static void main(String[] args) throws Exception
    {
		DomainModel moodledb = null;

		App.init ();

		try
		{
			moodledb = (MoodleDatabaseFactory.getInstance ()).createDomainModel ();

			UserManager umanager = (UserFactory.getInstance ()).createManager (moodledb);

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");
			System.out.println (umanager.fetchByUsername ("starkj"));

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");
		}
		finally
		{
			if (moodledb != null)
			{
				moodledb.close ();
			}
		}
    }
}
