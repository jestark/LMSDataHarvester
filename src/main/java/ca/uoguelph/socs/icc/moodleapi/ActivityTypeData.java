package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActivityTypeData implements ActivityType
{
	private long id;
	private String name;
	private Set<Action> actions;

	protected ActivityTypeData ()
	{
		this.id = -1;
		this.name = null;
		this.actions = new HashSet<Action> ();
	}

	protected ActivityTypeData(String name)
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
				ebuilder.appendSuper (super.equals (obj));
				ebuilder.append (this.name, ((ActivityTypeData) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 13; // base value and multiplier for the hashcode, should be a prime number
		final int mult = 37; // and unique among classes in the domain model

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

	public Set<Action> getActions ()
	{
		return new HashSet<Action> (this.actions);
	}

	protected void setActions (Set<Action> actions)
	{
		this.actions = actions;
	}

	protected void addAction (Action action)
	{
		this.actions.add (action);
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}