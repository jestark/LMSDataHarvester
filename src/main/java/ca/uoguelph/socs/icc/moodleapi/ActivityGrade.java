package ca.uoguelph.socs.icc.moodleapi;

public class ActivityGrade implements PersistentData
{
	private Integer grade;
	private Grade student;
	private Activity activity;

	ActivityGrade (Activity activity, Grade student, Integer grade)
	{
	}

	public Long getId ()
	{

	}

	public String getName ()
	{

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
		return new Integer (this.grade);
	}

	public Grade getStudent()
	{
		return this.student;
	}
}