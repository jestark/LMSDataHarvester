package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivity<E extends GenericActivityGroupMember> extends GenericNamedActivity implements Activity, GenericActivityGroup<E>
{
	private Set<E> children;

	protected GenericGroupedActivity()
	{
		super ();
		this.children = null;
	}

	public GenericGroupedActivity(ActivityType type, String name)
	{
		super (type, name);
		this.children = new HashSet<E> ();
	}

	@Override
	public boolean equals(Object obj)
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

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode()
	{
		final int base = 1021;
		final int mult = 977;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	@Override
	public Set<E> getChildren()
	{
		return new HashSet<E> (this.children);
	}

	protected void setChildren(Set<E> children)
	{
		this.children = children;
	}

	@Override
	public void addChild(E child)
	{
		this.children.add (child);
	}
}
