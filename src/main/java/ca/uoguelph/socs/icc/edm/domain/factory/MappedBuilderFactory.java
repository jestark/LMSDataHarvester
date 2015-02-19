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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

/**
 * Factory for creating <code>ElementBuilder</code> objects.   
 * 
 * @author  James E.Stark
 * @version 1.0
 * @param   <T> The domain model interface represented by this factory
 * @param   <X> The builder interface created by this factory
 * @param   <Y> The element factory interface used by the builder created by
 *              this factory
 * @see     MappedFactory
 * @see     ca.uoguelph.socs.icc.edm.builder.BuilderFactory
 * @see     ca.uoguelph.socs.icc.edm.builder.ElementFactory
 */

public final class MappedBuilderFactory<T extends Element, X extends ElementBuilder<T>>
{
	/** Singleton instance */
	private static final Map<Class<? extends ElementBuilder<? extends Element>>, MappedBuilderFactory<? extends Element, ? extends ElementBuilder<? extends Element>>> INSTANCE;

	/** The logger */
	private final Logger log;

	/** The type of the domain model interface associated with this factory */
	private final Class<T> type;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final Map<Class<? extends ElementBuilder<? extends Element>>, BuilderFactory<X>> builders;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private final Map<Class<? extends T>, Class<? extends X>> elements;

	/**
	 *  static initializer to create the singleton
	 */

	static
	{
		INSTANCE = new HashMap<Class<? extends ElementBuilder<? extends Element>>, MappedBuilderFactory<? extends Element, ? extends ElementBuilder<? extends Element>>> ();
	}

	/**
	 * Get an instance of the <code>MappedBuilderFactory</code>.
	 *
	 * @param  type    The <code>Element</code> interface, not null
	 * @param  builder The <code>ElementBuilder</code> interface, not null
	 *
	 * @return         The <code>MappedBuilderFactory</code> instance
	 */

	@SuppressWarnings("unchecked")
	public static <T extends Element, X extends ElementBuilder<T>> MappedBuilderFactory<T, X> getInstance (Class<T> type, Class<X> builder)
	{
		if (type == null)
		{
			throw new NullPointerException ();
		}

		if (builder == null)
		{
			throw new NullPointerException ();
		}

		if (! INSTANCE.containsKey (builder))
		{
			INSTANCE.put (builder, new MappedBuilderFactory<T, X> (type));
		}

		return (MappedBuilderFactory<T, X>) INSTANCE.get (builder);
	}

	/**
	 * Create the <code>MappedBuilderFactory</code>.
	 *
	 * @param  type The domain model interface class for which the builders are
	 *              being created, not null
	 */

	public MappedBuilderFactory (Class<T> type)
	{
		this.log = LoggerFactory.getLogger (MappedBuilderFactory.class);

		if (type == null)
		{
			this.log.error ("Type is NULL");
			throw new NullPointerException ("Type is NULL");
		}

		this.type = type;
		this.builders = new HashMap<Class<? extends ElementBuilder<? extends Element>>, BuilderFactory<X>> ();
		this.elements = new HashMap<Class<? extends T>, Class<? extends X>> ();
	}

	/**
	 * Register a <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementBuilder</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>ElementBuilder</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>BuilderFactory</code> used to
	 *                                  create the <code>ElementBuilder</code>,
	 *                                  not null
	 * @throws IllegalArgumentException If the <code>ElementBuilder</code> is
	 *                                  already registered with the factory
	 */

	public void registerClass (Class<? extends ElementBuilder<? extends Element>> impl, BuilderFactory<X> factory)
	{
		this.log.trace ("Registering Builder: {} ({})", impl, factory);

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

		if (this.builders.containsKey (impl))
		{
			this.log.error ("Class has already been registered: {}", impl);
			throw new IllegalArgumentException ("Duplicate class registration");
		}

		this.builders.put (impl, factory);
	}

	/**
	 * Register a <code>Element</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>Element</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>Element</code> implementation
	 *                                  which is being registered, not null
	 * @param  builder                  The builder class to be used to build the
	 *                                  <code>Element</code>, not null
	 * @throws IllegalArgumentException If the <code>Element</code> is already
	 *                                  registered with the factory
	 */

	public void registerElement (Class<? extends T> impl, Class<? extends X> builder)
	{
		this.log.trace ("Registering Element: {} ({})", impl, builder);

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

		if (this.elements.containsKey (impl))
		{
			this.log.error ("Class already registered: {}", impl);
			throw new IllegalArgumentException ("Class already registered");
		}

		this.elements.put (impl, builder);
	}

	/**
	 * Get the <code>Set</code> of <code>ElementBuilder</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementBuilder</code> implementations
	 */

	public Set<Class<? extends ElementBuilder<? extends Element>>> getRegisteredClasses ()
	{
		return new HashSet<Class<? extends ElementBuilder<? extends Element>>> (this.builders.keySet ());
	}

	/**
	 * Get the <code>Set</code> of <code>ElementBuilder</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementBuilder</code> implementations
	 */

	public Set<Class<? extends Element>> getRegisteredElements ()
	{
		return new HashSet<Class<? extends Element>> (this.elements.keySet ());
	}

	/**
	 * Determine if the specified <code>ElementBuilder</code> implementation class
	 * has been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>ElementManager</code> implementation
	 *         has been registered, <code>false</code> otherwise
	 */

	public boolean isRegistered (Class<? extends X> impl)
	{
		return this.builders.containsKey (impl);
	}

	/**
	 * Determine if the specified <code>Element</code> implementation class has
	 * been registered with the factory.
	 *
	 * @return  <code>true</code> if the <code>Element</code> implementation
	 *         has been registered, <code>false</code> otherwise
	 */

	public boolean isElementRegistered (Class<? extends Element> impl)
	{
		return this.elements.containsKey (impl);
	}

	/**
	 * Create a <code>ElementBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code> for which the
	 *                               <code>ElementBuilder</code> is to be created,
	 *                               not null
	 * @return                       The <code>ElementBuilder</code>
	 * @throws IllegalStateException if the either of the element or builder 
	 *                               implementation classes is not registered
	 */

	public X create (DomainModel model)
	{
		this.log.debug ("Creating builder for interface {} on model {}", this.type, model);

		if (model == null)
		{
			this.log.error ("DomainModel is null");
			throw new NullPointerException ();
		}

		if (! this.elements.containsKey ((model.getProfile ()).getImplClass (this.type)))
		{
			this.log.error ("Attempting to create a builder for an unregistered Element: {}", (model.getProfile ()).getImplClass (this.type));
			throw new IllegalStateException ("Element is not registered");
		}

		if (! this.builders.containsKey (this.elements.get ((model.getProfile ()).getImplClass (this.type))))
		{
			this.log.error ("Attempting to create an unregistered builder: {}", this.elements.get ((model.getProfile ()).getImplClass (this.type)));
			throw new IllegalStateException ("Builder is now registered");
		}

		return (this.builders.get (this.elements.get ((model.getProfile ()).getImplClass (this.type)))).create (model);
	}
}
