package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;

public interface EnrolmentManager extends DataStoreManager<Enrolment, Set<Enrolment>>
{
	@Override
	public abstract Set<Enrolment> fetchAll ();

	public abstract Set<Enrolment> fetchAllForRole (Role role);
	
	@Override
	public abstract Enrolment fetchById (Long id);

	public abstract Enrolment createEntity (User user, Course course, Role role);

	@Override
	public abstract Enrolment importEntity (Enrolment enrolment);

	@Override
	public abstract Enrolment importEntity (Enrolment enrolment, Boolean recursive);

	@Override
	public abstract void removeEntity (Enrolment enrolment);

	@Override
	public abstract void removeEntity (Enrolment enrolment, Boolean recursive);

	public abstract void setFinalGrade (Enrolment enrolment, Integer grade);

	public abstract void setUsable (Enrolment enrolment, Boolean Usable);

	public abstract void addGrade (Enrolment enrolment, Activity activity, Integer grade);
	
	public abstract void addGrade (Enrolment enrolment, Grade grade);

	public abstract void removeGrade (Enrolment enrolment, Grade grade);
}
