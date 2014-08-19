package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;

public class ActivityTypeData implements ActivityType
{
	private Long Id;
	private String name;
	private DataSource source;
	private Set<Action> actions;

	protected ActivityTypeData(Long id, String name)
	{

	}

	public ActivityTypeData(String name)
	{
	}

	public Long getId ()
	{
		return new Long (this.id);
	}

	public String getName()
	{
		return new String (this.name);
	}

	public String toString()
	{
		return null;
	}

	public Set<Action> getActions ()
	{
		
	}

	protected addAction (Action action)
	{

	}
}