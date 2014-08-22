package ca.uoguelph.socs.icc.moodleapi;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Course
{
	private long id;
	private String name;
	private Semester semester;
	private Integer year;
//	private Set<Activity> activities;
	private Set<Enrolment> enrolments;
//	private List<LogEntry> logentries;

	protected Course ()
	{
		this.id = -1;
		this.name = new String ("UNSET");
		this.semester = Semester.WINTER;
		this.year = new Integer (0);
//		this.activities = new HashSet<Activity> ();
		this.enrolments = new HashSet<Enrolment> ();
//		this.logentries = new ArrayList<LogEntry> ();
	}

	protected Course(String name)
	{
		this();
		this.name = name;
	}

	protected Course(String name, Semester semester, Integer year)
	{
		this(name);

		this.semester = semester;
		this.year = year;
	}

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Semester getSemester()
	{
		return this.semester;
	}

	public void setSemester(Semester semester)
	{
		this.semester = semester;
	}

	public Integer getYear()
	{
		return this.year;
	}

	public void setYear(Integer year)
	{
		this.year = year;
	}

//	public Set<Activity> getActivities()
//	{
//		return new HashSet<Activity> (this.activities);
//	}

//	public void addActivity(Activity activity)
//	{
//
//	}

	public Set<Enrolment> getEnrolments()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

//	protected void addEnrolment(Enrolment enrolment)
//	{
//	}

//	public Set<Enrolment> getStudents()
//	{
//		return null;
//	}

//	public List<LogEntry> getLog()
//	{
//		return new ArrayList<LogEntry> (this.logentries);
//	}

//	protected void addLogEntry(LogEntry entry)
//	{
//	}

	public String toString()
	{
		return new String (this.name + ": " + this.semester + ", " + this.year);
	}
}