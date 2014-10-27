package ca.uoguelph.socs.icc.edm.domain;

public interface Grade
{
	public abstract String getName ();
	public abstract Activity getActivity ();
	public abstract Enrolment getEnrolment ();
	public abstract Integer getGrade ();
}
