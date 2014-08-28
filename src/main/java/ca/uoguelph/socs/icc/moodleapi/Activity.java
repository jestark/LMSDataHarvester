package ca.uoguelph.socs.icc.moodleapi;

public interface Activity
{
	public Long getId();
	public String getName();
	public ActivityType getType();
	public ActivityInstance getInstance();
}