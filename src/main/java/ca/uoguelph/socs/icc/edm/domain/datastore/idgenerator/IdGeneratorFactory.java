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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Factory for creating <code>IdGenerator</code> instances.  This class 
 * requires all of the <code>IdGenerator</code> implementations to register
 * a factory object (extending the <code>IdGeneratorImplFactory</code>
 * interface) capable of instantiating and initializing the
 * <code>IdGenerator</code>. 
 * 
 * @author  James E. Stark
 * @version 1.0
 * @see     IdGeneratorImplFactory
 */

public final class IdGeneratorFactory
{
	/** Singleton instance */
	private static final IdGeneratorFactory instance;

	/** The logger */
	private final Logger log;

	/** Factory for the <code>IdGenerator</code> implementations */
	private final Map<Class<? extends IdGenerator>, IdGeneratorImplFactory> generators;

	/**
	 * static initializer to create the <code>IdGeneratorFactory</code> singleton.
	 */

	static
	{
		instance = new IdGeneratorFactory ();
	}

	/**
	 * Get the instance of the <code>IdGeneratorFactory</code>.
	 *
	 * @param The <code>IdGneratorFactory</code> instance
	 */

	public static IdGeneratorFactory getInstance ()
	{
		return IdGeneratorFactory.instance ();
	}

	/**
	 * Create the <code>IdGeneratorFactory</code>.
	 */

	private IdGeneratorFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedManagerFactory.class);

		this.generators = new HashMap<Class<? extends IdGenerator>, IdGeneratorImplFactory> ();
	}

	/**
	 * Register a <code>IdGenerator</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>IdGenerator</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>IdGenerator</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>IdGeneratorImplFactory</code>
	 *                                  used to create the
	 *                                  <code>IdGenerator</code>, not null
	 * @throws IllegalArgumentException If the <code>IdGenerator</code> is
	 *                                  already registered with the factory
	 */

	public void registerClass (Class<? extends IdGenerator> impl, IdGeneratorImplFactory factory)
	{
		this.log.trace ("Registering ID Generator: {} ({})", impl, factory);

		if (impl == null)
		{
			this.log.error ("Implementation class is NULL");
			throw new NullPointerException ();
		}

		if (factory == null)
		{
			this.log.error ("Factory is NULL");
			throw new NullPointerException ();
		}

		if (this.generators.containsKey (impl))
		{
			this.log.error ("Class has already been registered: {}", impl);
			throw new IllegalArgumentException ("Duplicate class registration");
		}

		this.generators.put (impl, factory);
	}

	/**
	 * Get the <code>Set</code> of <code>IdGenerator</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>IdGenerator</code> implementations
	 */

	public Set<Class<? extends IdGenerator>> getRegisteredClasses ()
	{
		return this.generators.keySet ();
	}

	/**
	 * Determine if the specified <code>IdGenerator</code> implementation class
	 * has been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>IdGenerator</code> implementation
	 *         has been registered, <code>false</code> otherwise
	 */

	public boolean isRegistered (Class<? extends IdGenerator> impl)
	{
		return this.generators.containsKey (impl);
	}

	/**
	 * Create a <code>IdGenerator</code> for the specified
	 * <code>DataStoreQuery</code>.
	 *
	 * @param  query                 The <code>DataStoreQuery</code> for which the
	 *                               <code>IdGenerator</code> is to be created,
	 *                               not null
	 * @return                       The <code>IdGenerator</code> instance
	 * @throws IllegalStateException if the <code>IdGenerator</code> implementation
	 *                               class is not registered
	 * @see    MappedFactory#create
	 */

	public <T extends Element> IdGenerator create (Class<T> type, DataStoreQuery<T> query)
	{
		if (query == null)
		{
			this.log.error ("Query is NULL");
			throw new NullPointerException ();
		}

		Class<? extends IdGenerator> generator = ((query.getDataStore ()).getProfile ()).getGenerator (type);

		if (! this.generators.containsKey (generator))
		{
			this.log.error ();
			throw new IllegalStateException ();
		}

		return (this.generator.get (generator)).create (query);
	}
}
