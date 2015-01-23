/* Copyright (C) 2014 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.factory.MappedFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.BaseMappedFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.CachedMappedFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The domain model interface represented by this factory
 * @param   <X> The Manager interface created by this factory
 */

public abstract class AbstractManagerFactory<T extends Element, X extends ElementManager<T>, Y extends ElementBuilder<T>, Z>
{
	/** The logger */
	private final Log log;

	/** The Domain Model Type for this factory */
	private final DomainModelType type;

	/** Caching factory for the <code>ElementManager</code> implementations */
	private final MappedFactory<Class<? extends X>, X, DomainModel> managerfactories;

	/** Caching factory for the <code>ElementBuilder</code> implementations */
	private final MappedFactory<Class<? extends Y>, Y, DomainModel> builderfactories;

	/** Caching factory for the <code>DataStoreQuery</code> instances */
	private final MappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> queryfactories;

	/** <code>Element<code> to <code>ElementManager</code> implementation mapping */
	private final Map<Class<? extends T>, Class<? extends X>> elementmanagers;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private final Map<Class<? extends T>, Class<? extends Y>> elementbuilders;

	/** <code>ElementFactory</code> implementations */
	private final Map<Class<? extends T>, Z> elementfactories;

	/**
	 * Create the <code>AbstractManagerFactory</code>.
	 *
	 * @param  type 
	 */

	protected AbstractManagerFactory (DomainModelType type)
	{
		this.log = LogFactory.getLog (AbstractManagerFactory.class);

		this.type = type;

		this.managerfactories = new CachedMappedFactory<Class<? extends X>, X, DomainModel> (new BaseMappedFactory<Class<? extends X>, X, DomainModel> ());
		this.builderfactories = new BaseMappedFactory<Class<? extends Y>, Y, DomainModel> ();
		this.queryfactories = new CachedMappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> (new BaseMappedFactory<Class<? extends Element>, DataStoreQuery<T>, DataStore> ());

		this.elementmanagers = new HashMap<Class<? extends T>, Class<? extends X>> ();
		this.elementbuilders = new HashMap<Class<? extends T>, Class<? extends Y>> ();
		this.elementfactories = new HashMap<Class<? extends T>, Z> ();
	}

	/**
	 * Set the <code>ElementManager</code> implementation to be used with a given
	 * <code>Element</code> implementation.  This method is intended to be used by
	 * the element implementations when they register with the factory.
	 *
	 * @param  impl                     The <code>Element</code> implementation
	 *                                  which is being registered, not null
	 * @param  manager                  The manager implementation to use with the
	 *                                  element, not null
	 * @throws IllegalArgumentException if a manager is already registered for the
	 *                                  element
	 */

	public final void registerElement (Class<? extends T> impl, Class<? extends X> manager, Class<? extends Y> builder, Z factory)
	{
		if (impl == null)
		{
			this.log.error ("Element is NULL");
			throw new NullPointerException ("Element is NULL");
		}

		if (manager == null)
		{
			this.log.error ("Manager is NULL");
			throw new NullPointerException ("Manager is NULL");
		}

		if (builder == null)
		{
			this.log.error ("Builder is NULL");
			throw new NullPointerException ("Builder is NULL");
		}

		if (this.elementmanagers.containsKey (impl))
		{
			this.log.error ("Attempting to replace previously registered element");
			throw new IllegalArgumentException ("Attempting to replace previously registered element");
		}

		this.elementmanagers.put (impl, manager);
		this.elementbuilders.put (impl, builder);
		this.elementfactories.put (impl, factory);
	}

	public final void registerBuilder (Class<? extends Y> impl, BuilderFactory<Y> factory)
	{
		this.builderfactories.registerClass (impl, factory);
	}

	/**
	 * Register a <code>ElementManager</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementManager</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>ElementManager</code>
	 *                                  implementation which is being registered
	 * @param  factory                  The <code>ManagerFactory</code> used to
	 *                                  create the <code>ElementManager</code>
	 * @throws IllegalArgumentException If the <code>ElementManager</code> is
	 *                                  already registered with the factory
	 */

	public final void registerManager (Class<? extends X> impl, ManagerFactory<X> factory)
	{
		this.managerfactories.registerClass (impl, factory);
	}

	/**
	 * Register a <code>QueryFactory</code> instance with the factory.  This
	 * method is intended to be used by the <code>Element</code> implementations
	 * to provide a properly initialized <code>QueryFactory</code> when they
	 * register with the factory.
	 *
	 * @param  impl                      The <code>Element</code> implementation
	 *                                   which is being registered, not null
	 * @param  factory                   The <code>QueryFactory</code> instance,
	 *                                   not null
	 * @throws IllegalArguementException If a <code>QueryFactory</code> has
	 *                                   already been registered for the specified
	 *                                   <code>Element</code> implementation
	 */

	public final <A extends T> void registerQueryFactory (Class<T> type, Class<A> impl)
	{
		if (type == null)
		{
			this.log.error ("Type is NULL");
			throw new NullPointerException ();
		}

		if (impl == null)
		{
			this.log.error ("Implementation is NULL");
			throw new NullPointerException ();
		}

//		this.queryfactories.registerClass (impl, new DefaultQueryFactory<T, A> (type, impl));
	}

	/**
	 * Get the <code>Set</code> of <code>ElementManager</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementManager</code> implementations
	 */

	public final Set<Class<? extends X>> getRegisteredManagerFactories ()
	{
		return this.managerfactories.getRegisteredClasses ();
	}

	/**
	 * Get the <code>Set</code> of <code>Element</code> implementations
	 * which have registered instances of <code>QueryFactory</code> with the
	 * factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>Element</code> implementations
	 */

	public final Set<Class<? extends Element>> getRegisteredQueryFactories ()
	{
		return this.queryfactories.getRegisteredClasses ();
	}

	/**
	 * Create a <code>ElementManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code> for which the
	 *                               <code>ElementManager</code> is to be created,
	 *                               not null
	 * @return                       The <code>ElementManager</code> for the
	 *                               <code>DomainModel</code>
	 * @throws IllegalStateException if the <code>Element</code> implementation is
	 *                               not registered
	 */

	protected final X createManager (DomainModel model)
	{
		X manager = null;

		if (model == null)
		{
			this.log.error ("DomainModel is null");
			throw new NullPointerException ("DomainModel is null");
		}

		Class<? extends Element> impl = (model.getProfile ()).getImplClass (this.type);

		if (! this.elementmanagers.containsKey (impl))
		{
			throw new IllegalStateException ();
		}

		try
		{
			manager = this.managerfactories.create (this.elementmanagers.get (impl), model);
			((AbstractManager<T>) manager).setFactory (this);
		}
		catch (IllegalArgumentException ex)
		{
			throw new IllegalStateException ("", ex);
		}

		return manager;
	}

	/**
	 * Create a <code>DataStoreQuery</code> for the specified
	 * <code>DomainModel</code>.  The method is intended to be used by the
	 * <code>AbstractManager</code> to retrieve the <code>DataStoreQuery</code>
	 * objects which is uses to access the <code>DataStore</code>.
	 *
	 * @param  model The <code>DomainModel</code> for which the query is to be
	 *               retrieved, not null
	 * @return       The <code>DataStoreQuery</code> object for the
	 *               <code>DomainModel</code> and the type of this factory
	 */

	protected final DataStoreQuery<T> createQuery (DomainModel model)
	{
		if (model == null)
		{
			this.log.error ("Model is NULL");
			throw new NullPointerException ("Model is NULL");
		}

		return this.queryfactories.create ((model.getProfile ()).getImplClass (this.type), model.getDataStore ());
	}
}
