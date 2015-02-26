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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;

import ca.uoguelph.socs.icc.edm.domain.manager.ManagerProxy;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActionBuilder extends AbstractBuilder<Action> implements ActionBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultActionBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<Action, ActionBuilder>
	{
		/**
		 * Create the <code>ActionBuilder</code>.  The supplied
		 * <code>ManagerProxy</code> will be used by the builder to access the
		 * <code>ActionManager</code> to perform operations on the
		 * <code>DataStore</code>.
		 *
		 * @param  manager The <code>ManagerProxy</code> used to the 
		 *                 <code>ActionManager</code> instance, not null
		 *
		 * @return         The <code>ActionBuilder</code>
		 */

		@Override
		public ActionBuilder create (final ManagerProxy<Action> manager)
		{
			return new DefaultActionBuilder (manager);
		}
	}

	/** The logger */
	private final Logger log;

	/** The name of the Action */
	private String name;

	/**
	 * static initializer to register the <code>DefaultActionBuilder</code> with the
	 * factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActionBuilder.class, DefaultActionBuilder.class, new Factory ());
	}

	protected DefaultActionBuilder (final ManagerProxy<Action> manager)
	{
		super (manager);

		this.log = LoggerFactory.getLogger (DefaultActionBuilder.class);
	}

	@Override
	public Action build ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The action's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		return null; //this.factory.create (this.name);
	}

	@Override
	public void clear ()
	{
		this.name = null;
	}

	@Override
	public String getName ()
	{
		return this.name;
	}

	@Override
	public ActionBuilder setName (final String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		this.name = name;

		return this;
	}
}
