package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class CourseData implements Course
{
	private String name;
	private int semester;
	private int year;
//	private ArrayList logentries;
//	private ArrayList activities;
//	private ArrayList enrolments;

	protected CourseData(String name)
	{
		this.name = name.clone ();
	}

	protected CourseData(String name, int semester, int year)
	{
	}

//	protected CourseData(String name, int semester, int year, ArrayList enrolments, ArrayList activities)
//	{
//	}

	public String getName()
	{
		return this.name.clone ();
	}

	public void setName(String name)
	{
	}

	public int getSemester()
	{
		return 0;
	}

	public void setSemester(int semester)
	{
	}

	public int getYear()
	{
		return 0;
	}

	public void setYear(int year)
	{
	}

	public ArrayList getActivities()
	{
		return null;
	}

	public void addActivity(Activity activity)
	{
	}

	public ArrayList getEnrollments()
	{
		return null;
	}

	public ArrayList getStudents()
	{
		return null;
	}

	public ArrayList getLog()
	{
		return null;
	}

	protected void addLogEntry(LogEntry entry)
	{
	}

	public String toString()
	{
		return null;
	}

	protected void addEnrollment(Enrolment enrolment)
	{
	}
}