package ca.uoguelph.socs.icc.moodleapi;

public class Role
{
	private long id;
	private String name;

	protected Role ()
	{
		this.id = -1;
		this.name = new String ("UNSET");
	}

	protected Role(String name)
	{
		this ();
		this.name = name;
	}

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	protected void setName (String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return this.name;
	}
}