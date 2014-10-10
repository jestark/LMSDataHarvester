package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActionData implements Action
{
	private Long id;
	private String name;
	private Set<ActivityType> types;

	protected ActionData ()
	{
		this.id= null;
		this.name = null;
		this.types = null;
	}

	public ActionData (String name)
	{
		this ();
		this.name = new String (name);
		this.types = new HashSet<ActivityType> ();
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
				ebuilder.append (this.name, ((ActionData) obj).name);

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

	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	protected void setTypes (Set<ActivityType> types)
	{
		this.types = types;
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