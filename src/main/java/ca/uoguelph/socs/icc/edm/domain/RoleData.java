package ca.uoguelph.socs.icc.edm.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RoleData implements Role
{
	private Long id;
	private String name;

	protected RoleData ()
	{
		this.id = null;
		this.name = null;
	}

	protected RoleData (String name)
	{
		this ();
		this.name = name;
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
				ebuilder.append (this.name, ((RoleData) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1069;
		final int mult = 919;

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