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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

/**
 * Factory for creating <code>ElementBuilder</code> objects.  This factory is
 * implemented using a <code>MappedFactory</code>.
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

public final class MappedBuilderFactory<T extends Element, X extends ElementBuilder<T>, Y extends ElementFactory<T>>
{
	/**
	 * Internal carrier class containing the registration data provided by the
	 * associated element.
	 *
	 * @param  <T> The associated <code>Element</code> interface
	 * @param  <X> The associated <code>ElementBuilder</code> interface
	 * @param  <Y> The associated <code>ElementFactory</code> interface
	 */

	private final class ElementData <T extends Element, X extends ElementBuilder<T>, Y extends ElementFactory<T>>
	{
		/** The builder implementation to be used with the <code>Element</code> */
		private final Class<? extends X> builder;

		/** The <code>ElementFactory</code> to be used by the builder instance */
		private final Y factory;

		/**
		 * Crate the <code>ElementData</code> carrier.
		 *
		 * @param  builder The builder implementation to use with the associated
		 *                 <code>Element</code>, not null
		 * @param  factory The <code>ElementFactory</code> to be used by the
		 *                 <code>ElementBuilder</code> instance
		 */

		public ElementData (Class<? extends X> builder, Y factory)
		{
			this.builder = builder;
			this.factory = factory;
		}

		/**
		 * Get the builder class.
		 *
		 * @return The builder class
		 */

		public Class<? extends X> getBuilder ()
		{
			return this.builder;
		}

		/**
		 * Get the <code>ElementFactory</code>.
		 *
		 * @return The <code>ElementFactory</code>
		 */

		public Y getFactory ()
		{
			return factory;
		}
	}

	/** The logger */
	private final Logger log;

	/** The type of the domain model interface associated with this factory */
	private final Class<T> type;

	/** Factory for the <code>ElementBuilder</code> implementations */
	private final MappedFactory<Class<? extends ElementBuilder<? extends Element>>, X, DomainModel> builders;

	/** <code>Element<code> to <code>ElementBuilder</code> implementation mapping */
	private final Map<Class<? extends T>, ElementData<T, X, Y>> elements;

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
		this.builders = new BaseMappedFactory<Class<? extends ElementBuilder<? extends Element>>, X, DomainModel> ();
		this.elements = new HashMap<Class<? extends T>, ElementData<T, X, Y>> ();
	}

	/**
	 * Register a <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the implementation of
	 * <code>ElementBuilder</code> when the classes initialize.
	 *
	 * @param  impl                     The <code>ElementBuilderr</code>
	 *                                  implementation which is being registered,
	 *                                  not null
	 * @param  factory                  The <code>BuilderFactory</code> used to
	 *                                  create the <code>ElementBuilder</code>,
	 *                                  not null
	 * @throws IllegalArgumentException If the <code>ElementBuilder</code> is
	 *                                  already registered with the factory
	 * @see    MappedFactory#registerClass
	 */

	public void registerClass (Class<? extends ElementBuilder<? extends Element>> impl, BuilderFactory<X> factory)
	{
		this.log.trace ("Registering Builder: {} ({})", impl, factory);

		this.builders.registerClass (impl, factory);
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
	 * @param  factory                  The <code>ElementFactory</code> to be used
	 *                                  by the <code>ElementBuilder</code>, not null
	 *                                  instance to create the <code>Element</code>
	 * @throws IllegalArgumentException If the <code>Element</code> is already
	 *                                  registered with the factory
	 */

	public void registerElement (Class<? extends T> impl, Class<? extends X> builder, Y factory)
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

		if (factory == null)
		{
			this.log.error ("Attempting to register an Element with a NULL ElementFactory");
			throw new NullPointerException ();
		}

		this.elements.put (impl, new ElementData<T, X, Y> (builder, factory));
	}

	/**
	 * Get the <code>Set</code> of <code>ElementBuilder</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementBuilder</code> implementations
	 * @see    MappedFactory#getRegisteredClasses
	 */

	public Set<Class<? extends ElementBuilder<? extends Element>>> getRegisteredClasses ()
	{
		return this.builders.getRegisteredClasses ();
	}

	/**
	 * Get the <code>Set</code> of <code>ElementBuilder</code> implementations
	 * which have been registered with the factory.
	 *
	 * @return A <code>Set</code> containing the registered
	 *         <code>ElementBuilder</code> implementations
	 */

	public Set<Class<? extends T>> getRegisteredElements ()
	{
		return this.elements.keySet ();
	}

	/**
	 * Determine if the specified <code>ElementBuilder</code> implementation class
	 * has been registered with the factory.
	 *
	 * @return <code>true</code> if the <code>ElementManager</code> implementation
	 *         has been registered, <code>false</code> otherwise
	 * @see    MappedFactory#isRegistered
	 */

	public boolean isRegistered (Class<? extends X> impl)
	{
		return this.builders.isRegistered (impl);
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
	 * @see    MappedFactory#create
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

		ElementData<T, X, Y> edata = this.elements.get ((model.getProfile ()).getImplClass (this.type));

		if (! this.builders.isRegistered (edata.getBuilder ()))
		{
			this.log.error ("Attempting to create an unregistered builder: {}", edata.getBuilder ());
			throw new IllegalStateException ("Builder is now registered");
		}

		return this.builders.create (edata.getBuilder (), model);
	}
}
