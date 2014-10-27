package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Semester;

public interface CourseManager extends DataStoreManager<Course, Set<Course>>
{
	@Override
	public abstract Set<Course> fetchAll ();

	public abstract Set<Course> fetchAllForOffering (Semester semester, Integer year);
	
	public abstract Set<Course> fetchAllForOffering (String name, Semester semester, Integer year);
	
	@Override
	public abstract Course fetchById (Long id);

	public abstract Course fetchByOffering (String name, Semester semester, Integer year);
	
	public abstract Course createEntity (String name, Semester semester, Integer year);

	@Override
	public abstract Course importEntity (Course course);

	@Override
	public abstract Course importEntity (Course course, Boolean recursive);

	@Override
	public abstract void removeEntity (Course course);

	@Override
	public abstract void removeEntity (Course course, Boolean recursive);

}
