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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Load <code>Action</code> instances from the <code>DataStore</code>.  This
 * class extends <code>AbstractLoader</code>, adding the functionality
 * required to handle <code>Action</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActionLoader extends AbstractLoader<Action>
{
	/**
	 * Get an instance of the <code>ActionLoader</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>ActionLoader</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Element</code> queried by the
	 *                               loader
	 */

	public static ActionLoader getInstance (final DomainModel model)
	{
		return AbstractLoader.getInstance (model, Action.class, ActionLoader::new);
	}

	/**
	 * Create the <code>ActionLoader</code>
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActionLoader</code> will operate, not null
	 */

	public ActionLoader (final DataStore datastore)
	{
		super (Action.class, datastore);
	}

	/**
	 * Retrieve an <code>Element</code> instance from the
	 * <code>DataStore</code> based on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> to retrieve, not null
	 *
	 * @return    The requested <code>Element</code>
	 */

	public Action fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		if (id == null)
		{
			this.log.error ("Attempting to fetch an element with a NULL id");
			throw new NullPointerException ();
		}

		return this.getQuery (Action.SELECTOR_ID)
			.setValue (Action.ID, id)
			.query ();
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances from the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public List<Action> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.getQuery (Action.SELECTOR_ALL)
			.queryAll ();
	}

	/**
	 * Retrieve the <code>Action</code> with the specified name from the
	 * <code>DataStore</code>.
	 *
	 * @param  name                  The name of the <code>Action</code>, not
	 *                               null
	 *
	 * @return                       The <code>Action</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 */

	public Action fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Action name is NULL");
			throw new NullPointerException ();
		}

		return this.getQuery (Action.SELECTOR_NAME)
			.setValue (Action.NAME, name)
			.query ();
	}
}
