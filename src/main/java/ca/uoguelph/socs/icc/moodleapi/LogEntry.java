package ca.uoguelph.socs.icc.moodleapi;

import java.util.Date;

public interface LogEntry
{
	public long getId();
	public Enrolment getEnrolment();
	public Course getCourse();
	public ActivityInstance getActivity();
	public Action getAction();
	public Date getTime();
}