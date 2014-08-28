package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Action
{
	private Long id;
	private String name;
	private ActivityType atype;

	protected Action ()
	{
		this.id= null;
		this.name = null;
		this.atype = null;
	}

	public Action (ActivityType type, String name)
	{
		this ();
		this.name = new String (name);
		this.atype = type;
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
				ebuilder.append (this.atype, ((Action) obj).atype);
				ebuilder.append (this.name, ((Action) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1051;
		final int mult = 941;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.atype);
		hbuilder.append (this.name);

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

	public ActivityType getType ()
	{
		return this.atype;
	}

	protected void setType (ActivityType atype)
	{
		this.atype = atype;
	}

	public String getName()
	{
		return this.name;
	}

	protected void setName (String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}