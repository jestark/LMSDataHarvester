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

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Default implementation of the <code>ActivityManager</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultActivityManager extends AbstractManager<Activity> implements ActivityManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>DefaultActivityManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActivityManager>
	{
		/**
		 * Create an instance of the <code>DefaultActivityManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivityManager</code> will operate,
		 *                   not null
		 *
		 * @return           The <code>DefaultActivityManager</code>
		 */

		@Override
		public ActivityManager create (final DataStore datastore)
		{
			assert datastore != null : "datastore is NULL";

			return new DefaultActivityManager (datastore);
		}
	}

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (ActivityManager.class, DefaultActivityManager.class, new Factory ());
	}

	/**
	 * Create the <code>ActivityManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which
	 *                   the <code>ActivityManager</code> will operate, not
	 *                   null
	 */

	public DefaultActivityManager (final DataStore datastore)
	{
		super (Activity.class, datastore);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  builder The <code>ActivityBuilder</code> interface of the
	 *                 builder to be returned, not null
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code> to be created by the
	 *                 <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

	@Override
	public <T extends ActivityBuilder> T getBuilder (final Class<T> builder, final ActivityType type)
	{
		this.log.trace ("getBuilder: builder={}, type={}", builder, type);

		if (builder == null)
		{
			this.log.error ("builder interface class is NULL");
			throw new NullPointerException ();
		}

		if (type == null)
		{
			this.log.error ("ActivityType is NULL");
			throw new NullPointerException ();
		}

		return this.getBuilder (builder, AbstractActivity.getActivityClass (type), type);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable
	 * for use with the <code>DataStore</code>.
	 *
	 * @param  type The <code>ActivityType</code> of the <code>Activity</code>
	 *              to be created by the <code>ActivityBuilder</code>
	 *
	 * @return      An <code>ActivityBuilder</code> instance
	 */

	@Override
	public ActivityBuilder getBuilder (final ActivityType type)
	{
		this.log.trace ("getBuilder: type={}", type);

		if (type == null)
		{
			this.log.error ("ActivityType is NULL");
			throw new NullPointerException ();
		}

		return this.getBuilder (ActivityBuilder.class, type);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified parent <code>Activity</code>.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  builder  The <code>SubActivityBuilder</code> interface of
	 *                  the builder to be returned, not null
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	@Override
	public <T extends SubActivityBuilder> T getBuilder (final Class<T> builder, final Activity activity)
	{
		this.log.trace ("getBuilder: builder={}, activity={}", builder, activity);

		if (builder == null)
		{
			this.log.error ("builder interface class is NULL");
			throw new NullPointerException ();
		}

		if (activity == null)
		{
			this.log.error ("parent Activity is NULL");
			throw new NullPointerException ();
		}

		return this.getBuilder (builder, AbstractActivity.getSubActivityClass (activity.getClass ()), activity);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return          A <code>SubActivityBuilder</code> instance
	 */

	@Override
	public SubActivityBuilder getBuilder (final Activity activity)
	{
		this.log.trace ("getBuilder: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("parent Activity is NULL");
			throw new NullPointerException ();
		}

		return this.getBuilder (SubActivityBuilder.class, activity);
	}

	/**
	 * Retrieve an <code>Activity</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> to retrieve, not null
	 *
	 * @return          A reference to the <code>Activity</code> in the
	 *                  <code>DataStore</code>, may be null
	 */

	@Override
	public Activity fetch (final Activity activity)
	{
		this.log.trace ("fetch: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("The specified Activity is NULL");
			throw new NullPointerException ();
		}

		Activity result = activity;

		if (! (this.fetchQuery (activity.getClass ())).contains (activity))
		{
			result = null; // this.fetchByName (activity.getName ());
		}

		return result;
	}

	/**
	 * Get a <code>List</code> of all of the <code>Activity</code> instances
	 * which are associated with a particular <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

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
