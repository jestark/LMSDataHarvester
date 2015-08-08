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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * Builder for the datastore <code>Profile</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 */

public final class ProfileBuilder
{
	/** The logger */
	private final Logger log;

	/** Map of <code>Element</code> implementation classes */
	private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

	/** Map of <code>IdGenerator</code> classes to <code>Element</code> classes */
	private final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators;

	/** Can the <code>DataStore</code> be modified */
	private boolean mutable;

	/**
	 * Create the <code>DomainModelBuilder</code>.
	 */

	public ProfileBuilder ()
	{
		this.log = LoggerFactory.getLogger (ProfileBuilder.class);

		this.mutable = false;
		this.implementations = new HashMap<> ();
		this.generators = new HashMap<> ();
	}

	/**
	 * Reset the builder to a blank state.
	 *
	 * @return This <code>ProfileBuilder</code>
	 */

	public ProfileBuilder clear ()
	{
		this.log.trace ("clear:");

		this.mutable = false;
		this.implementations.clear ();
		this.generators.clear ();

		return this;
	}

	/**
	 * Determine if the <code>DomainModel</code> and its underlying
	 * <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> is mutable,
	 *         <code>false</code> otherwise
	 */

	public final boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Set the mutability of the <code>DomainModel</code>.
	 *
	 * @param  mutable The mutability of the <code>DomainModel</code>
	 *
	 * @return         This <code>ProfileBuilder</code>
	 */

	public ProfileBuilder setMutable (final boolean mutable)
	{
		this.log.trace ("setMutable: mutable={}", mutable);

		this.mutable = mutable;

		return this;
	}

	/**
	 * Determine if there is an implementation class registered for the
	 * specified <code>Element</code> interface class.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      <code>true</code> if the <code>Element</code> has a
	 *              registered implementation class, <code>false</code>
	 *              otherwise
	 */

	public boolean hasElementClass (final Class<? extends Element> type)
	{
		assert type != null : "type is NULL";

		return this.implementations.containsKey (type);
	}

	/**
	 * Get the default implementation class for the specified
	 * <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The default <code>Element</code> implementation class,
	 *                 may be null
	 */

	public Class<? extends Element> getElementClass (Class<? extends Element> element)
	{
		this.log.trace ("getElementClass: element={}", element);

		if (element == null)
		{
			throw new NullPointerException ();
		}

		return this.implementations.get (element);
	}

	/**
	 * Set the default implementation class for the specified
	 * <code>Element</code> interface.
	 *
	 * @param  <T>                      The <code>Element</code> interface type
	 * @param  <U>                      The <code>Element</code> implementation
	 *                                  type
	 * @param  type                     The <code>Element</code> interface
	 *                                  class, not null
	 * @param  impl                     The <code>Element</code> implementation
	 *                                  class, not null
	 *
	 * @return                          This <code>ProfileBuilder</code>
	 * @throws IllegalArgumentException if an implementation for the
	 *                                  <code>Element</code> class is already
	 *                                  registered
	 */

	public <T extends Element, U extends T> ProfileBuilder setElementClass (final Class<T> type, final Class<U> impl)
	{
		this.log.trace ("setElementClass: type={}, impl={}", type, impl);

		if (type == null || impl == null)
		{
			throw new NullPointerException ();
		}

		if (this.implementations.containsKey (type))
		{
			this.log.error ("Implementation for {} already registered", type);
			throw new IllegalArgumentException ("Element implementation is already registered");
		}

		this.implementations.put (type, impl);

		return this;
	}

	/**
	 * Get the <code>IdGenerator</code> associated with the specified
	 * <code>Element</code> class, for the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         The associated <code>IdGenerator</code>
	 */

	public Class<? extends IdGenerator> getGenerator (final Class<? extends Element> element)
	{
		this.log.trace ("getGenerator: element={}", element);

		if (element == null)
		{
			throw new NullPointerException ();
		}

		return this.generators.get (element);
	}

	/**
	 * Register an <code>IdGenerator</code> class to be used to provide ID
	 * numbers for the specified <code>Element</code> class and its
	 * descendants.
	 *
	 * @param  element                  The <code>Element</code>, not null
	 * @param  generator                The <code>IdGenerator</code>, not null
	 *
	 * @return                          This <code>ProfileBuilder</code>
	 * @throws IllegalArgumentException if a <code>IdGenerator</code> is
	 *                                  already registered for the
	 *                                  <code>Element</code>
	 */

	public ProfileBuilder setGenerator (final Class<? extends Element> element, final Class<? extends IdGenerator> generator)
	{
		this.log.trace ("setGenerator: element={}, generator={}", element, generator);

		if (element == null || generator == null)
		{
			throw new NullPointerException ();
		}

		if (this.generators.containsKey (element))
		{
			this.log.error ("Generator already registered for: {}", element.getSimpleName ());
			throw new IllegalArgumentException ("Generator already registered for element");
		}

		this.generators.put (element, generator);

		return this;
	}

	/**
	 * Create the <code>Profile</code>.
	 *
	 * @return                       The <code>Profile</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is mutable
	 *                               and there is not <code>IdGenerator</code>
	 *                               set for <code>Element</code>
	 */

	public Profile build ()
	{
		this.log.trace ("build:");

		if (this.mutable && ! this.generators.containsKey (Element.class))
		{
			this.log.error ("Missing IDGenerator for Element");
			throw new IllegalStateException ("Missing IDGenerator for Element");
		}

		return new Profile (this.mutable, this.implementations, this.generators);
	}
}
