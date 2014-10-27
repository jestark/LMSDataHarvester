package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Set;
import ca.uoguelph.socs.icc.edm.domain.User;

public interface UserManager extends DataStoreManager<User, Set<User>>
{
	@Override
	public abstract Set<User> fetchAll ();

	@Override
	public abstract User fetchById (Long id);

	public abstract User fetchByIdNumber (Integer idnumber);
	
	public abstract User fetchByUsername (String Username);

	public abstract User createEntity (Integer idnumber, String username, String firstname, String lastname);

	@Override
	public abstract User importEntity (User user);

	@Override
	public abstract User importEntity (User user, Boolean recursive);

	@Override
	public abstract void removeEntity (User user);

	@Override
	public abstract void removeEntity (User user, Boolean recursive);

}
