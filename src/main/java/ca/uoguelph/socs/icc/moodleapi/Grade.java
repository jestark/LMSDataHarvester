package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public class Grade
{
	private long id;
	private Integer finalgrade;
	private Set<ActivityGrade> grades;
	private Enrolment student;

	protected Grade ()
	{
		this.id = -1;
		this.finalgrade = new Integer (-1);
		this.grades = new HashSet<ActivityGrade>();
	}

	protected Grade(Enrolment student)
	{
		this ();
		this.student = student;

		// add Grade to Enrolment
	}

	protected Grade(Enrolment student, Integer grade)
	{
		this(student);
		this.finalgrade = new Integer (grade);
	}

	public long getId()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.student.getName();
	}

	public Integer getFinalGrade()
	{
		return this.finalgrade;
	}

	public void setFinalGrade(Integer grade)
	{
		this.finalgrade = grade;
	}

	public Set<ActivityGrade> getActivities()
	{
		return null;
	}

	public void addActivity(ActivityGrade activity)
	{
	}

	public Enrolment getStudent()
	{
		return this.student;
	}

	public String toString()
	{
		return new String ();
	}
}