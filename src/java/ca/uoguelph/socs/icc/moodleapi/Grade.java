package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public interface Grade
{
	public int getFinalGrade();
	public void setFinalGrade(int grade);
	public ArrayList getActivities();
	public void addActivity(GradedActivity activity);
	public void getStudent();
	public String toString();
}