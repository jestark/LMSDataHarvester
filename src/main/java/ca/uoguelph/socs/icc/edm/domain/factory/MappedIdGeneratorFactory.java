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

package ca.uoguelph.socs.icc.edm.domain.factory;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGeneratorFactory;

/**
 * Factory for creating <code>IdGenerator</code> objects.  This class is
 * implemented using a <code>MappedFactory<code>, requiring all of the
 * <code>IdGenerator</code> implementations to register a factory object
 * capable of instantiating and initializing the <code>IdGenerator</code>
 * with the factory
 * 
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGeneratorFactory
 */

public final class MappedIdGeneratorFactory
{
	/** The logger */
	private final Logger log;

	/** Factory for the <code>IdGenerator</code> implementations */
	private final Map<Class<? extends IdGenerator>, IdGeneratorFactory> generators;

	/**
	 * Create the <code>MappedIdGeneratorFactory</code>.
	 */

	public MappedIdGeneratorFactory ()
	{
		this.log = LoggerFactory.getLogger (MappedManagerFactory.class);

		this.generators = new HashMap<Class<? extends IdGenerator>, IdGeneratorFactory> ();
	}

	/**
	 * Register a <code>IdGenerator</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>IdGenerator</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>IdGenerator</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>IdGeneratorFactory</code> used to
	 *                                  create the <code>Idgenerator</code>,
	 *                                  not null
	 * @throws IllegalArgumentException If the <code>IdGenerator</code> is
	 *                                  already registered with the factory
	 */

	public void registerClass (Class<? extends IdGenerator> impl, IdGeneratorFactory factory)
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
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code> for which the
	 *                               manager is to be created, not null
	 * @return                       The <code>IdGenerator</code>
	 * @throws IllegalStateException if the <code>IdGenerator</code> implementation
	 *                               class is not registered
	 * @see    MappedFactory#create
	 */

	public IdGenerator create (DataStore datastore)
	{
		if (datastore == null)
		{
			this.log.error ("Domain model is NULL");
			throw new NullPointerException ();
		}

		return null;
	}
}
