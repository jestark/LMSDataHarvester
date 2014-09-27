package ca.uoguelph.socs.icc.moodleapi;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActivityInstance implements Serializable
{
	private Long id;
	private Boolean gradable;
	private Boolean stealth;
	private Course course;
	private Activity activity;
	private ActivityType type;
	private Set<Grade> grades;

	protected ActivityInstance ()
	{
		super ();
		this.id = null;
		this.type = null;
		this.course = null;
		this.activity = null;
		this.grades = null;

		this.gradable = new Boolean (false);
		this.stealth = new Boolean (false);
	}

	public ActivityInstance (Course course, ActivityType type, Activity activity, Boolean gradable, Boolean stealth)
	{
		super ();
		this.type = type;
		this.course = course;
		this.activity = activity;
		this.gradable = gradable;
		this.stealth = stealth;

		if (this.gradable)
		{
			this.grades = new HashSet<Grade> ();
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
				ebuilder.append (this.type, ((ActivityInstance) obj).type);
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
		hbuilder.append (this.type);
		hbuilder.append (this.course);
		hbuilder.append (this.activity);

		return hbuilder.toHashCode ();
	}

//	@Override
	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
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

	public ActivityType getType ()
	{
		return this.type;
	}

	protected void setType (ActivityType type)
	{
		this.type = type;
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

	public Set<Grade> getGrades ()
	{
		return new HashSet<Grade> (this.grades);
	}

	protected void setGrades (Set<Grade> grades)
	{
		this.grades = grades;
	}

	public void addGrade (Grade grade)
	{
		this.grades.add (grade);
	}

	public String getName ()
	{
		String name = this.type.getName ();

		if (this.activity != null)
		{
			name = this.activity.getName();
		}

		return name;
	}

	@Override
	public String toString ()
	{
		String string = this.getName ();

		if (this.stealth)
		{
			string = new String ("<" + string + ">");
		}

		if (this.gradable)
		{
			string = new String (string + ": Gradable (" + this.grades.size () + " entries)");
		}

		return string;
	}
}