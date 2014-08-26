package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivityGroup<T, E> extends GrenericGroupedActivityMember<T> implements GenericActivityGroup<E>, GenericActivityGroupMember<T>
{
	private Set<E> children;

	protected GenericGroupedActivityGroup ()
	{
		super ();
		this.children = null;
	}

	public GenericGroupedActivityGroup(String name, GenericActivityGroup parent)
	{
		super (name, parent);
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
		final int base = 1033;
		final int mult = 967;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	@Override
	public Set<E> getChildren()
	{
		return this.children;
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
