package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Set;

public interface Activity
{
	public abstract String getName();
	public abstract ActivityType getType();
	public abstract Course getCourse ();
	public abstract Boolean isStealth ();
	public abstract Set<Grade> getGrades ();
	public abstract List<LogEntry> getLog ();
}
