package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.Role;

public interface RoleFactory extends DataStoreFactory<Role, Set<Role>>
{
	@Override
	public abstract Set<Role> fetchAll ();

	@Override
	public abstract Role fetchById (Long id);

	public abstract Role fetchByName (String name);
	
	public abstract Role createEntity (String name);

	@Override
	public abstract Role importEntity (Role role);

	@Override
	public abstract Role importEntity (Role role, Boolean recursive);

	@Override
	public abstract void removeEntity (Role role);

	@Override
	public abstract void removeEntity (Role role, Boolean recursive);
}
