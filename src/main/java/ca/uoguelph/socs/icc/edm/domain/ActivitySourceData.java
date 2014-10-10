package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActivitySourceData implements ActivitySource
{
	private Long id;
	private String name;
	private Set<ActivityType> types;

	protected ActivitySourceData ()
	{
		this.id = null;
		this.name = null;
		this.types = null;
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
				ebuilder.append (this.name, ((ActivitySourceData) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1097;
		final int mult = 883;

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

	@Override
	public String getName ()
	{
		return this.name;
	}

	protected void setName (String name)
	{
		this.name = name;
	}

	@Override
	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	protected void setTypes (Set<ActivityType> types)
	{
		this.types = types;
	}

	@Override
	public String toString ()
	{
		return this.name;
	}
}