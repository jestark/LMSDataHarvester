package ca.uoguelph.socs.icc.edm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import java.util.Set;
import java.util.Map;
import java.util.List;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActionManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeManager;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.CourseManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentManager;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryManager;
import ca.uoguelph.socs.icc.edm.domain.NamedActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleManager;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;
import ca.uoguelph.socs.icc.edm.domain.UserManager;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.ActionData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.core.GenericActivity;
import ca.uoguelph.socs.icc.edm.domain.core.LogData;
import ca.uoguelph.socs.icc.edm.domain.core.RoleData;
import ca.uoguelph.socs.icc.edm.domain.core.UserData;
import ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData;

import ca.uoguelph.socs.icc.edm.domain.database.UserDatabaseFactory;
import ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleActivity;
import ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleLogData;
import ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleDatabaseFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreTransaction;

public class App
{
	public static final Logger log;

	private ActivitySource source;
	private Map<String, Action> actions;
	private Map<String, Activity> activities;
	private Map<String, ActivityType> types;

	private Map<Long, Enrolment> enrolments;
	private Map<Long, User> users;

	static void loadClass (final String name)
	{
		log.trace ("loadClass: name={}", name);
		try
		{
			Class.forName (name);
		}
		catch (ClassNotFoundException ex)
		{
			log.error ("Failed to load class", ex);
			throw new RuntimeException (ex.getMessage ());
		}
	}

	static
	{
		log = LoggerFactory.getLogger (App.class);

		// Default ElementBuilder Implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActionBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityTypeBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultCourseBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultGenericActivityBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultGradeBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultRoleBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultSubActivityBuilder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.builder.DefaultUserBuilder");

		// Default ElementManager implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActionManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityTypeManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultCourseManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultLogEntryManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultRoleManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.manager.DefaultUserManager");

		// ElementManager implementations for the Moodle Database
		loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleActivityManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleCourseManager");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleLogEntryManager");

		// IdGenerator implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator");

		// Core Element implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActionData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.CourseData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.EnrolmentData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.GradedActivity");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.LogData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.RoleData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.core.UserEnrolmentData");

		// Element implmentations for the Moodle Database
		loadClass ("ca.uoguelph.socs.icc.edm.domain.database.moodle.MoodleActivity");

		// Moodle Activity Data Classes
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Assign");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Book");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.BookChapter");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.BookChapterLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Checklist");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Choice");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Feedback");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Folder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Forum");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumDiscussion");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumDiscussionLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumPost");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.ForumPostLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Label");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Lesson");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.LessonPage");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.LessonPageLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Page");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Quiz");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Resource");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Scheduler");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.URL");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.Workshop");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.WorkshopSubmission");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.activity.moodle.WorkshopSubmissionLog");
	}

	public App ()
	{
		this.source = new ActivitySourceData ("moodle");
		this.actions = new HashMap<String, Action> ();
		this.activities = new HashMap<String, Activity> ();
		this.types = new HashMap<String, ActivityType> ();

		this.enrolments = new HashMap<Long, Enrolment> ();
		this.users = new HashMap<Long, User> ();
	}

	public void loadCSV (final DomainModel model, final String filename, final Course course, final Role role) throws Exception
	{
		CSVParser reader = (CSVFormat.EXCEL.withNullString("#N/A")).parse (new BufferedReader (new FileReader (filename)));

		for (CSVRecord rec : reader)
		{
			User muser = (model.getUserManager ()).fetchByUsername ((rec.get (0)).substring (0, (rec.get (0)).indexOf ('@')));

			if (muser != null)
			{
				this.users.put (muser.getId (), new UserData (Integer.valueOf (rec.get (3)), muser.getFirstname (), muser.getLastname (), muser.getUsername ()));

				this.enrolments.put (muser.getId (), new UserEnrolmentData (this.users.get (muser.getId ()), course, role, (rec.get (1) != null) ?  new Integer ((int) Math.ceil (Double.valueOf (rec.get (1)))) : null, (rec.get (2) != null) ? true : false));
			}
		}

		reader.close ();
	}

	public Action getAction (final String name)
	{
		if (! this.actions.containsKey (name))
		{
			this.actions.put (name, new ActionData (name));
		}

		return this.actions.get (name);
	}

	public ActivityType getType (final String name)
	{
		if (! this.types.containsKey (name))
		{
			this.types.put (name, new ActivityTypeData (this.source, name));
		}

		return this.types.get (name);
	}

	public Activity getGenericActivity (final String name, final Course course)
	{
		if (! this.activities.containsKey (name))
		{
			activities.put (name, new GenericActivity (this.getType (name), course));
		}

		return activities.get (name);
	}

	public List<LogEntry> processLog (final DomainModel model, final List<LogEntry> entries)
	{
		List<LogEntry> result = new LinkedList<LogEntry> ();

		for (LogEntry e : entries)
		{
			if (this.enrolments.containsKey (((MoodleLogData) e).getUser ()))
			{
				Action action = this.getAction (((MoodleLogData) e).getActionName ());

				Activity activity = (model.getActivityManager ()).fetchById (((MoodleLogData) e).getActivityId ());

				if (activity == null)
				{
					activity = this.getGenericActivity (((MoodleLogData)e).getModule (), e.getCourse ());
				}

				Enrolment enrolment = this.enrolments.get (((MoodleLogData) e).getUser ());

				result.add (new LogData (action, activity, enrolment, e.getIPAddress (), e.getTime ()));
			}
			else
			{

			}
		}

		return result;
	}

	public void processCourse (final DomainModel model, final Course course)
	{
		log.trace ("processCourse: model={}, course={}", model, course);

		CourseBuilder builder = (model.getCourseManager ()).getBuilder ();
		builder.load (course);

		Course newCourse = builder.build ();

		for (Activity a : course.getActivities ())
		{
			processActivity (model, a);
		}
	}

	public void processActivity (final DomainModel model, final Activity activity)
	{
		log.trace ("processActivity: model={}, activity={}", model, activity);

		ActivityType type = (model.getActivityTypeManager ()).fetchByName (this.source, (activity.getType ()).getName ());

		NamedActivityBuilder builder = (model.getActivityManager ()).getBuilder (NamedActivityBuilder.class, type);
		builder.setCourse (activity.getCourse ());
		builder.setName (activity.getName ());

		Activity newActivity = builder.build ();

		if (activity.hasSubActivities ())
		{
			SubActivityBuilder sBuilder = (model.getActivityManager ()).getBuilder (newActivity);

			for (SubActivity s : activity.getSubActivities ())
			{
				processSubActivity (model, s, activity);
			}
		}
	}

	public void processSubActivity (final DomainModel model, final SubActivity subactivity, final Activity parent)
	{
		log.trace ("processSubActivity: model={}, subactivity='{}', parent={}", model, subactivity, parent);

		Activity newSubActivity = null;

		if (subactivity.hasSubActivities ())
		{
			SubActivityBuilder sBuilder = (model.getActivityManager ()).getBuilder (newSubActivity);

			for (SubActivity s : subactivity.getSubActivities ())
			{
				processSubActivity (model, s, subactivity);
			}
		}
	}

    public static void main(final String[] args) throws Exception
    {
		DomainModel moodledb = null;
		DomainModel coursedb = null;

		try
		{
			moodledb = (MoodleDatabaseFactory.getInstance ()).createDomainModel ();
			coursedb = (UserDatabaseFactory.getInstance ()).createDomainModel ();

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");

			App app = new App ();

			DataStoreTransaction trans = coursedb.getTransaction ();
//			trans.begin ();

			Course course = (moodledb.getCourseManager ()).fetchById (5L);

			app.loadCSV (moodledb, "***REMOVED***", course, new RoleData ("student"));

			List<LogEntry> entries = app.processLog (moodledb, (moodledb.getLogEntryManager ()).fetchAllforCourse (course));

//			processCourse (cmanager.fetchById (5L), coursedb);

//			trans.commit ();

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");
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
