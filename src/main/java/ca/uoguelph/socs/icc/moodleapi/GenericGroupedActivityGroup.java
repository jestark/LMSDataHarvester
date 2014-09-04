package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivityGroup<T extends AbstractNamedActivity, E extends GenericGroupedActivityMember> extends GenericGroupedActivityMember<T>
{
	private GenericActivitySubGroup<E> inner;

	protected GenericGroupedActivityGroup ()
	{
		super ();
		this.inner = new GenericActivitySubGroup<E> (this);
	}

	public GenericGroupedActivityGroup(String name, T parent)
	{
		super (name, parent);
		this.inner = new GenericActivitySubGroup<E> (this);
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

	public Set<E> getChildren()
	{
		return this.inner.getChildren ();
	}

	protected void setChildren(Set<E> children)
	{
		this.inner.setChildren (children);
	}

	public void addChild(E child)
	{
		this.inner.addChild (child);
	}
}
