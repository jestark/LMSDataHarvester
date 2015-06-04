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

public final class DefaultActionBuilder extends AbstractBuilder<Action, Action.Properties> implements ActionBuilder
{
	/**
	 * static initializer to register the <code>DefaultActionBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultActionBuilder.class, DefaultActionBuilder::new);
	}

	/**
	 * Create the <code>DefaultActionBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Action</code> instance will be inserted
	 */

	protected DefaultActionBuilder (final DataStore datastore)
	{
		super (Action.Properties.class, datastore);
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
		return this.getPropertyValue (Action.Properties.NAME);
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
		this.log.trace ("setName: name={}", name);

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

		this.setPropertyValue (Action.Properties.NAME, name);

		return this;
	}
}
