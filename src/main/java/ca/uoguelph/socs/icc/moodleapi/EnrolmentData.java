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
//	private List<LogEntry> logentries;

	protected EnrolmentData()
	{
		this.id = null;
		this.usable = new Boolean (false);
		this.course = null;
		this.grade = null;
		this.role = null;
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	public String getName ()
	{
		return (new Long (this.id)).toString ();
	}

	public Role getRole()
	{
		return this.role;
	}

	protected void setRole (Role role)
	{
		this.role = role;
	}

	public Course getCourse()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	public Boolean isUsable ()
	{
		return this.usable;
	}

	public void setUsable (Boolean usable)
	{
		this.usable = usable;
	}

	public Grade getGrade ()
	{
		return null;
	}

	protected void setGrade (Grade grade)
	{
		this.grade = grade;
	}

	public String toString()
	{
		return this.id.toString ();
	}
}
