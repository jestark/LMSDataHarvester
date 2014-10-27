package ca.uoguelph.socs.icc.edm.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EnrolmentData implements Enrolment, Serializable
{
	private Long id;
	private Course course;
	private Role role;
	protected Integer finalgrade;
	protected Boolean usable;
	protected Set<Grade> grades;
	protected List<LogEntry> log;

	protected EnrolmentData ()
	{
		this.id = null;
		this.log = null;
		this.role = null;
		this.course = null;
		this.usable = new Boolean (false);
		this.finalgrade = null;
		this.grades = null;
	}

	protected EnrolmentData (Course course, Role role)
	{
		this ();
		this.role = role;
		this.course = course;
		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
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
				ebuilder.append (this.course, ((AbstractEnrolment) obj).course);
				ebuilder.append (this.role, ((AbstractEnrolment) obj).role);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1091;
		final int mult = 907;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.course);
		hbuilder.append (this.role);

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
	public Course getCourse()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	@Override
	public Role getRole()
	{
		return this.role;
	}

	protected void setRole (Role role)
	{
		this.role = role;
	}

	@Override
	public String getName ()
	{
		String result = new String ("(unset)");
		Long id = this.getId ();

		if (id != null)
		{
			result = id.toString ();
		}

		return result;
	}

	@Override
	public Grade getGrade (Activity activity)
	{
		Grade result = null;

		for (Grade i : this.grades)
		{
			if (activity == i.getActivity ())
			{
				result = i;
				break;
			}
		}

		return result;
	}

	@Override
	public Set<Grade> getGrades ()
	{
		return new HashSet<Grade> (this.grades);
	}

	protected void setGrades (Set<Grade> grades)
	{
		this.grades = grades;
	}

	protected boolean addGrade (Grade grade)
	{
		return this.grades.add (grade);
	}

	@Override
	public Integer getFinalGrade ()
	{
		return this.finalgrade;
	}

	protected void setFinalGrade (Integer finalgrade)
	{
		this.finalgrade = finalgrade;
	}

	@Override
	public Boolean isUsable ()
	{
		return this.usable;
	}

	protected void setUsable (Boolean usable)
	{
		this.usable =usable;
	}

	@Override
	public List<LogEntry> getLog ()
	{
		return this.log;
	}

	protected void setLog (List<LogEntry> log)
	{
		this.log = log;
	}

	protected boolean addLog (LogEntry entry)
	{
		return this.log.add (entry);
	}

	@Override
	public String toString ()
	{
		return new String ((this.getCourse ()).toString () + ": " + this.getName ());
	}
}
