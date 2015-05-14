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

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceLoader;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActivitySourceLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivitySourceLoader extends AbstractLoader<ActivitySource> implements ActivitySourceLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultActivitySourceLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<ActivitySourceLoader>
	{
		/**
		 * Create an instance of the <code>DefaultActivitySourceLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivitySourceLoader</code> will
		 *                   operate, not null
		 * @return           The <code>DefaultActivitySourceLoader</code>
		 */

		@Override
		public ActivitySourceLoader create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActivitySourceLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (ActivitySourceLoader.class, DefaultActivitySourceLoader.class, new Factory ());
	}

	/**
	 * Create the <code>ActivitySourceLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivitySourceLoader</code> will operate,
	 *                   not null
	 */

	public DefaultActivitySourceLoader (final DataStore datastore)
	{
		super (ActivitySource.class, datastore);
	}

	/**
	 * Retrieve the <code>ActivitySource</code> object associated with the
	 * specified name from the <code>DataStore</code>.
	 *
	 * @param  name The name of the <code>ActivitySource</code> to retrieve,
	 *              not null
	 *
	 * @return      The <code>ActivitySource</code> instance associated with
	 *              the specified name
	 */

	public ActivitySource fetchByName (final String name)
	{
		this.log.trace ("fetchByName: name={}", name);

		if (name == null)
		{
			this.log.error ("The specified ActivitySource name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("name", name);

		return (this.fetchQuery ()).query ("name", params);
	}
}
