package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;

public interface ActivitySourceFactory extends DataStoreFactory<ActivitySource, Set<ActivitySource>>
{
	@Override
	public abstract Set<ActivitySource> fetchAll ();

	@Override
	public abstract ActivitySource fetchById (Long id);

	public abstract ActivitySource fetchByName (String name);
	
	public abstract ActivitySource createEntity (String name);

	@Override
	public abstract ActivitySource importEntity (ActivitySource source);

	@Override
	public abstract ActivitySource importEntity (ActivitySource source, Boolean recursive);

	@Override
	public abstract void removeEntity (ActivitySource source);

	@Override
	public abstract void removeEntity (ActivitySource source, Boolean recursive);
}
