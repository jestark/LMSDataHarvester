package ca.uoguelph.socs.icc.moodleapi;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericNamedActivity extends AbstractNamedActivity implements Serializable
{
	private ActivityInstance instance;

	protected GenericNamedActivity ()
	{
		super ();
		this.instance = null;
	}

	public GenericNamedActivity (String name, ActivityInstance instance)
	{
		super (name);
		this.instance = instance;
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

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1019;
		final int mult = 983;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	@Override
	public Course getCourse ()
	{
		return this.instance.getCourse ();
	}

	@Override
	public ActivityType getType ()
	{
		return this.instance.getType ();
	}

	@Override
	public Boolean isGradable ()
	{
		return this.instance.isGradable ();
	}

	@Override
	public Boolean isStealth ()
	{
		return this.instance.isStealth ();
	}

	@Override
	public Set<Grade> getGrades ()
	{
		return this.instance.getGrades ();
	}

	@Override
	protected boolean addGrade (Grade grade)
	{
		return this.instance.addGrade (grade);
	}

	public ActivityInstance getInstance ()
	{
		return this.instance;
	}

	protected void setInstance (ActivityInstance instance)
	{
		this.instance = instance;
	}

	@Override
	public List<LogEntry> getLog ()
	{
		return this.instance.getLog ();
	}

	@Override
	protected boolean addLog (LogEntry entry)
	{
		return this.instance.addLog (entry);
	}

	@Override
	public String toString ()
	{
		return new String (this.getType () + ": " + this.getName ());
	}
}