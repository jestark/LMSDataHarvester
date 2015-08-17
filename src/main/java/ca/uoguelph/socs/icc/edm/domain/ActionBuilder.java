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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

/**
 * Create new <code>Action</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Action</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Action
 */

public final class ActionBuilder extends AbstractBuilder<Action>
{
	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>ActionBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Action</code>
	 */

	public static ActionBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, Action.class, ActionBuilder::new);
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActionBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Action</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static ActionBuilder getInstance (final DomainModel model)
	{
		return ActionBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Create the <code>ActionBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 */

	protected ActionBuilder (final DataStore datastore, final Creator<Action> metadata)
	{
		super (datastore, metadata);
	}

	/**
	 * Load a <code>Action</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Action</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  action                   The <code>Action</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Action</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Action action)
	{
		this.log.trace ("load: action={}", action);

		if (action == null)
		{
			this.log.error ("Attempting to load a NULL Action");
			throw new NullPointerException ();
		}

		super.load (action);
		this.setName (action.getName ());

		this.builder.setProperty (Action.ID, action.getId ());
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Action.NAME);
	}

	/**
	 * Set the name of the <code>Action</code>.
	 *
	 * @param  name                     The name of the <code>Action</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the name is empty
	 */

	public void setName (final String name)
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

		this.builder.setProperty (Action.NAME, name);
	}
}
