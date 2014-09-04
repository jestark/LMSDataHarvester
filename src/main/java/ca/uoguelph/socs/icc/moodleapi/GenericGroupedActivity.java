package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivity<E extends GenericGroupedActivityMember> extends GenericNamedActivity
{
	private GenericActivityBaseGroup<E> inner;

	protected GenericGroupedActivity()
	{
		super ();
		this.inner = new GenericActivityBaseGroup<E> (this);
	}

	public GenericGroupedActivity(String name)
	{
		super (name);
		this.inner = new GenericActivityBaseGroup<E> (this);
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

	public Set<E> getChildren()
	{
		return inner.getChildren ();
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
