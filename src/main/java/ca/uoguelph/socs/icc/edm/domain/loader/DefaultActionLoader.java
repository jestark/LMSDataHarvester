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

package ca.uoguelph.socs.icc.edm.domain.loader;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.Query;

/**
 * Default implementation of the <code>ActionLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActionLoader extends AbstractLoader<Action> implements ActionLoader
{
	/**
	 * Static initializer to register the <code>ActionLoader</code> with the
	 * <code>LoaderFactory</code>.
	 */

	static
	{
		AbstractLoader.registerLoader (Action.class, DefaultActionLoader::new);
	}

	/**
	 * Create the <code>ActionLoader</code>
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActionLoader</code> will operate, not null
	 */

	public DefaultActionLoader (final DataStore datastore)
	{
		super (Action.class, datastore);
	}

	/**
	 * Retrieve the <code>Action</code> with the specified name from the
	 * <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>Action</code> to retrieve, not null
	 *
	 * @return      The <code>Action</code> associated with the specified name
	 */

	@Override
	public Action fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified Action name is NULL");
			throw new NullPointerException ();
		}

		Query<Action> query = this.fetchQuery (Action.Selectors.NAME);
		query.setProperty (Action.Properties.NAME, name);

		return query.query ();
	}
}
