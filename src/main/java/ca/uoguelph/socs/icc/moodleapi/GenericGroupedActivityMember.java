package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivityMember<T> implements GenericActivityGroupMember<T>
{
	private long id;
	private String name;
	private T parent;

	protected GenericGroupedActivityMember()
	{
		this.id = -1;
		this.name = null;
		this.parent = null;
	}

	public GenericGroupedActivityMember(String name, GenericActivityGroup parent)
	{
		this ();
		this.name = name;
		this.parent = parent;
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
				ebuilder.append (this.name, ((GenericGroupedActivityMember) obj).name);
				ebuilder.append (this.parent, ((GenericGroupedActivityMember) obj).parent);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode()
	{
		final int base = 1031;
		final int mult = 971;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.parent);

		return hbuilder.toHashCode ();
	}

	@Override
	public long getId()
	{
		return this.id;
	}

	protected void setId(long id)
	{
		this.id = id;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	protected void setName(String name)
	{
		this.name = name;
	}

	@Override
	public ActivityType getType()
	{
		return this.parent.getType ();
	}

	@Override
	public ActivityInstance getInstance()
	{
		return this.parent.getInstance ();
	}

	@Override
	public T getParent()
	{
		return this.parent;
	}

	protected void setParent(T parent)
	{
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		return new String (this.parent + ": " + this.name);
	}
}
