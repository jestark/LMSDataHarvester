package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Collection;

public interface DataStoreManager<T, C extends Collection<T>>
{
	public abstract T fetchById (Long id);
	public abstract C fetchAll ();
	public abstract T importEntity (T entity);
	public abstract T importEntity (T entity, Boolean recursive);
	public abstract void removeEntity (T entity);
	public abstract void removeEntity (T entity, Boolean recursive);
}
