package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
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
	public List<LogEntry> getLog ();
}
