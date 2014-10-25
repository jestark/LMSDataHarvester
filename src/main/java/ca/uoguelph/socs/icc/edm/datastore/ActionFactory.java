package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Action;

public interface ActionFactory extends DataStoreFactory<Action, Set<Action>>
{
	@Override
	public abstract Set<Action> fetchAll ();

	@Override
	public abstract Action fetchById (Long id);

	public abstract Action fetchByName (String name);
	
	public abstract Action createEntity (String name);

	@Override
	public abstract Action importEntity (Action action);

	@Override
	public abstract Action importEntity (Action action, Boolean recursive);

	@Override
	public abstract void removeEntity (Action action);

	@Override
	public abstract void removeEntity (Action action, Boolean recursive);
}
