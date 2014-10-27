package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CourseData implements Course
{
	private Long id;
	private String name;
	private Semester semester;
	private Integer year;
	private Set<Activity> activities;
	private Set<Enrolment> enrolments;

	protected CourseData ()
	{
		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;
		this.activities = null;
		this.enrolments = null;
	}

	public CourseData (String name, Semester semester, Integer year)
	{
		this();
		this.name = name;
		this.semester = semester;
		this.year = year;

		this.activities = new HashSet<Activity> ();
		this.enrolments = new HashSet<Enrolment> ();
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
				ebuilder.append (this.name, ((CourseData) obj).name);
				ebuilder.append (this.year, ((CourseData) obj).year);
				ebuilder.append (this.semester, ((CourseData) obj).semester);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1061;
		final int mult = 937;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.year);
		hbuilder.append (this.semester);

		return hbuilder.toHashCode ();
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

	protected void setSemester (Semester semester)
	{
		this.semester = semester;
	}

	@Override
	public Integer getYear ()
	{
		return this.year;
	}

	protected void setYear (Integer year)
	{
		this.year = year;
	}

	@Override
	public Set<Activity> getActivities ()
	{
		return new HashSet<Activity> (this.activities);
	}

	protected void setActivities (Set<Activity> activities)
	{
		this.activities = activities;
	}

	protected boolean addActivity (Activity activity)
	{
		return this.activities.add (activity);
	}

	@Override
	public Set<Enrolment> getEnrolments ()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	protected boolean addEnrolment (Enrolment enrolment)
	{
		return this.enrolments.add (enrolment);
	}

	@Override
	public String toString()
	{
		return new String (this.name + ": " + this.semester + ", " + this.year);
	}
}
