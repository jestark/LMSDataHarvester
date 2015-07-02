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
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The <code>ActionBuilder</code> instance
	 */

	public static ActionBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new ActionBuilder (datastore, AbstractBuilder.getBuilder (datastore, datastore.getElementClass (Action.class)));
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Action</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  action    The <code>Action</code>, not null
	 *
	 * @return           The <code>ActionBuilder</code> instance
	 */

	public static ActionBuilder getInstance (final DataStore datastore, Action action)
	{
		assert datastore != null : "datastore is NULL";
		assert action != null : "action is NULL";

		ActionBuilder builder = ActionBuilder.getInstance (datastore);
		builder.load (action);

		return builder;
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The <code>ActionBuilder</code> instance
	 */


	public static ActionBuilder getInstance (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return ActionBuilder.getInstance (model.getDataStore ());
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Action</code>.
	 *
	 * @param  model  The <code>DomainModel</code>, not null
	 * @param  action The <code>Action</code>, not null
	 *
	 * @return        The <code>ActionBuilder</code> instance
	 */

	public static ActionBuilder getInstance (final DomainModel model, Action action)
	{
		if (action == null)
		{
			throw new NullPointerException ("action is NULL");
		}

		ActionBuilder builder = ActionBuilder.getInstance (model);
		builder.load (action);

		return builder;
	}

	/**
	 * Create the <code>ActionBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  builder   The <code>Builder</code>, not null
	 */

	protected ActionBuilder (final DataStore datastore, final Builder<Action> builder)
	{
		super (datastore, builder);
	}

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

		this.builder.setProperty (Action.Properties.ID, action.getId ());
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public String getName ()
	{
		return this.builder.getPropertyValue (Action.Properties.NAME);
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

		this.builder.setProperty (Action.Properties.NAME, name);
	}
}
