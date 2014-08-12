package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class GradeData implements Grade
{
	private int grade;
	private ArrayList activitygrades;
	private Enrolment student;

	protected GradeData(Enrolment student)
	{
	}

	protected Grade(Enrolment student, int grade)
	{
	}

	public int getFinalGrade()
	{
		return 0;
	}

	public void setFinalGrade(int grade)
	{
	}

	public ArrayList getActivities()
	{
		return null;
	}

	public void addActivity(GradedActivity activity)
	{
	}

	public void getStudent()
	{
	}

	public String toString()
	{
		return null;
	}
}