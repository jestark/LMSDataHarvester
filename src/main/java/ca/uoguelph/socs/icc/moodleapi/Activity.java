package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public interface Activity extends LoggedActivity
{
	public Course getCourse();
//	public void setCourse(Course course);
	public ArrayList getActions();
	public void addAction(Action action);
	public String getName();
	public boolean isGradable();
	public void setGradable(boolean gradable);
	public ArrayList getGrades();
	public String toString();
	public String getActivityName();
}