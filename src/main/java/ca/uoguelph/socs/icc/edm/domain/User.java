package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface User
{
	public abstract Integer getIdNumber ();
	public abstract String getFirstname ();
	public abstract String getLastname ();
	public abstract String getUsername ();
	public abstract String getName ();
	public abstract Enrolment getEnrolment (Course course);
	public abstract Set<Enrolment> getEnrolments ();
}

