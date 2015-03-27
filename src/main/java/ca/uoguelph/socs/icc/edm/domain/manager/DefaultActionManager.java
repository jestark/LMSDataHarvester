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

package ca.uoguelph.socs.icc.edm.domain.manager;

import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActionManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActionManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActionManager extends AbstractManager<Action> implements ActionManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActionManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActionManager>
	{
		/**
		 * Create an instance of the <code>DefaultActionManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActionManager</code> will operate,
		 *                   not null
		 *
		 * @return           The <code>DefaultActionManager</code>
		 */

		@Override
		public ActionManager create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActionManager (datastore);
		}
	}

	/** The logger */
	private final Logger log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (ActionManager.class, DefaultActionManager.class, new Factory ());
	}

	/**
	 * Create the <code>ActionManager</code>
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActionManager</code> will operate, not null
	 */

	public DefaultActionManager (final DataStore datastore)
	{
		super (Action.class, datastore);

		this.log = LoggerFactory.getLogger (ActionManager.class);
	}

	/**
	 * Get an instance of the <code>ActionBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActionBuilder</code> instance
	 */

	@Override
	public ActionBuilder getBuilder ()
	{
		return this.getBuilder (ActionBuilder.class);
	}

	/**
	 * Retrieve an <code>Action</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Action</code>.
	 *
	 * @param  action The <code>Action</code> to retrieve, not null
	 *
	 * @return        A reference to the <code>Action</code> in the
	 *                <code>DataStore</code>, may be null
	 */

	@Override
	public Action fetch (final Action action)
	{
		this.log.trace ("fetch: action={}", action);

		if (action == null)
		{
			this.log.error ("The specified Action is NULL");
			throw new NullPointerException ();
		}

		Action result = action;

		if (! (this.fetchQuery ()).contains (action))
		{
			result = this.fetchByName (action.getName ());
		}

		return result;
	}

	/**
	 * Retrieve the <code>Action</code> with the specified name from the
	 * <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>Action</code> to retrieve, not null
	 *
	 * @return      The <code>Action</code> associated with the specified name
	 */

	public Action fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Action name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put ("name", name);

		return (this.fetchQuery ()).query ("name", parameters);
	}
}
