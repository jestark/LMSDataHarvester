package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

public interface ActivityManager extends DataStoreManager<Activity, Set<Activity>>
{
	@Override
	public abstract Set<Activity> fetchAll ();

	public abstract Set<Activity> fetchAllForType (ActivityType type);

	@Override
	public abstract Activity fetchById (Long id);

	public abstract Activity createEntity (ActivityType type, Course course);

	@Override
	public abstract Activity importEntity (Activity activity);

	@Override
	public abstract Activity importEntity (Activity activity, Boolean recursive);

	@Override
	public abstract void removeEntity (Activity activity);

	@Override
	public abstract void removeEntity (Activity activity, Boolean recursive);

	public abstract void setStealth (Activity activity, Boolean stealth);
}
