package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericNamedActivity implements Activity
{
	private Long id;
	private String name;
	private ActivityInstance instance;

	protected GenericNamedActivity ()
	{
		this.id = null;
		this.name = null;
		this.instance = null;
	}

	public GenericNamedActivity (String name)
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
				ebuilder.append (this.name, ((GenericNamedActivity) obj).name);

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
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	@Override
	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	@Override
	public ActivityType getType ()
	{
		return this.instance.getType ();
	}

	@Override
	public ActivityInstance getInstance ()
	{
		return this.instance;
	}

	protected void setInstance (ActivityInstance instance)
	{
		this.instance = instance;
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
	public String toString ()
	{
		return new String (this.getType () + ": " + this.name);
	}
}