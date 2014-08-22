package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;

public interface Activity extends LoggedActivity
{
	public Course getCourse();
//	public void setCourse(Course course);
	public String getName();
	public Boolean isGradable();
	public void setGradable(Boolean gradable);
	public Set<ActivityGrade> getGrades();
	public String toString();
	public String getActivityName();
}