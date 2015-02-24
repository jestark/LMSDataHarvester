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

package ca.uoguelph.socs.icc.edm.domain.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractBuilder<T extends Element> implements ElementBuilder<T>
{
	/** The builder factory */
	private static final ElementBuilderFactory FACTORY;
	
	/** The manager (used to add new instances to the model) */
	private final AbstractManager<T> manager;

	/** The Logger */
	private final Logger log;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORY = new ElementBuilderFactory ();
	}

	/**
	 *
	 * @param  <T>     The <code>ElementBuilder</code> type to be returned
	 * @param  <U>     The <code>Element</code> type produced by the builder
	 * @param  type    The <code>ElementBuilder</code> interface class, not null
	 * @param  manager The <code>ElementManager</code> instance, not null
	 */

	public static <T extends ElementBuilder<U>, U extends Element> T getInstance (final Class<T> type, final AbstractManager<U> manager)
	{
		return null; //FACTORY.create (type, manager);
	}

	/**
	 *
	 * @param  <T>
	 * @param  <U>
	 * @param  builder
	 * @param  impl
	 * @param  factory
	 */

	protected static <T extends ElementBuilder<? extends Element>> void registerBuilder (final Class<T> builder, final Class<? extends T> impl, final BuilderFactory<T> factory)
	{
		assert builder != null : "builder is NULL";
		assert factory != null : "factory is NULL";
		assert impl != null : "impl is NULL";

		FACTORY.registerFactory (builder, impl, factory);
	}

	protected AbstractBuilder (AbstractManager<T> manager)
	{
		this.log = LoggerFactory.getLogger (AbstractBuilder.class);
		
		this.manager = manager;
	}

	public final T create ()
	{
		return this.build ();
	}

	protected abstract T build ();

	public abstract void clear ();
}
