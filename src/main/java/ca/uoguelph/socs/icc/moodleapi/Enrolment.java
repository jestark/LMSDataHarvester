package ca.uoguelph.socs.icc.moodleapi;

public interface Enrolment
{
	public Role getRole();
	public Course getCourse();
	public void setCourse(Course course);
	public String toString();
}
