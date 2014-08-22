package ca.uoguelph.socs.icc.moodleapi;

public class ActivityGrade implements PersistentData
{
	private Integer grade;
	private Grade student;
	private Activity activity;

	ActivityGrade (Activity activity, Grade student, Integer grade)
	{
	}

	public long getId ()
	{
		return 0;
	}

	public String getName ()
	{
		return new String ();
	}

	public String toString()
	{
		return new String ();
	}

	public Activity getActivity()
	{
		return this.activity;
	}

	public Integer getGrade()
	{
		return this.grade;
	}

	public Grade getStudent()
	{
		return this.student;
	}
}