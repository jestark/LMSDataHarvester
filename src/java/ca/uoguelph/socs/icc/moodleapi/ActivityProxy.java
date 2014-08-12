package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class ActivityProxy implements Activity
{
	protected ActivityProxy(String name, boolean gradable)
	{
	}

	public Course getCourse()
	{
		return null;
	}

	void setCourse(Course course)
	{
	}

	public ArrayList getActions()
	{
		return null;
	}

	public void addAction(Action action)
	{
	}

	public String getName()
	{
		return null;
	}

	public boolean isGradable()
	{
	return false;
	}

	public void setGradable(boolean gradable)
	{
	}

	public ArrayList getGrades()
	{
		return null;
	}

	public String toString()
	{
		return null;
	}

	public String getActivityName()
	{
		return null;
	}
}