package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public class Grade implements PersistentData
{
	private Long id;
	private Integer finalgrade;
	private Set<ActivityGrade> grades;
	private Enrolment student;

	protected Grade(Enrolment student)
	{
		// add Grade to Enrolment

		this.student = student;
		this.finalgrade = new Integer (-1);
		this.grades = new HashSet ();
	}

	protected Grade(Enrolment student, Integer grade)
	{
		this.Grade (student);
		this.finalgrade = new Integer (grade);
	}

	public Integer getFinalGrade()
	{
		return new Integer (this.finalgrade);
	}

	public void setFinalGrade(Integer grade)
	{
		this.finalgrade = new Integer (grade);
	}

	public Set<ActivityGrade> getActivities()
	{
		return null;
	}

	public void addActivity(GradedActivity activity)
	{
	}

	public void getStudent()
	{
		return this.student;
	}

	public String toString()
	{
		return new String ();
	}
}