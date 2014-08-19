package ca.uoguelph.socs.icc.moodleapi;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class CourseData implements Course
{
	private Long id;
	private String name;
	private Semester semester;
	private int year;
	private Set<Activity> activities;
	private Set<Enrolment> enrolments;
	private List<LogEntry> logentries;

	protected CourseData(String name)
	{
		this.name = new String (name);
		this.semester = Semester.Winter;
		this.year = 0;
		this.activities = new HashSet<Activity> ();
		this.enrolments = new HashSet<Enrolment> ();
		this.logentries = new ArrayList<LogEntry> ();
	}

	protected CourseData(String name, Semester semester, int year)
	{
		this.CourseData (name);

		this.semester = semester;
		this.year = year;
	}

	protected CourseData(String name, Semester semester, int year, Set<Enrolment> enrolments, Set<Activity> activities)
	{
		this.CourseData(name, semester, year);

		this.activities.addAll(activities);
		this.enrolments.addAll(enrolments);
	}

	public String getName()
	{
		return new String (this.name);
	}

	public void setName(String name)
	{
		this.name = new String (name);
	}

	public Semester getSemester()
	{
		return this.semester;
	}

	public void setSemester(Semester semester)
	{
		this.semester = semester;
	}

	public int getYear()
	{
		return this.year;
	}

	public void setYear(int year)
	{
		this.year = year
	}

	public Set<Activity> getActivities()
	{
		return new HashSet<Activity> (this.activities);
	}

	public void addActivity(Activity activity)
	{

	}

	public Set<Enrolment> getEnrolments()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	protected void addEnrolment(Enrolment enrolment)
	{
	}

	public Set<Enrolment> getStudents()
	{
		return null;
	}

	public List<LogEntry> getLog()
	{
		return new ArrayList<LogEntry> (this.logentries);
	}

	protected void addLogEntry(LogEntry entry)
	{
	}

	public String toString()
	{
		return new String (this.name + ": " + this.semester + ", " + this.year);
	}
}