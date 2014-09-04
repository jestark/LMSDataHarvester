package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;

public interface Activity
{
	public String getName();
	public ActivityType getType();
	public Course getCourse ();
	public Boolean isGradable ();
	public Boolean isStealth ();
	public Set<ActivityGrade> getGrades ();
	public void addGrade (ActivityGrade grade);
}