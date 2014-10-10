package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface User
{
	public Integer getIdNumber ();
	public String getFirstname ();
	public String getLastname ();
	public String getUsername ();
	public String getName ();
	public Enrolment getEnrolment (Course course);
	public Set<Enrolment> getEnrolments ();
}

