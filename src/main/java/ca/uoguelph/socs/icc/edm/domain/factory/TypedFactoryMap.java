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

final class TypedFactoryMap<T extends Class<?>, U extends Class<?>, V>
{
	private static final class FactoryKey<T, U>
	{
		private T type;
		private U impl;

		public FactoryKey (final T type, final U impl)
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

		public T getType ()
		{
			return this.type;
		}

		public U getImpl ()
		{
			return this.impl;
		}
	}

	/** The logger */
	private final Logger log;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<FactoryKey<T, U>, V> factories;

	public TypedFactoryMap ()
	{
		this.log = LoggerFactory.getLogger (TypedFactoryMap.class);

		this.factories = new HashMap<FactoryKey<T, U>, V> ();
	}

	/**
	 *
	 * @param  type
	 * @param  impl
	 * @param  factory
	 */

	public void put (final T type, final U impl, final V factory)
	{
		this.log.trace ("Registering Class: {}, with type {} and factory {}", type, impl, factory);

		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		FactoryKey<T, U> key = new FactoryKey<T, U> (type, impl);

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

	public Set<U> getRegisteredClasses ()
	{
		Set<U> result = new HashSet<U> ();

		for (FactoryKey<T, U> key : this.factories.keySet ())
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

	public V get (final T type, final U impl)
	{
		this.log.trace ("Retrieve the factory for class: {} (type {})", impl, type);
		
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		FactoryKey<T, U> key = new FactoryKey<T, U> (type, impl);

		if (! this.factories.containsKey (key))
		{
			this.log.error ("Class is not registered: {}", impl);
			throw new IllegalStateException ("Class not registered");
		}

		return this.factories.get (key);
	}
}
