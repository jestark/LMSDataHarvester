package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActivityInstance
{
	private long id;
	private Boolean gradable;
	private Boolean stealth;
	private Course course;
	private Activity activity;
	private Set<ActivityGrade> grades;

	protected ActivityInstance ()
	{
		this.id = -1;
		this.gradable = new Boolean (false);
		this.stealth = new Boolean (false);
		this.course = null;
		this.activity = null;
		this.grades = null;
	}

	public ActivityInstance (Course course, Activity activity, Boolean gradable, Boolean stealth)
	{
		this ();
		this.course = course;
		this.activity = activity;
		this.gradable = gradable;
		this.stealth = stealth;

		if (this.gradable)
		{
			this.grades = new HashSet<ActivityGrade> ();
		}
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.appendSuper (super.equals (obj));
				ebuilder.append (this.course, ((ActivityInstance) obj).course);
				ebuilder.append (this.activity, ((ActivityInstance) obj).activity);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1039;
		final int mult = 953;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.course);
		hbuilder.append (this.activity);

		return hbuilder.toHashCode ();
	}

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public Course getCourse ()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	public Activity getActivity ()
	{
		return this.activity;
	}

	protected void setActivity (Activity activity)
	{
		this.activity = activity;
	}

	public Boolean isGradable ()
	{
		return this.gradable;
	}

	public void setGradable (Boolean gradable)
	{
		this.gradable = gradable;
	}

	public Boolean isStealth ()
	{
		return this.stealth;
	}

	public void setStealth (Boolean stealth)
	{
		this.gradable = stealth;
	}

	public Set<ActivityGrade> getGrades ()
	{
		return new HashSet<ActivityGrade> (this.grades);
	}

	protected void setGrades (Set<ActivityGrade> grades)
	{
		this.grades = grades;
	}

	public void addGrade (ActivityGrade grade)
	{
		this.grades.add (grade);
	}

	public String getName ()
	{
		return this.activity.getName();
	}

	@Override
	public String toString ()
	{
		String string = this.activity.toString ();

		if (this.stealth)
		{
			string = new String ("<" + string + ">");
		}

		if (this.gradable)
		{
			string = new String (string + ": Gradable (" + this.grades.size + " entries)"
		}

		return string;
	}
}