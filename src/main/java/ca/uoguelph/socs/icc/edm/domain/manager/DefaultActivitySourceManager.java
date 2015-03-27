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

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivitySourceManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActivitySourceManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivitySourceManager extends AbstractManager<ActivitySource> implements ActivitySourceManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActivitySourceManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActivitySourceManager>
	{
		/**
		 * Create an instance of the <code>DefaultActivitySourceManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivitySourceManager</code> will
		 *                   operate, not null
		 * @return           The <code>DefaultActivitySourceManager</code>
		 */

		@Override
		public ActivitySourceManager create (DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActivitySourceManager (datastore);
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
		AbstractManager.registerManager (ActivitySourceManager.class, DefaultActivitySourceManager.class, new Factory ());
	}

	/**
	 * Create the <code>ActivitySourceManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivitySourceManager</code> will operate,
	 *                   not null
	 */

	public DefaultActivitySourceManager (final DataStore datastore)
	{
		super (ActivitySource.class, datastore);

		this.log = LoggerFactory.getLogger (ActivitySourceManager.class);
	}

	/**
	 * Get an instance of the <code>ActivitySourceBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivitySourceBuilder</code> instance
	 */

	@Override
	public ActivitySourceBuilder getBuilder ()
	{
		return this.getBuilder (ActivitySourceBuilder.class);
	}

	/**
	 * Retrieve an <code>ActivitySource</code> from the <code>DataStore</code>
	 * which identifies the same as the specified <code>ActivitySource</code>.
	 *
	 * @param  source The <code>ActivitySource</code> to retrieve, not null
	 *
	 * @return        A reference to the <code>ActivitySource</code> in the
	 *                <code>DataStore</code>, may be null
	 */

	@Override
	public ActivitySource fetch (final ActivitySource source)
	{
		this.log.trace ("fetch: source={}", source);

		if (source == null)
		{
			this.log.error ("The specified ActivitySource is NULL");
			throw new NullPointerException ();
		}

		ActivitySource result = source;

		if (! (this.fetchQuery ()).contains (source))
		{
			result = this.fetchByName (source.getName ());
		}

		return result;
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
