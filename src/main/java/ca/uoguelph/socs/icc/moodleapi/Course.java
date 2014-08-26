package ca.uoguelph.socs.icc.moodleapi;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Course
{
	private long id;
	private String name;
	private Semester semester;
	private Integer year;
	private Set<ActivityInstance> activities;
	private Set<Enrolment> enrolments;
	private List<LogEntry> logentries;

	protected Course ()
	{
		this.id = -1;
		this.name = null;
		this.semester = null;
		this.year = null;
		this.activities = null;
		this.enrolments = null;
		this.logentries = null;
	}

	public Course (String name, Semester semester, Integer year)
	{
		this();
		this.name = name;
		this.semester = semester;
		this.year = year;

		this.activities = new HashSet<ActivityInstance> ();
		this.enrolments = new HashSet<Enrolment> ();
		this.logentries = new ArrayList<LogEntry> ();
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
				ebuilder.append (this.name, ((Course) obj).name);
				ebuilder.append (this.year, ((Course) obj).year);
				ebuilder.append (this.semester, ((Course) obj).semester);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashcode ()
	{
		final int base = 1061;
		final int mult = 937;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.year);
		hbuilder.append (this.semester);

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

	public String getName ()
	{
		return this.name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	public Semester getSemester ()
	{
		return this.semester;
	}

	public void setSemester (Semester semester)
	{
		this.semester = semester;
	}

	public Integer getYear ()
	{
		return this.year;
	}

	public void setYear (Integer year)
	{
		this.year = year;
	}

	public Set<ActivityInstance> getActivities ()
	{
		return new HashSet<ActivityInstance> (this.activities);
	}

	protected void setActivities (Set<ActivityInstance> activities)
	{
		this.activities = activities;
	}

	public void addActivity (ActivityInstance activity)
	{
		this.activities.add (activity);
	}

	public Set<Enrolment> getEnrolments ()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	public void addEnrolment (Enrolment enrolment)
	{
		this.enrolments.add (enrolment);
	}

	public List<LogEntry> getLog ()
	{
		return new ArrayList<LogEntry> (this.logentries);
	}

	protected void setLog (List<LogEntry> log)
	{
		this.logentries = log;
	}

	public void addLogEntry (LogEntry entry)
	{
		this.logentries.add (entry);
	}

	@Override
	public String toString()
	{
		return new String (this.name + ": " + this.semester + ", " + this.year);
	}
}