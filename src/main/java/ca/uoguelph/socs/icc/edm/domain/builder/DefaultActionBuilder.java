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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>ActionBuilder</code> interface.
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

	private static class Factory implements BuilderFactory<ActionBuilder, Action>
	{
		/**
		 * Create the <code>ActionBuilder</code> for the specified
		 * <code>DataStore</code>.
		 *
		 * @param  datastore The <code>DataStore</code> into which new
		 *                   <code>Action</code> will be inserted
		 *
		 * @return           The <code>ActionBuilder</code>
		 */

		@Override
		public ActionBuilder create (final DataStore datastore)
		{
			return new DefaultActionBuilder (datastore);
		}
	}

	/** The name of the Action */
	private String name;

	/**
	 * static initializer to register the <code>DefaultActionBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (ActionBuilder.class, DefaultActionBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultActionBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Action</code> instance will be inserted
	 */

	protected DefaultActionBuilder (final DataStore datastore)
	{
		super (Action.class, datastore);
	}

	@Override
	protected Action buildElement ()
	{
		if (this.name == null)
		{
			this.log.error ("Can not build: The action's name is not set");
			throw new IllegalStateException ("name not set");
		}

		Action result = this.element;

		if ((this.element == null) || (! this.name.equals (this.element.getName ())))
		{
			// result = this.factory.create (this.name);
		}

		return result;
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.name = null;
	}

	/**
	 * Load a <code>Action</code> instance into the <code>ActionBuilder</code>.
	 * This method resets the <code>ActionBuilder</code> and initializes all of
	 * its parameters from the specified <code>Action</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  action                   The <code>Action</code> to load into
	 *                                  the <code>ActionBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Action</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Action action)
	{
		this.log.trace ("load: action={}", action);

		super.load (action);
		this.setName (action.getName ());
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Action</code>.
	 *
	 * @param  name                     The name of the <code>Action</code>,
	 *                                  not null
	 *
	 * @return                          This <code>ActionBuilder</code>
	 * @throws IllegalArgumentException If the name is an empty
	 */

	@Override
	public ActionBuilder setName (final String name)
	{
		if (name == null)
		{
			this.log.error ("name is NULL");
			throw new NullPointerException ("name is NULL");
		}

		if (name.length () == 0)
		{
			this.log.error ("name is an empty string");
			throw new IllegalArgumentException ("name is empty");
		}

		this.name = name;

		return this;
	}
}
