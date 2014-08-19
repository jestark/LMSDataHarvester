package ca.uoguelph.socs.icc.moodleapi;

public class ActionData implements PersistentData
{
	private Long id;
	private String name;
	private ActivityType atype;

	public ActionData (ActivityType type, String name)
	{
		this.name = new String (name);
		this.type = atype;

		// Add the Action to the Activity Type (via a protected Method)
	}

	public ActivityType getActivityType ()
	{
		return this.atype;
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
		return new String (this.name);
	}
}