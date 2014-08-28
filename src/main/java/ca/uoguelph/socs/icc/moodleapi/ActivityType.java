package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActivityType
{
	private Long id;
	private String name;
	private Set<Action> actions;

	protected ActivityType ()
	{
		this.id = null;
		this.name = null;
		this.actions = null;
	}

	protected ActivityType (String name)
	{
		this ();
		this.name = name;
		this.actions = new HashSet<Action> ();
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
				ebuilder.append (this.name, ((ActivityType) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1009;
		final int mult = 997;

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

	public String getName ()
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

	public void addAction (Action action)
	{
		this.actions.add (action);
	}

	@Override
	public String toString ()
	{
		return this.name;
	}
}