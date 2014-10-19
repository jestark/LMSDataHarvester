package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Set;

public interface Activity
{
	public String getName();
	public ActivityType getType();
	public Course getCourse ();
	public Boolean isStealth ();
	public Set<Grade> getGrades ();
	public List<LogEntry> getLog ();
}