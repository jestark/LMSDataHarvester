package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class AbstractNamedActivity extends AbstractActivity implements Activity
{
	private String name;

	protected AbstractNamedActivity ()
	{
		super ();
		this.name = null;
	}

	public AbstractNamedActivity (String name)
	{
		super ();
		this.name = name;
	}

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
				ebuilder.append (this.name, ((AbstractNamedActivity) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	public int hashCode ()
	{
		final int base = 1013;
		final int mult = 991;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	public String getName ()
	{
		return this.name;
	}

	protected void setName (String name)
	{
		this.name = name;
	}
}