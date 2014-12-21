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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.factory.GenericFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.GenericBaseFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.GenericCachedFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.QueryFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The domain model interface represented by this factory
 * @param   <X> The Manager interface created by this factory
 */

public abstract class AbstractManagerFactory<T extends Element, X extends ElementManager<T>>
{
	/** The logger */
	private final Log log;

	/** */
	private final DomainModelType type;

	/** Caching factory for the <code>ElementManager</code> implementations */
	private final GenericFactory<X, DomainModel> managerfactories;

	/** <code>Element<code> to <code>ElementManager</code> implementation mapping */
	private final Map<Class<? extends T>, Class<? extends X>> elementmanagers;

	/**
	 * Create the <code>AbstractManagerFactory</code>.
	 *
	 * @param  type 
	 */

	protected AbstractManagerFactory (DomainModelType type)
	{
		this.log = LogFactory.getLog (AbstractManagerFactory.class);

		this.type = type;

		this.managerfactories = new GenericCachedFactory<X, DomainModel> (new GenericBaseFactory<X, DomainModel> ());

		this.elementmanagers = new HashMap<Class<? extends T>, Class<? extends X>> ();
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

	public final void registerManagerFactory (Class<? extends X> impl, ManagerFactory<X> factory)
	{
		this.managerfactories.registerClass (impl, factory);
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

	public final void setElementManager (Class<? extends T> impl, Class<? extends X> manager)
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

		if (this.elementmanagers.containsKey (impl))
		{
			this.log.error ("Attempting to replace previously registered element");
			throw new IllegalArgumentException ("Attempting to replace previously registered element");
		}

		this.elementmanagers.put (impl, manager);
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

	public final X createManager (DomainModel model)
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
		}
		catch (IllegalArgumentException ex)
		{
			throw new IllegalStateException ("", ex);
		}

		return manager;
	}
}
