package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;

public interface Enrolment
{
	public String getName ();
	public Course getCourse();
	public Role getRole();
	public Boolean isUsable ();
	public Boolean isActive ();
	public Integer getFinalGrade ();
	public Grade getGrade (Activity activity);
	public Set<Grade> getGrades ();
}
