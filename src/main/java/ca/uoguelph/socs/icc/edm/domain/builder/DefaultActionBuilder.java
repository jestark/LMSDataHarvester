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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;

public final class DefaultActionBuilder extends AbstractBuilder<Action> implements ActionBuilder
{
	private static class Factory implements BuilderFactory<ActionBuilder>
	{
		public ActionBuilder create (DomainModel model)
		{
			return new DefaultActionBuilder ((AbstractManager<Action>) model.getActionManager ());
		}
	}

	/** The logger */
	private final Log log;

	/** <code>ElementFactory</code> to build the action */
	private final ActionElementFactory factory;

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

	protected DefaultActionBuilder (AbstractManager<Action> manager)
	{
		super (manager);

		this.factory = null;
		this.log = LogFactory.getLog (DefaultActionBuilder.class);
	}

	@Override
	protected Action build ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The action's name is not set");
			throw new IllegalStateException ("name not set");		
		}

		return this.factory.create (this.name);
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
	public ActionBuilder setName (String name)
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
