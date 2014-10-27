package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface Course
{
	public abstract String getName ();
	public abstract Semester getSemester ();
	public abstract Integer getYear ();
	public abstract Set<Activity> getActivities ();
	public abstract Set<Enrolment> getEnrolments ();
}

