package ca.uoguelph.socs.icc.moodleapi;

public abstract class AbstractActivity implements Activity
{
	private Long id;

	protected AbstractActivity ()
	{
		this.id = null;
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}
}