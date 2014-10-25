package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

public interface ActivityTypeFactory extends DataStoreFactory<ActivityType, Set<ActivityType>>
{
	@Override
	public abstract Set<ActivityType> fetchAll ();

	@Override
	public abstract ActivityType fetchById (Long id);

	public abstract ActivityType fetchByName (ActivitySource source, String name);

	public abstract ActivityType createEntity (ActivitySource source, String name);

	@Override
	public abstract ActivityType importEntity (ActivityType type);

	@Override
	public abstract ActivityType importEntity (ActivityType type, Boolean recursive);

	@Override
	public abstract void removeEntity (ActivityType type);

	@Override
	public abstract void removeEntity (ActivityType type, Boolean recursive);

	public abstract void addAction (ActivityType type, Action action);

	public abstract void removeAction (ActivityType type, Action action);
}
