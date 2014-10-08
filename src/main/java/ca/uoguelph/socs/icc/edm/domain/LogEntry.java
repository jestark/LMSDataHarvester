package ca.uoguelph.socs.icc.edm.domain;

import java.util.Date;

public interface LogEntry
{
	public Long getId();
	public Enrolment getEnrolment();
	public Course getCourse();
	public Activity getActivity();
	public Action getAction();
	public Date getTime();
}