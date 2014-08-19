package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public class ActivityData implements Activity
{
	private Long id;
	private Boolean gradable;
	private Course course;
	private ActivityType type;
	private Set<ActivityGrade> grades;

	protected ActivityData (ActivityType type, Course course)
	{
		this.course = course;
		this.type = type;
		this.gradable = new Boolean (false);
		this.grades = null;
	}

	protected ActivityData(ActivityType type, Course course, Boolean gradable)
	{
		this.ActivityData(type, course);

		if (gradable)
		{
			this.gradable = true;
			this.grades = new HashSet ();
		}
	}

	public Long getId ()
	{
		return new Long (this.id);
	}

	public String getName()
	{
		return this.type.getName();
	}

	public Course getCourse()
	{
		return this.course;
	}

	void setCourse(Course course)
	{
	}

	public Boolean isGradable()
	{
		return this.gradable;
	}

	public void setGradable(Boolean gradable)
	{
	}

	public Set<ActivityGrade> getGrades()
	{
		return null;
	}

	public String toString()
	{
		return null;
	}

	public String getActivityName()
	{
		return null;
	}
}