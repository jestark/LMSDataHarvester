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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityTypeManager;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActivityTypeManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivityTypeManager extends AbstractManager<ActivityType> implements ActivityTypeManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActivityTypeManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActivityTypeManager>
	{
		/**
		 * Create an instance of the <code>DefaultActivityTypeManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivityTypeManager</code> will
		 *                   operate, not null
		 * @return           The <code>DefaultActivityTypeManager</code>
		 */

		@Override
		public ActivityTypeManager create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActivityTypeManager (datastore);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (ActivityTypeManager.class, DefaultActivityTypeManager.class, new Factory ());
	}

	/**
	 * Create the <code>ActivityType</code> manager.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityTypeManager</code> will operate, not
	 *                   null
	 */

	public DefaultActivityTypeManager (final DataStore datastore)
	{
		super (ActivityType.class, datastore);
	}

	/**
	 * Get an instance of the <code>ActivityTypeBuilder</code> interface,
	 * suitable for use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivityTypeBuilder</code> instance
	 */

	@Override
	public ActivityTypeBuilder getBuilder ()
	{
		return this.getBuilder (ActivityTypeBuilder.class);
	}

	/**
	 * Retrieve an <code>ActivityType</code> from the <code>DataStore</code>
	 * which identifies the same as the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code> to retrieve, not null
	 *
	 * @return      A reference to the <code>ActivityType</code> in the
	 *              <code>DataStore</code>, may be null
	 */

	@Override
	public ActivityType fetch (final ActivityType type)
	{
		this.log.trace ("fetch: type={}", type);

		if (type == null)
		{
			this.log.error ("The specified ActivityType is NULL");
			throw new NullPointerException ();
		}

		ActivityType result = type;

		if (! (this.fetchQuery ()).contains (type))
		{
			result = this.fetchByName (type.getSource (), type.getName ());
		}

		return result;
	}

	/**
	 * Retrieve the <code>ActivityType</code> object from the
	 * <code>DataStore</code> which has the specified
	 * <code>ActivitySource</code> and name.
	 *
	 * @param  source The <code>ActivitySource</code> containing the
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 *
	 * @return        The <code>ActivityType</code> instance which is
	 *                associated with the specified source and name
	 */

	public ActivityType fetchByName (final ActivitySource source, final String name)
	{
		this.log.trace ("fetchByName source={}, name={}", source, name);

		if (source == null)
		{
			this.log.error ("The specified ActivitySource is NULL");
			throw new NullPointerException ();
		}

		if (name == null)
		{
			this.log.error ("The specified ActivityType name is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("source", source);
		params.put ("name", name);

		return (this.fetchQuery ()).query ("name", params);
	}
}
