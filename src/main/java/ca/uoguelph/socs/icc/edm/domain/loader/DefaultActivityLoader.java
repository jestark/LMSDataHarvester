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

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityLoader;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActivityLoader</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivityLoader extends AbstractLoader<Activity> implements ActivityLoader
{
	/**
	 * Implementation of the <code>LoaderFactory</code> to create a
	 * <code>DefaultActivityLoader</code>.
	 */

	private static final class Factory implements LoaderFactory<ActivityLoader>
	{
		/**
		 * Create an instance of the <code>DefaultActivityLoader</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivityLoader</code> will operate,
		 *                   not null
		 *
		 * @return           The <code>DefaultActivityLoader</code>
		 */

		@Override
		public ActivityLoader create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActivityLoader (datastore);
		}
	}

	/**
	 * Static initializer to register the Loader with its
	 * <code>AbstractLoaderFactory</code> implementation.
	 */

	static
	{
		AbstractLoader.registerLoader (ActivityLoader.class, DefaultActivityLoader.class, new Factory ());
	}

	/**
	 * Create the <code>ActivityLoader</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityLoader</code> will operate, not
	 *                   null
	 */

	public DefaultActivityLoader (final DataStore datastore)
	{
		super (Activity.class, datastore);
	}

	@Override
	public List<Activity> fetchAllForType (final ActivityType type)
	{
		this.log.trace ("fetchAllForType: type={}", type);

		if (type == null)
		{
			this.log.error ("The specified ActivityType is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("type", type);

		return (this.fetchQuery ()).queryAll ("type", params);
	}
}
