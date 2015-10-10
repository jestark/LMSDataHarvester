package ca.uoguelph.socs.icc.edm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.net.InetAddress;

import java.util.List;
import java.util.NavigableMap;

import java.util.TreeMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.InsertProcessor;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.ParentActivity;
import ca.uoguelph.socs.icc.edm.domain.Role;
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

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Semester;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
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

import ca.uoguelph.socs.icc.edm.resolver.LogEntryConverter;

public class App
{
	public static final Logger log;

	public static final Profile COURSE;

	public static final Profile MEMORY;

	public static final Profile MOODLE;

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
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActivityReferenceData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.ActionData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.CourseData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.EnrolmentData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.GenericActivity");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.GradeData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.LogData");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.MoodleActivityReference");
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
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Wiki");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WikiPage");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WikiPageLog");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Workshop");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmission");
		loadClass ("ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmissionLog");

		COURSE = new ProfileBuilder ()
			.setName ("coursedb")
			.setMutable (true)
			.setElementClass (Action.class, ActionData.class)
			.setElementClass (Activity.class, GenericActivity.class)
			.setElementClass (ActivityReference.class, ActivityReferenceData.class)
			.setElementClass (ActivitySource.class, ActivitySourceData.class)
			.setElementClass (ActivityType.class, ActivityTypeData.class)
			.setElementClass (Course.class, CourseData.class)
			.setElementClass (Enrolment.class, EnrolmentData.class)
			.setElementClass (Grade.class, GradeData.class)
			.setElementClass (LogEntry.class, LogData.class)
			.setElementClass (Network.class, NetworkData.class)
			.setElementClass (Role.class, RoleData.class)
			.setElementClass (User.class, UserData.class)
			.setGenerator (Element.class, NullIdGenerator.class)
			.setGenerator (Enrolment.class, RandomIdGenerator.class)
			.build ();

		MOODLE = new ProfileBuilder ()
			.setName ("moodledb")
			.setElementClass (ActivityReference.class, MoodleActivityReference.class)
			.setElementClass (ActivityType.class, ActivityTypeData.class)
			.setElementClass (Course.class, CourseData.class)
			.setElementClass (Enrolment.class, EnrolmentData.class)
			.setElementClass (LogEntry.class, MoodleLogData.class)
			.setElementClass (User.class, UserData.class)
			.setGenerator (Element.class, SequentialIdGenerator.class)
			.build ();

		MEMORY = new ProfileBuilder ()
			.setName ("mem")
			.setMutable (true)
			.setElementClass (Action.class, ActionData.class)
			.setElementClass (Activity.class, GenericActivity.class)
			.setElementClass (ActivityReference.class, ActivityReferenceData.class)
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
	}

	public static int compareIds (final Element e1, final Element e2)
	{
		return e1.getId ().compareTo (e2.getId ());
	}

	public static int compareUsers (final User u1, final User u2, final Course course)
	{
		int result = u1.getEnrolment (course).getRole ().getId ().compareTo (u2.getEnrolment (course).getRole ().getId ());

		if (result == 0)
		{
			result = u1.getLastname ().compareTo (u2.getLastname ());

			if (result == 0)
			{
				result = u1.getFirstname ().compareTo (u2.getFirstname ());

				if (result == 0)
				{
					result = u1.getUsername ().compareTo (u2.getUsername ());
				}
			}
		}

		return result;
	}

    public static void main(final String[] args) throws Exception
    {
		DomainModel scratch = null;
		DomainModel moodledb = null;
		DomainModel coursedb = null;

		try
		{
			CSVFormat format = CSVFormat.EXCEL.withNullString("#N/A");

			App.log.debug ("Creating Course DomainModel");
			coursedb = new DomainModel (DataStore.getInstance (JPADataStore.class, App.COURSE));

			App.log.debug ("Creating Moodle DomainModel");
			moodledb = new DomainModel (DataStore.getInstance (JPADataStore.class, App.MOODLE));

			App.log.debug ("Creating Scratch DomainModel");
			scratch = new DomainModel (DataStore.getInstance (MemDataStore.class, App.MEMORY));

			Query<Course> cQuery = moodledb.getQuery (Course.class, Course.SELECTOR_ID);
			Query<LogEntry> lQuery = moodledb.getQuery (LogEntry.class, LogEntry.SELECTOR_COURSE);
			Query<User> uQuery = moodledb.getQuery (User.class, User.SELECTOR_USERNAME);

			MoodleHarvester harvester = new MoodleHarvester (scratch);

			InsertProcessor processor = coursedb.getProcessor ();

			scratch.getTransaction ().begin ();
			coursedb.getTransaction ().begin ();

			App.log.info ("Creating Roles in scratch");
			Role admin = harvester.addRole ("admin");
			Role instructor = harvester.addRole ("instructor");
			Role ta = harvester.addRole ("ta");
			Role student = harvester.addRole ("student");

			final Course moodleCourse = cQuery.setValue (Course.ID, 5L)
					.query ();

			App.log.info ("Copying Course from moodledb to scratch");
			final Course course = harvester.addCourse (moodleCourse);

			App.log.info ("Copying Activities from scratch to coursedb");
			processor.processElements (course.getActivities ());

			App.log.info ("Adding admin users to scratch");
			harvester.addEnrolment (harvester.addUser ("@@NONE@@", "NULL", "USER"), course, admin);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "admin").query (), course, admin);

			App.log.info ("Adding instructors to scratch");
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, instructor);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, instructor);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, instructor);

			App.log.info ("Adding TA's to scratch");
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);
			harvester.addEnrolment (uQuery.setValue (User.USERNAME, "***REMOVED***").query (), course, ta);

			App.log.info ("Adding students to scratch");
			for (CSVRecord rec : format.parse (new BufferedReader (new FileReader ("***REMOVED***"))))
			{
				User user = uQuery.setValue (User.USERNAME, rec.get (0))
					.query ();

				if (user != null)
				{
					harvester.addEnrolment (user,
							course,
							student,
							(rec.get (1) != null)
								?  Integer.valueOf ((int) Math.ceil (Double.valueOf (rec.get (1))))
								: null,
							(rec.get (2) != null) ? true : false);
				}
				else
				{
					App.log.warn ("User: {} does not exist in the moodle database", rec.get (0));
				}
			}

			App.log.info ("Processing Log");

			LogEntryConverter lConverter = new LogEntryConverter (scratch, moodledb);

			List<LogEntry> log = lQuery.setValue (LogEntry.COURSE, moodleCourse)
				.queryAll ()
				.stream ()
				.map (x -> lConverter.convert ((MoodleLogData) x))
				.collect (Collectors.toList ());

			scratch.getTransaction ().commit ();

			App.log.info ("Copying Course from scratch to coursedb");
			final Course cCourse = processor.processElement (course);

			App.log.info ("Copying Roles from scratch to coursedb");
			processor.processElement (admin);
			processor.processElement (instructor);
			processor.processElement (ta);
			processor.processElement (student);

			App.log.info ("Copying Activities from scratch to coursedb");
			processor.processElements (course.getActivities ());

			List<User> allUsers = scratch.getQuery (User.class, User.SELECTOR_ALL)
				.queryAll ()
				.stream ()
				.filter (u -> u.getEnrolment (course) != null)
				.sorted ((u1, u2) -> App.compareUsers (u1, u2, course))
				.collect (Collectors.toList ());

			App.log.info ("Copying Enrolments from scratch to coursedb");
			processor.processElements (allUsers.stream ()
					.map (u -> u.getEnrolment (course))
					.sorted ((e1, e2) -> App.compareIds (e1, e2))
					.collect (Collectors.toList ()));

			processor.clear ();

			App.log.info ("Copying Log Entries from scratch to coursedb");
			processor.processElements (log);
			processor.processQueue ();

			App.log.info ("Writing Course/Enrolment/Log data to the database");
			coursedb.getTransaction ().commit ();
			coursedb.getTransaction ().begin ();

			App.log.info ("Copying Users from scratch to coursedb");
			processor.processElements (allUsers);
			processor.processQueue ();

			App.log.info ("Writing User data to the database");
			coursedb.getTransaction ().commit ();
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

class MoodleHarvester
{
	private static final CSVFormat FORMAT;

	private final Logger log;

	private final DomainModel model;

	private final EnrolmentBuilder eBuilder;

	private final RoleBuilder rBuilder;

	private final UserBuilder uBuilder;

	private final InsertProcessor processor;

//	private final Role admin;

//	private final Role instructor;

//	private final Role ta;

//	private final Role student;

	static
	{
		FORMAT = CSVFormat.EXCEL.withNullString("#N/A");
	}

	public MoodleHarvester (final DomainModel model)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.model = model;

		this.eBuilder = Enrolment.builder (model);
		this.uBuilder = User.builder (model);

		this.rBuilder = Role.builder (model);

		this.processor = this.model.getProcessor ();

//		this.admin = rBuilder.setName ("admin").build ();
//		this.instructor = rBuilder.setName ("instructor").build ();
//		this.ta = rBuilder.setName ("ta").build ();
//		this.student = rBuilder.setName ("student").build ();
	}

	public Course addCourse (final Course course)
	{
		Course result = this.processor.processElement (course);

		this.processor.clear ();
		this.processor.processElements (course.getActivities ());

		return result;
	}

	public Enrolment addEnrolment (final User user, final Course course, final Role role)
	{
		this.log.trace ("addEnrolment: user={}, course={}, role={}", user, course, role);

		Enrolment enrolment = this.eBuilder.clear ()
			.setRole (role)
			.setCourse (course)
			.setUsable (false)
			.build ();

		this.uBuilder.load (user)
			.addEnrolment (enrolment)
			.build ();

		return enrolment;
	}

	public Enrolment addEnrolment (final User user, final Course course, final Role role, final Integer grade, final Boolean usable)
	{
		Enrolment enrolment = this.eBuilder.clear ()
			.setRole (role)
			.setCourse (course)
			.setFinalGrade (grade)
			.setUsable (usable)
			.build ();

		this.uBuilder.load (user)
			.addEnrolment (enrolment)
			.build ();

		return enrolment;
	}

	public Role addRole (final String name)
	{
		return this.rBuilder.setName (name)
			.build ();
	}

	public User addUser (final String username, final String firstName, final String lastName)
	{
		return this.uBuilder.clear ()
			.setUsername (username)
			.setFirstname (firstName)
			.setLastname (lastName)
			.build ();
	}
}
