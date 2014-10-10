package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.List;

public interface Course
{
	public String getName ();
	public Semester getSemester ();
	public Integer getYear ();
	public Set<Activity> getActivities ();
	public Set<Enrolment> getEnrolments ();
	public List<LogEntry> getLog ();
}

