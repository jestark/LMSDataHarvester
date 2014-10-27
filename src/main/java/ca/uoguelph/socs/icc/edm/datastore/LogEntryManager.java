package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Date;
import java.util.List;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

public interface LogEntryManager extends DataStoreManager<LogEntry, List<LogEntry>>
{
	@Override
	public abstract List<LogEntry> fetchAll ();

	public abstract List<LogEntry> fetchAllforCourse (Course course);

	@Override
	public abstract LogEntry fetchById (Long id);

	public abstract LogEntry createEntity (Enrolment enrolment, Activity activity, Action action);

	public abstract LogEntry createEntity (Enrolment enrolment, Activity activity, Action action, Date time);

	public abstract LogEntry createEntity (Enrolment enrolment, Activity activity, Action action, String ip);

	public abstract LogEntry createEntity (Enrolment enrolment, Activity activity, Action action, Date time, String ip);

	@Override
	public abstract LogEntry importEntity (LogEntry entry);

	@Override
	public abstract LogEntry importEntity (LogEntry entry, Boolean recursive);

	@Override
	public abstract void removeEntity (LogEntry entry);

	@Override
	public abstract void removeEntity (LogEntry entry, Boolean recursive);

	public abstract void setTime (LogEntry entry, Date time);

	public abstract void setIPAddress (LogEntry entry, String ip);
}
