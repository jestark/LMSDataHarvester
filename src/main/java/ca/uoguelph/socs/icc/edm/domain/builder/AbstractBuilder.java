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

package ca.uoguelph.socs.icc.edm.domain.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ElementManager
 */

public abstract class AbstractBuilder<T extends Element> implements ElementBuilder<T>
{
	/** The builder factory */
	private static final ElementBuilderFactory FACTORY;

	/** The <code>ElementFactory</code> implementations */
	private static final MappedElementFactory ELEMENTS;
	
	/** The manager (used to add new instances to the model) */
	private final ManagerProxy<T> manager;

	/** The Logger */
	private final Logger log;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORY = new ElementBuilderFactory ();
		ELEMENTS = new MappedElementFactory ();
	}

	/**
	 * Register an <code>ElementBuilder</code> implementation with the factory.
	 * This method is intended to be used by the <code>ElementBuilder</code>
	 * implementations to register themselves with the factory so that they may
	 * be instantiated on demand.
	 *
	 * @param  <T>         The <code>ElementBuilder</code> type to be registered
	 * @param  <U>         The <code>Element</code> type produced by the builder
	 * @param  builder     The <code>ElementBuilder</code> interface class, not
	 *                     null
	 * @param  builderImpl The <code>ElementBuilder</code> implementation class,
	 *                     not null
	 * @param  factory     The factory to create instances of the implementation
	 *                     class, not null
	 */

	protected static final <T extends ElementBuilder<U>, U extends Element> void registerBuilder (final Class<T> builder, final Class<? extends T> builderImpl, final BuilderFactory<U, T> factory)
	{
		assert builder != null : "builder is NULL";
		assert builderImpl != null : "impl is NULL";
		assert factory != null : "factory is NULL";

		FACTORY.registerFactory (builder, builderImpl, factory);
	}

	/**
	 * Get an instance of the <code>ElementFactory</code> which corresponds to the
	 * specified <code>Element</code> implementation class.  This method is
	 * intended to be used by the <code>ElementBuilder</code> implementations to
	 * get the relevant <code>ElementFactory</code>, which is required to create
	 * and operate on the <code>Element</code> implementations.
	 *
	 * @param  <T>     The type of <code>ElementFactory</code> to be returned
	 * @param  <U>     The <code>Element</code> type returned by the
	 *                 <code>ElementFactory</code>
	 * @param  factory The <code>ElementFactory</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 *
	 * @return The <code>ElementFactory</code>
	 */

	protected static final <T extends ElementFactory<U>, U extends Element> T getFactory (final Class<T> factory, final Class<? extends U> element)
	{
		assert factory != null : "factory is NULL";
		assert element != null : "element is NULL";

		return ELEMENTS.getFactory (factory, element);
	}

	/**
	 * Get an instance of the <code>ElementBuilder</code> which corresponds to the
	 * specified <code>Element</code> implementation class.  This method is
	 * intended to be used by the <code>ElementManager</code> implementations to
	 * get the relevant builder for the <code>ElementManager</code> instance and
	 * the <code>Element</code> upon which it is operating.
	 *
	 * @param  <T>     The <code>ElementBuilder</code> type to be returned
	 * @param  <U>     The <code>Element</code> type produced by the builder
	 * @param  builder The <code>ElementBuilder</code> interface class, not null
	 * @param  element The <code>Element</code> implementation class, not null
	 * @param  manager The <code>ElementManager</code> instance, not null
	 */

	public static final <T extends ElementBuilder<U>, U extends Element> T getInstance (final Class<T> builder, final Class<? extends Element> element, final ManagerProxy<U> manager)
	{
		assert builder != null : "builder is NULL";
		assert element != null : "element is NULL";
		assert manager != null : "manager is NULL";

		return FACTORY.create (builder, element, manager);
	}

	/**
	 * Register the association between an <code>Element</code> implementation and
	 * the appropriate <code>ElementBuilder</code> implementation.  This method is
	 * intended to be used by the <code>Element</code> implementations to specify
	 * the appropriate <code>ElementBuilder</code> implementation to use for
	 * creating instances of that <code>Element</code>.
	 *
	 * @param  <T>     The interface type of the <code>Element</code>
	 * @param  <U>     The implementation type of the <code>Element</code>
	 * @param  element The implementation class, not null
	 * @param  builder The <code>ElementBuilder</code> implementation class, not
	 *                 null
	 */

	public static final <T extends ElementBuilder<U>, U extends Element> void registerElement (final Class<? extends U> element, final Class<T> builder)
	{
		assert element != null : "element is NULL";
		assert builder != null : "builder is NULL";
		
		FACTORY.registerElement (element, builder);
	}

	/**
	 * Register an <code>ElementFactory</code> for an <code>Element</code>
	 * implementation so that it can be used by the <code>ElementBuilder</code>
	 * instances.  This method is intended to be used by the <code>Element</code>
	 * implementations to register their <code>ElementFactory</code>
	 * implementation with the factory contained in the
	 * <code>AbstractBuilder</code>.
	 *
	 * @param  <T>         The implementation type of the <code>Element</code>
	 * @param  <U>         The interface type of the <code>Element</code>
	 * @param  factory     The <code>ElementFactory</code> interface class, not
	 *                     null
	 * @param  element     The <code>Element</code> implementation class, not null
	 * @param  factoryImpl The <code>ElementFactory</code> to register, not null
	 */

	public static final <T extends ElementFactory<U>, U extends Element> void registerFactory (final Class<? extends U> element, final Class<T> factory, final T factoryImpl)
	{
		assert element != null : "element is NULL";
		assert factory != null : "factory is NULL";
		assert factoryImpl != null : "factoryImpl is NULL";

		ELEMENTS.registerFactory (factory, element, factoryImpl);
	}

	/**
	 * Create the <code>AbstractBuilder</code>.
	 *
	 * @param  manager The <code>ElementManager</code> which the
	 *                 <code>ElementBuilder</code> will use to operate on the
	 *                 <code>DataStore</code>.
	 */

	protected AbstractBuilder (final ManagerProxy<T> manager)
	{
		this.log = LoggerFactory.getLogger (AbstractBuilder.class);
		
		this.manager = manager;
	}

	public T build ()
	{
		return null;
	}

	public void load (T element)
	{
	}
}
