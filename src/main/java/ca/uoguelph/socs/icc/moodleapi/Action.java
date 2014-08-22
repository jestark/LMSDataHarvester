package ca.uoguelph.socs.icc.moodleapi;

public class Action implements PersistentData
{
	private long id;
	private String name;
	private ActivityType atype;

	public Action (ActivityType type, String name)
	{
		this.name = new String (name);
		this.atype = type;

		// Add the Action to the Activity Type (via a protected Method)
	}

	public ActivityType getActivityType ()
	{
		return this.atype;
	}

	public long getId ()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String toString()
	{
		return this.name;
	}
}