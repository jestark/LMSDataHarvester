package ca.uoguelph.socs.icc.moodleapi;

public class RoleData implements PersistentData
{
	private Long id;
	private String name;

	protected RoleData(String name)
	{
		this.id = new Long (-1);
		this.name = new String (name);
	}

	protected RoleData(Long id, String name)
	{
		this.id = new Long (id);
		this.name = new String (name);
	}

	@Override
	public Long getId ()
	{
		return new Long (this.id);
	}

	@Override
	public String getName()
	{
		return new String (this.Name);
	}

	@Override
	public String toString()
	{
		return new String (this.name);
	}
}