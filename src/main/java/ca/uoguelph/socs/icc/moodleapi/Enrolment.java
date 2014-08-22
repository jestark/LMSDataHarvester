package ca.uoguelph.socs.icc.moodleapi;

public interface Enrolment extends PersistentData
{
	public Role getRole();
	public Course getCourse();
	public String toString();
}
