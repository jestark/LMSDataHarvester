package ca.uoguelph.socs.icc.edm.domain;

import java.util.Date;

public interface LogEntry
{
	public abstract Enrolment getEnrolment();
	public abstract Course getCourse();
	public abstract Activity getActivity();
	public abstract Action getAction();
	public abstract Date getTime();
	public abstract String getIPAddress ();
}
