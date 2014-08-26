package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericActivity implements Activity
{
	private long id;
	private ActivityType atype;
	private ActivityInstance instance;

	protected GenericActivity ()
	{
		this.id = -1;
		this.atype = null;
		this.instance = null;
	}

	public GenericActivity (ActivityType atype)
	{
		this ();
		this.atype = atype;
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
				ebuilder.append (this.atype, ((GenericActivity) obj).atype);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashcode ()
	{
		final int base = 1013;
		final int mult = 991;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.atype);

		return hbuilder.toHashCode ();
	}

	@Override
	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	@Override
	public ActivityType getType ()
	{
		return this.atype;
	}

	protected void setType (ActivityType atype)
	{
		this.atype = atype;
	}

	@Override
	public ActivityInstance getInstance ()
	{
		return this.instance;
	}

	protected void setInstance (Activity instance)
	{
		this.instance = instance;
	}

	@Override
	public String getName ()
	{
		return this.atype.getName ();
	}

	@Override
	public String toString ()
	{
		return this.getName ();
	}
}