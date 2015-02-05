/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.factory;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 * Factory for creating <code>ElementManager</code> objects.  This class is
 * implemented using a <code>CachedMappedFactory<code>, requiring all of the
 * <code>ElementManager</code> implementations to register a factory object
 * capable of instantiating and initializing the <code>ElementManager</code>
 * with the factory.  As a caching factory, this factory will ensure that the
 * is no more than one instance of a given <code>ElementManager</code> 
 * implementation for a given <code>DomainModel</code> at any time.  If the
 * requested <code>Elementmanager</code> already exists then the cached copy
 * will returned, otherwise a new instance will be created.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The domain model interface represented by this factory
 * @param   <X> The Manager interface created by this factory
 * @see     CachedMappedFactory
 * @see     ca.uoguelph.socs.icc.edm.manager.ManagerFactory
 */

public final class MappedManagerFactory<T extends Element, X extends ElementManager<T>>
{
	/** The logger */
	private final Logger log;

	/** The type of the domain model interface associated with this factory*/
	private final Class<T> type;

	/** Caching factory for the <code>ElementManager</code> implementations */
	private final CachedMappedFactory<Class<? extends ElementManager<? extends Element>>, X, DomainModel> managers;

	/**
	 * Create the <code>MappedManagerFactory</code>.
	 *
	 * @param  type The domain model interface class for which the managers are
	 *              being created, not null
	 */

	public MappedManagerFactory (Class<T> type)
	{
		this.log = LoggerFactory.getLogger (MappedManagerFactory.class);

		if (type == null)
		{
			this.log.error ("domain model interface type NULL");
			throw new NullPointerException ();
		}

		this.type = type;
		this.managers = new CachedMappedFactory<Class<? extends ElementManager<? extends Element>>, X, DomainModel> (new BaseMappedFactory<Class<? extends ElementManager<? extends Element>>, X, DomainModel> ());
	}

	/**
	 * Register a <code>ElementManager</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementManager</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>ElementManager</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>ManagerFactory</code> used to
	 *                                  create the <code>ElementManager</code>,
	 *                                  not null
	 * @throws IllegalArgumentException If the <code>ElementManager</code> is
	 *                                  already registered with the factory
	 * @see    MappedFactory#registerClass
	 */

	public void registerClass (Class<? extends ElementManager<? extends Element>> impl, ManagerFactory<X> factory)
	{
		this.log.trace ("Registering Class: {} ({})", impl, factory);

		this.managers.registerClass (impl, factory);
	}

	/**
	 * Get the <code>Set</code> of <code>ElementManager</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementManager</code> implementations
	 * @see    MappedFactory#getRegisteredClasses
	 */

	public Set<Class<? extends ElementManager<? extends Element>>> getRegisteredClasses ()
	{
		return this.managers.getRegisteredClasses ();
	}

	/**
	 * Determine if the specified <code>ElementManager</code> implementation class
	 * has been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>ElementManager</code> implementation
	 *         has been registered, <code>false</code> otherwise
	 * @see    MappedFactory#isRegistered
	 */

	public boolean isRegistered (Class<? extends X> impl)
	{
		return this.managers.isRegistered (impl);
	}

	/**
	 * Create a <code>ElementManager</code> for the specified
	 * <code>DomainModel</code>.  If the <code>ElementManager</code> already
	 * exists in the cache, then the cached copy will be returned, otherwise a new
	 * <code>ElementManager</code> will be created.
	 *
	 * @param  model                 The <code>DomainModel</code> for which the
	 *                               manager is to be created, not null
	 * @return                       The <code>ElementManager</code>
	 * @throws IllegalStateException if the manager implementation class is not
	 *                               registered
	 * @see    MappedFactory#create
	 */

	public X create (DomainModel model)
	{
		this.log.debug ("Create manager for interface {}, on model {}", this.type, model);

		if (model == null)
		{
			this.log.error ("Domain model is NULL");
			throw new NullPointerException ();
		}

		Class<? extends ElementManager<? extends Element>> manager = (model.getProfile ()).getManagerClass (this.type);

		if (! this.managers.isRegistered (manager))
		{
			this.log.error ("Attempting to create manager for unregistered class: {}", manager);
			throw new IllegalStateException ("ElementManager implementation is not registered");
		}

		return this.managers.create (manager, model);
	}

	/**
	 * Remove all of the Managers for the specified <code>DomainModel</code> from the
	 * cache.
	 *
	 * @param  model The <code>DomainModel</code> for which all of the cached
	 *               managers are to be purged, not null
	 * @see    CachedMappedFactory#remove
	 */

	public void remove (DomainModel model)
	{
		this.log.trace ("Removing all ElementManager instances for {}", model);

		this.managers.remove (model);
	}

	/**
	 * Remove all managers from the cache.
	 *
	 * @see    CachedMappedFactory#flush
	 */

	public void flush ()
	{
		this.log.trace ("Flushing cache");

		this.managers.flush ();
	}

}