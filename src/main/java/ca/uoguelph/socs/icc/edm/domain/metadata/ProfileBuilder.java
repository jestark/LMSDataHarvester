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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

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

	/** Can the <code>DataStore</code> be modified */
	private boolean mutable;

	/** The name of the <code>Profile</code> */
	private String name;

	/** Map of <code>Element</code> implementation classes */
	private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

	/** Map of <code>IdGenerator</code> classes to <code>Element</code> classes */
	private final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators;

	/**
	 * Create the <code>DomainModelBuilder</code>.
	 */

	public ProfileBuilder ()
	{
		this.log = LoggerFactory.getLogger (ProfileBuilder.class);

		this.mutable = false;
		this.name = "";
		this.generators = new HashMap<> ();
		this.implementations = new HashMap<> ();
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
	 * Get the name of the <code>Profile</code>.  This is also the name of any
	 * associated <code>DataStore</code> instances.
	 *
	 * @return The name of the <code>Profile</code>.
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Profile</code>.
	 *
	 * @param  name                     The name of the <code>Profile</code>,
	 *                                  not null and must not be empty
	 *
	 * @return                          This <code>ProfileBuilder</code>
	 * @throws IllegalArgumentException if the name is empty
	 */

	public ProfileBuilder setName (final String name)
	{
		this.log.trace ("setName: name={}", name);

		if (name == null)
		{
			throw new NullPointerException ();
		}

		if (name.length () < 1)
		{
			this.log.error ("The supplied name is Empty");
			throw new IllegalArgumentException ("name is Empty");
		}

		this.name = name;

		return this;
	}

	/**
	 * Get the <code>Set</code> of <code>Element</code> interfaces for which a
	 * default implementation is set.
	 *
	 * @return A <code>Set</code> of <code>Element</code> classes
	 */

	public Set<Class<? extends Element>> getElements ()
	{
		return new HashSet<> (this.implementations.keySet ());
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
		if (type != null)
		{
			throw new NullPointerException ();
		}

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
	 * Remove the entry for the specified <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 *
	 * @return         This <code>ProfileBuilder</code>
	 */

	public ProfileBuilder removeElementClass (Class<? extends Element> element)
	{
		this.log.trace ("removeElementClass: element={}", element);

		if (element == null)
		{
			throw new NullPointerException ();
		}

		this.implementations.remove (element);

		return this;
	}

	/**
	 * Get the <code>Set</code> of <code>Element</code> classes for which an
	 * <code>IdGenerator</code> has been assigned.
	 *
	 * @return A <code>Set</code> of <code>Element</code> classes
	 */

	public Set<Class<? extends Element>> getGenerators ()
	{
		return new HashSet<> (this.generators.keySet ());
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
	 * Remove the <code>IdGenerator</code> for the specified
	 * <code>Element</code> class.  The <code>IdGenerator</code> implementation
	 * assigned the <code>Element</code> is the default <code>IdGnerator</code>
	 * for the <code>DataStore</code> and can not be removed.
	 *
	 * @param  element                  The <code>Element</code> class, not
	 *                                  null
	 *
	 * @return                          The <code>ProfileBuilder</code>
	 * @throws IllegalArgumentException if the <code>IdGenerator</code> for
	 *                                  <code>Element</code> is to be removed
	 */

	public ProfileBuilder removeGenerator (final Class<? extends Element> element)
	{
		this.log.trace ("removeGenerator: element={}", element);

		if (element == null)
		{
			throw new NullPointerException ();
		}

		if (element == Element.class)
		{
			this.log.error ("Can not remove the IdGenerator for Element");
			throw new IllegalArgumentException ("Cann not remove the IdGenerator for Element");
		}

		return this;
	}

	/**
	 * Create the <code>Profile</code>.
	 *
	 * @return                       The <code>Profile</code>
	 * @throws IllegalStateException if there is not <code>IdGenerator</code>
	 *                               set for <code>Element</code>
	 * @throws IllegalStateException if the name is empty
	 */

	public Profile build ()
	{
		this.log.trace ("build:");

		if (this.name.length () < 0)
		{
			this.log.error ("name is Empty");
			throw new IllegalStateException ("name is Empty");
		}

		if (! this.generators.containsKey (Element.class))
		{
			this.log.error ("Missing IDGenerator for Element");
			throw new IllegalStateException ("Missing IDGenerator for Element");
		}

		return new Profile (this.name, this.mutable, this.implementations, this.generators);
	}
}
