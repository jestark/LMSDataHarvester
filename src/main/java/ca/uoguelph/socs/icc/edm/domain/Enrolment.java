package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Set;

public interface Enrolment
{
	public abstract String getName ();
	public abstract Course getCourse();
	public abstract Role getRole();
	public abstract Boolean isUsable ();
	public abstract Integer getFinalGrade ();
	public abstract Grade getGrade (Activity activity);
	public abstract Set<Grade> getGrades ();
	public abstract List<LogEntry> getLog ();
}
