package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Action implements PersistentData
{
	private long id;
	private String name;
	private ActivityType atype;

	protected Action ()
	{
		this.id= -1;
		this.name = null;
		this.atype = null;
	}

	public Action (ActivityType type, String name)
	{
		this ();
		this.name = new String (name);
		this.atype = type;

		// Add the Action to the Activity Type (via a protected Method)
		this.atype.addAction (this);
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
				ebuilder.appendSuper (super.equals (obj));
				ebuilder.append (this.atype, ((Action) obj).atype);
				ebuilder.append (this.name, ((Action) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	public int hashcode ()
	{
		final int base = 11; // base value and multiplier for the hashcode, should be a prime number
		final int mult = 31; // and unique among classes in the domain model

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.atype);
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

	public ActivityType getActivityType ()
	{
		return this.atype;
	}

	protected void setActivityType (ActivityType atype)
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

	public String toString()
	{
		return this.name;
	}
}