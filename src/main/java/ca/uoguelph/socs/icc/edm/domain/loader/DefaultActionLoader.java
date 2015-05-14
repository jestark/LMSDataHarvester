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

import java.util.Map;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActionLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActionLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActionLoader extends AbstractLoader<Action> implements ActionLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultActionLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<ActionLoader>
	{
		/**
		 * Create an instance of the <code>DefaultActionLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActionLoader</code> will operate,
		 *                   not null
		 *
		 * @return           The <code>DefaultActionLoader</code>
		 */

		@Override
		public ActionLoader create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActionLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (ActionLoader.class, DefaultActionLoader.class, new Factory ());
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
