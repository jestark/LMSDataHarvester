package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivityMember<T extends AbstractActivity> extends AbstractActivity
{
	private Long id;
	private T parent;

	protected GenericGroupedActivityMember()
	{
		super ();
		this.id = null;
		this.parent = null;
	}

	public GenericGroupedActivityMember(String name, T parent)
	{
		super (name);
		this.id = null;
		this.parent = parent;
	}

	@Override
	public boolean equals(Object obj)
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
				ebuilder.append (this.parent, ((GenericGroupedActivityMember) obj).parent);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode()
	{
		final int base = 1031;
		final int mult = 971;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());
		hbuilder.append (this.parent);

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

	@Override
	public Course getCourse ()
	{
		return this.parent.getCourse ();
	}

	@Override
	public ActivityType getType ()
	{
		return this.parent.getType ();
	}

	@Override
	public Boolean isGradable ()
	{
		return this.parent.isGradable ();
	}

	@Override
	public Boolean isStealth ()
	{
		return this.parent.isStealth ();
	}

	@Override
	public Set<ActivityGrade> getGrades ()
	{
		return this.parent.getGrades ();
	}

	@Override
	public void addGrade (ActivityGrade grade)
	{
		this.parent.addGrade (grade);
	}

	public T getParent()
	{
		return this.parent;
	}

	protected void setParent(T parent)
	{
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		return new String (this.parent + ": " + this.getName ());
	}
}
