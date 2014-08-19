package ca.uoguelph.socs.icc.moodleapi;

import java.util.List;
import java.util.ArrayList;

public class EnrolmentData implements Enrolment
{
	private Long id;
	private Boolean usable;
	private Course course;
	private Grade grade;
	private Role role;
	private List<LogEntry> logentries;

	protected EnrolmentData(Long id, Course course, Role role, Boolean usable)
	{

	}

	protected EnrolmentData(Course course, Role role, Boolean usable)
	{
		this.EnrolmentData (course, role);
		this.usable = usable;
	}

	public Long getId ()
	{
		return new Long (this.id);
	}

	public Role getRole()
	{
		return this.role;
	}

	public Course getCourse()
	{
		return this.course;
	}

	public String toString()
	{
		return new String ();
	}
}
