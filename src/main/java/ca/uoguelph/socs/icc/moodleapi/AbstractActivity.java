package ca.uoguelph.socs.icc.moodleapi;

public abstract class AbstractActivity implements Activity
{
	protected AbstractActivity () {}
	protected abstract boolean addGrade (Grade grade);
	protected abstract boolean addLog (LogEntry entry);
}