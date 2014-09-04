package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public abstract class GenericActivityGroup<E extends GenericGroupedActivityMember> implements Activity
{
	private Set<E> children;

	protected GenericActivityGroup ()
	{
		this.children = new HashSet<E> ();
	}

	protected abstract AbstractNamedActivity getOuter ();

	@Override
	public boolean equals (Object obj)
	{
		return (this.getOuter ()).equals (obj);
	}

	@Override
	public int hashCode ()
	{
		return (this.getOuter ()).hashCode ();
	}

	public Long getId ()
	{
		return (this.getOuter ()).getId ();
	}

	protected void setId (Long id)
	{
		(this.getOuter ()).setId (id);
	}

	@Override
	public String getName ()
	{
		return (this.getOuter ()).getName ();
	}

	protected void setName (String name)
	{
		(this.getOuter ()).setName (name);
	}

	@Override
	public Course getCourse ()
	{
		return (this.getOuter ()).getCourse ();
	}

	@Override
	public ActivityType getType ()
	{
		return (this.getOuter ()).getType ();
	}

	@Override
	public Boolean isGradable ()
	{
		return (this.getOuter ()).isGradable ();
	}

	@Override
	public Boolean isStealth ()
	{
		return (this.getOuter ()).isStealth ();
	}

	@Override
	public Set<ActivityGrade> getGrades ()
	{
		return (this.getOuter ()).getGrades ();
	}

	@Override
	public void addGrade (ActivityGrade grade)
	{
		(this.getOuter ()).addGrade (grade);
	}

	public Set<E> getChildren()
	{
		return new HashSet<E> (this.children);
	}

	protected void setChildren (Set<E> children)
	{
		this.children = children;
	}

	public void addChild(E child)
	{
		this.children.add (child);
	}

	@Override
	public String toString ()
	{
		return (this.getOuter ()).toString ();
	}
}
