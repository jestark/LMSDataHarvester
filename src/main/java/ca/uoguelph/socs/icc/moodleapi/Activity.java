package ca.uoguelph.socs.icc.moodleapi;

public interface Activity
{
	public long getId();
	public String getName();
	public ActivityType getType();
	public ActivityInstance getInstance();
}