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

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class TypedFactoryMap<T>
{
	private static final class FactoryKey
	{
		private Class<?> type;
		private Class<?> impl;

		public FactoryKey (final Class<?> type, final Class<?> impl)
		{
			this.type = type;
			this.impl = impl;
		}

		@Override
		public boolean equals (final Object obj)
		{
			boolean result = false;

			if (obj == this)
			{
				result = true;
			}
			else if (obj instanceof FactoryKey)
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.append (this.type, ((FactoryKey) obj).type);
				ebuilder.append (this.impl, ((FactoryKey) obj).impl);

				result = ebuilder.isEquals ();
			}

			return result;
		}

		@Override
		public int hashCode ()
		{
			HashCodeBuilder hbuilder = new HashCodeBuilder ();
			hbuilder.append (this.type);
			hbuilder.append (this.impl);

			return hbuilder.hashCode ();
		}

		public Class<?> getType ()
		{
			return this.type;
		}

		public Class<?> getImpl ()
		{
			return this.impl;
		}
	}

	/** The logger */
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<FactoryKey, T> factories;

	public TypedFactoryMap ()
	{
		this.log = LoggerFactory.getLogger (TypedFactoryMap.class);

		this.factories = new HashMap<FactoryKey, T> ();
	}

	/**
	 *
	 * @param  type
	 * @param  impl
	 * @param  factory
	 */

	public void registerFactory (final Class<?> type, final Class<?> impl, final T factory)
	{
		this.log.trace ("Registering Class: {}, with type {} and factory {}", type, impl, factory);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		FactoryKey key = new FactoryKey (type, impl);

		if (this.factories.containsKey (key))
		{
			this.log.error ("Class already Registered: {}", impl.getSimpleName ());
			throw new IllegalArgumentException ("Class already Registered");
		}

		this.factories.put (key, factory);
	}

	/**
	 * Get the <code>Set</code> of <code>Class</code> instances which are
	 * registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered <code>Class</code>
	 *         instances
	 */

	public Set<Class<?>> getRegisteredFactories ()
	{
		Set<Class<?>> result = new HashSet<Class<?>> ();

		for (FactoryKey key : this.factories.keySet ())
		{
			result.add (key.getImpl ());
		}

		return result;
	}

	/**
	 *
	 * @param  type
	 * @param  impl
	 *
	 * @return 
	 */

	public T getFactory (final Class<?> type, final Class<?> impl)
	{
		this.log.trace ("Retrieve the factory for class: {} (type {})", impl, type);
		
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		FactoryKey key = new FactoryKey (type, impl);

		if (! this.factories.containsKey (key))
		{
			this.log.error ("Class is not registered: {}", impl);
			throw new IllegalStateException ("Class not registered");
		}

		return this.factories.get (key);
	}
}
