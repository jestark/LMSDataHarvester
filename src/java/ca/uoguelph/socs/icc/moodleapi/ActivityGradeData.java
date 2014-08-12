package ca.uoguelph.socs.icc.moodleapi;

public class ActivityGradeData implements ActivityGrade
{
	private int grade;
	public Grade student;
	public Activity activity;

	ActivityGrade(int grade)
	{
	}

	ActivityGrade(Activity activity, Grade student, int grade)
	{
	}

	public Activity getActivity()
	{
		return null;
	}

	void setActivity(Activity activity)
	{
	}

	public int getGrade()
	{
		return 0;
	}

	public Grade getStudent()
	{
	}

	void setStudent(Grade student)
	{
	}

	public String toString()
	{
		return null;
	}
}