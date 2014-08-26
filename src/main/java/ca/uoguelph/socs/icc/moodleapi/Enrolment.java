package ca.uoguelph.socs.icc.moodleapi;

public interface Enrolment
{
	public long getId ();
	public String getName ();
	public Role getRole();
	public Course getCourse();
}
