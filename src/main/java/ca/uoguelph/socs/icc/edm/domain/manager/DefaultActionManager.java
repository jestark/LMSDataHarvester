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
import ca.uoguelph.socs.icc.edm.domain.ActionManager;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Create, insert and remove actions from the domain model.  Through
 * implementations of this interface, Actions can be added to or removed
 * from the domain model.
 *
 * @author James E. Stark
 * @version 1.0
 * @see     Action
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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultActionManager</code>
		 * @return       The <code>DefaultActionManager</code>
		 */

		@Override
		public ActionManager create (DomainModel model)
		{
			return new DefaultActionManager (model);
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
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActionManager</code> is to be created, not null
	 */

	public DefaultActionManager (DomainModel model)
	{
		super (Action.class, model);

		this.log = LoggerFactory.getLogger (ActionManager.class);
	}

	/**
	 * Retrieve the Action with the specified name from the data-store.
	 *
	 * @param  name The name of the <code>Action</code> to retrieve, not null
	 * @return      The <code>Action</code> associated with the specified name.
	 */

	public Action fetchByName (String name)
	{
		this.log.trace ("Fetching Action with name: {}", name);

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
