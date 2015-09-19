package ca.uoguelph.socs.icc.edm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.net.InetAddress;

import java.util.List;
import java.util.NavigableMap;

import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.User;

import ca.uoguelph.socs.icc.edm.domain.element.ActionData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;
import ca.uoguelph.socs.icc.edm.domain.element.CourseData;
import ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData;
import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;
import ca.uoguelph.socs.icc.edm.domain.element.GradeData;
import ca.uoguelph.socs.icc.edm.domain.element.LogData;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleActivity;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleActivityType;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;
import ca.uoguelph.socs.icc.edm.domain.element.NetworkData;
import ca.uoguelph.socs.icc.edm.domain.element.RoleData;
import ca.uoguelph.socs.icc.edm.domain.element.UserData;

import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.CourseBuilder;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;
import ca.uoguelph.socs.icc.edm.domain.NamedActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.NetworkBuilder;
import ca.uoguelph.socs.icc.edm.domain.RoleBuilder;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.UserBuilder;

import ca.uoguelph.socs.icc.edm.domain.ActionLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeLoader;
import ca.uoguelph.socs.icc.edm.domain.CourseLoader;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentLoader;
import ca.uoguelph.socs.icc.edm.domain.LogEntryLoader;
import ca.uoguelph.socs.icc.edm.domain.NetworkLoader;
import ca.uoguelph.socs.icc.edm.domain.RoleLoader;
import ca.uoguelph.socs.icc.edm.domain.UserLoader;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Transaction;
import ca.uoguelph.socs.icc.edm.domain.datastore.JPADataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.MemDataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.NullDataStore;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.PassThruIdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator;

import ca.uoguelph.socs.icc.edm.domain.metadata.Profile;
import ca.uoguelph.socs.icc.edm.domain.metadata.ProfileBuilder;

import ca.uoguelph.socs.icc.edm.domain.resolver.Resolver;

public class App
{
	public static final Logger log;

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

		// DataStore implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.JPADataStore");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.MemDataStore");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.NullDataStore");

		// IdGenerator implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.NullIdGenerator");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.PassThruIdGenerator");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.RandomIdGenerator");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.SequentialIdGenerator");

		// Core Element implementations
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActionData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.CourseData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.GradeData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.LogData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.MoodleActivity");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.MoodleActivityType");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.NetworkData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.RoleData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.UserData");

		// Moodle Activity Data Classes
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Assign");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Book");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.BookChapter");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.BookChapterLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Checklist");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Choice");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Feedback");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Folder");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Forum");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumDiscussion");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumDiscussionLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumPost");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumPostLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Label");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Lesson");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.LessonPage");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.LessonPageLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Page");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Quiz");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Resource");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Scheduler");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.URL");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Workshop");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmission");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmissionLog");
	}

/*	public void loadCSV (final DomainModel model, final String filename, final Course course, final Role role) throws Exception
	{
		CSVParser reader = (CSVFormat.EXCEL.withNullString("#N/A")).parse (new BufferedReader (new FileReader (filename)));

		for (CSVRecord rec : reader)
		{
			User muser = (model.getUserLoader ()).fetchByUsername ((rec.get (0)).substring (0, (rec.get (0)).indexOf ('@')));

			if (muser != null)
			{
				this.users.put (muser.getId (), new UserData (Integer.valueOf (rec.get (3)), muser.getFirstname (), muser.getLastname (), muser.getUsername ()));

				this.enrolments.put (muser.getId (), new UserEnrolmentData (this.users.get (muser.getId ()), course, role, (rec.get (1) != null) ?  new Integer ((int) Math.ceil (Double.valueOf (rec.get (1)))) : null, (rec.get (2) != null) ? true : false));
			}
		}

		reader.close ();
	}*/



    public static void main(final String[] args) throws Exception
    {
		DomainModel scratch = null;
		DomainModel moodledb = null;
		DomainModel coursedb = null;

		Profile course = new ProfileBuilder ()
			.setName ("course")
			.setMutable (true)
			.setElementClass (Action.class, ActionData.class)
			.setElementClass (Activity.class, GenericActivity.class)
			.setElementClass (ActivitySource.class, ActivitySourceData.class)
			.setElementClass (ActivityType.class, ActivityTypeData.class)
			.setElementClass (Course.class, CourseData.class)
			.setElementClass (Enrolment.class, EnrolmentData.class)
			.setElementClass (Grade.class, GradeData.class)
			.setElementClass (LogEntry.class, LogData.class)
			.setElementClass (Role.class, RoleData.class)
			.setElementClass (User.class, UserData.class)
			.setGenerator (Element.class, NullIdGenerator.class)
			.setGenerator (Enrolment.class, RandomIdGenerator.class)
			.build ();

		Profile moodle = new ProfileBuilder ()
			.setName ("moodledb")
			.setElementClass (Activity.class, MoodleActivity.class)
			.setElementClass (ActivityType.class, ActivityTypeData.class)
			.setElementClass (Course.class, CourseData.class)
			.setElementClass (Enrolment.class, EnrolmentData.class)
			.setElementClass (LogEntry.class, MoodleLogData.class)
			.setElementClass (User.class, UserData.class)
			.setGenerator (Element.class, SequentialIdGenerator.class)
			.build ();

		Profile mem = new ProfileBuilder ()
			.setName ("mem")
			.setMutable (true)
			.setElementClass (Action.class, ActionData.class)
			.setElementClass (Activity.class, GenericActivity.class)
			.setElementClass (ActivitySource.class, ActivitySourceData.class)
			.setElementClass (ActivityType.class, ActivityTypeData.class)
			.setElementClass (Course.class, CourseData.class)
			.setElementClass (Enrolment.class, EnrolmentData.class)
			.setElementClass (Grade.class, GradeData.class)
			.setElementClass (LogEntry.class, LogData.class)
			.setElementClass (Network.class, NetworkData.class)
			.setElementClass (Role.class, RoleData.class)
			.setElementClass (User.class, UserData.class)
			.setGenerator (Element.class, SequentialIdGenerator.class)
			.setGenerator (Enrolment.class, RandomIdGenerator.class)
			.build ();

		try
		{
			moodledb = new DomainModel (DataStore.getInstance (JPADataStore.class, moodle));
			scratch = new DomainModel (DataStore.getInstance (MemDataStore.class, mem));

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");

			Course c = CourseLoader.getInstance (moodledb)
				.fetchById (5L);

			System.out.printf ("%s: %s %d\n", c.getName (), c.getSemester ().getName (), c.getYear ());
			c.getActivities ().forEach (x -> System.out.printf ("\t%s: %d\n", x.getName (), x.getSubActivities ().size ()));

			scratch.getTransaction ().begin ();

			ActivitySource.builder (scratch)
				.setName ("moodle")
				.build ();

			scratch.insert (c);
			scratch.getTransaction ().commit ();

			Course nc = CourseLoader.getInstance (scratch)
				.fetchById (1L);

			System.out.printf ("%s: %s %d\n", nc.getName (), nc.getSemester ().getName (), nc.getYear ());
			nc.getActivities ().forEach (x -> System.out.printf ("\t%s: %d\n", x.getName (), x.getSubActivities ().size ()));

			System.out.println ("--------------------++++++++++++++++++++++++++++++++++++++++--------------------");
		}
		catch (Throwable ex)
		{
			log.error ("We have an exception: ", ex);
			throw ex;
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
