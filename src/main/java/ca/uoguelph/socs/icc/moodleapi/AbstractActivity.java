package ca.uoguelph.socs.icc.moodleapi;

public abstract class AbstractActivity implements Activity
{
	public abstract Long getId ()
	{
		return this.id;
	}
}