/* Copyright (C) 2014, 2015 James E. Stark
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.factory.MappedBuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.MappedManagerFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.MappedQueryFactory;

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
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final MappedFactory<Class<?>, Y, DomainModel> builderfactories;

	/** Factory for the <code>ElementManager</code> implementations */
	private final MappedManagerFactory<T, X> managers;

	/** Factory for the <code>DataStoreQuery</code> instances */
	private final MappedQueryFactory<T> queries;

	/**
	 * Create the <code>AbstractManagerFactory</code>.
	 *
	 * @param  type The domain model interface class for which the queries are
	 *              being created, not null
	 */

	protected AbstractManagerFactory (Class<T> type)
	{
		this.log = LoggerFactory.getLogger (AbstractManagerFactory.class);

		if (type == null)
		{
			this.log.error ("DomainModel interface type is NULL");
			throw new NullPointerException ();
		}

		this.builderfactories = new BaseMappedFactory<Class<?>, Y, DomainModel> ();

		this.managers = new MappedManagerFactory<T, X> (type);
		this.queries = new MappedQueryFactory<T> (type);
	}

	public final void registerBuilder (Class<? extends Y> impl, BuilderFactory<Y> factory)
	{
		this.log.trace ("Registering Builder: {} ({})", impl, factory);

		this.builderfactories.registerClass (impl, factory);
	}

	public final <A extends T> void registerElement (Class<A> impl, Class<? extends Y> builder, Z factory)
	{
		this.log.trace ("Registering Element: {} ({}, {})", impl, builder, factory);
		if (impl == null)
		{
			this.log.error ("Attempting to register a NULL Element");
			throw new NullPointerException ();
		}

		if (builder == null)
		{
			this.log.error ("Attempting to register an Element with a NULL Builder");
			throw new NullPointerException ();
		}

		this.queries.registerClass (impl);
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
		this.log.trace ("Registering Manager: {} ({})", impl, factory);

		this.managers.registerClass (impl, factory);
	}

	/**
	 * Get the <code>Set</code> of <code>ElementManager</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementManager</code> implementations
	 */

	public final Set<Class<? extends ElementManager<? extends Element>>> getRegisteredManagers ()
	{
		return this.managers.getRegisteredClasses ();
	}

	/**
	 * Get the <code>Set</code> of <code>Element</code> implementations
	 * which have registered instances of <code>QueryFactory</code> with the
	 * factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>Element</code> implementations
	 */

	public final Set<Class<? extends Element>> getRegisteredQueries ()
	{
		return this.queries.getRegisteredClasses ();
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
		this.log.trace ("Create manager for: {}", model);

		X manager = this.managers.create (model);
		((AbstractManager<T>) manager).setFactory (this);

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
		this.log.trace ("Create query on model {}", model);

		if (model == null)
		{
			this.log.error ("Attempting to create a Query on a NULL DomainModel");
			throw new NullPointerException ();
		}

		return this.queries.create (model.getDataStore ());
	}
}
