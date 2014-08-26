package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Role
{
	private long id;
	private String name;

	protected Role ()
	{
		this.id = -1;
		this.name = null;
	}

	protected Role(String name)
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
				ebuilder.append (this.name, ((Role) obj).name);

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

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
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