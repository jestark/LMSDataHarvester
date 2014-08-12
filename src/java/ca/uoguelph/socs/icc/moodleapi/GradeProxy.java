package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class GradeProxy implements Grade
{
	protected GradeProxy(Enrolment student)
	{
	}

	protected GradeProxy(Enrolment student, int grade)
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