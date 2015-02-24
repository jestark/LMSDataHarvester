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

package ca.uoguelph.socs.icc.edm.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.builder.BuilderFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

import ca.uoguelph.socs.icc.edm.domain.factory.MappedBuilderFactory;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

import ca.uoguelph.socs.icc.edm.domain.idgenerator.IdGenerator;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractBuilder<T extends Element>
{
	/** The builder factory */
	private static final MappedBuilderFactory FACTORY;
	
	/** The manager (used to add new instances to the model) */
	private final AbstractManager<T> manager;

	/** The ID number generator */
	private final IdGenerator generator;

	/** The Logger */
	private final Log log;

	/**
	 * static initializer to create the factory.
	 */

	static
	{
		FACTORY = new MappedBuilderFactory ();
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
		this.log = LogFactory.getLog (AbstractBuilder.class);
		
		this.manager = manager;
		this.generator = null;
	}

	public final T create ()
	{
		return this.build ();
	}

	protected abstract T build ();

	public abstract void clear ();
}
