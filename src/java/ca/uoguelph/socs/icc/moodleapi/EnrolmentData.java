package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class EnrolmentData implements Enrolment
{
	private boolean usable;
	private Course course;
	private Grade grade;
	private Role role;
	private ArrayList<LogEntry> logentries;

	public EnrolmentData(CourseData course, Role role)
	{
	}

	public EnrolmentData(CourseData course, Role role, boolean usable)
	{
	}

	public Role getRole()
	{
		return null;
	}

	public Course getCourse()
	{
		return null;
	}

	protected void setCourse(Course course)
	{
	}

	public String toString()
	{
		return null;
	}
}
