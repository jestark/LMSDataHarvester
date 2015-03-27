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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.SubActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;
import ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

import ca.uoguelph.socs.icc.edm.domain.manager.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager;
import ca.uoguelph.socs.icc.edm.domain.manager.ManagerFactory;

/**
 * <code>ActivityManager</code> implementation for Moodle. This class decorates
 * the <code>DefaultActivityManager</code> and overrides the fetch methods to
 * add an additional step to load any associated pieces of activity data.  This
 * class works around limitations imposed by the by the Moodle database due to
 * it's lack of proper normalization.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager
 */

public final class MoodleActivityManager extends AbstractManager<Activity> implements ActivityManager
{
	/**
	 * Implementation of the <code>ManagerFactory</code> to create a
	 * <code>MoodleActivityManager</code>.
	 */

	private static final class Factory implements ManagerFactory<ActivityManager>
	{
		/**
		 * Create an instance of the <code>MoodleActivityManager</code>.
		 *
		 * @param  datastore The <code>DataStore</code> upon which the
		 *                   <code>DefaultActivityManager</code> will operate, not null
		 *
		 * @return       The <code>MoodleActivityManager</code>
		 */

		@Override
		public ActivityManager create (final DataStore datastore)
		{
			return new MoodleActivityManager (datastore);
		}
	}

	/** <code>ActivitySource</code> instance for all moodle activities*/
	private static final ActivitySource SOURCE;

	/** Reference to the <code>DefaultActivityManager</code> */
	private final ActivityManager manager;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		SOURCE = new ActivitySourceData ("moodle");
		AbstractManager.registerManager (ActivityManager.class, MoodleActivityManager.class, new Factory ());
	}

	/**
	 * Create the <code>MoodleActvityManager</code>.
	 *
	 * @param  datastore The instance of the <code>DataStore</code> upon which the
	 *                   <code>ActivityManager</code> will operate, not null
	 */

	public MoodleActivityManager (final DataStore datastore)
	{
		super (Activity.class, datastore);

		this.manager = new DefaultActivityManager (datastore);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @param  <T>     The type of <code>ActivityBuilder</code>
	 * @param  builder The <code>ActivityBuilder</code> interface of the builder
	 *                 to be returned, not null
	 * @param  type    The <code>ActivityType</code> of the <code>Activity</code>
	 *                 to be created by the <code>ActivityBuilder</code>
	 *
	 * @return         An <code>ActivityBuilder</code> instance
	 */

	@Override
	public <T extends ActivityBuilder> T getBuilder (final Class<T> builder, final ActivityType type)
	{
		return this.manager.getBuilder (builder, type);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @param  type The <code>ActivityType</code> of the <code>Activity</code> to
	 *              be created by the <code>ActivityBuilder</code>
	 *
	 * @return      An <code>ActivityBuilder</code> instance
	 */

	@Override
	public ActivityBuilder getBuilder (final ActivityType type)
	{
		return this.manager.getBuilder (type);
	}

	/**
	 * Get an instance of the <code>SubActivityBuilder</code> suitable for use
	 * with the specified <code>Activity</code>.
	 *
	 * @param  <T>      The type of <code>SubActivityBuilder</code> to be
	 *                  returned
	 * @param  builder  The <code>SubActivityBuilder</code> interface of
	 *                  the builder to be returned, not null
	 * @param  activity The <code>Activity</code> instance to which the new
	 *                  <code>SubActivity</code> instance is to be assigned
	 *
	 * @return
	 */

	@Override
	public <T extends SubActivityBuilder> T getBuilder (final Class<T> builder, final Activity activity)
	{
		return this.manager.getBuilder (builder, activity);
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
		return this.manager.getBuilder (activity);
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

		return this.manager.fetch (activity);
	}

	/**
	 * Retrieve an <code>Activity</code> from the <code>DataStore</code> based
	 * on its <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Activity</code> to retrieve, not null
	 *
	 * @return    The requested <code>Activity</code>
	 */

	@Override
	public Activity fetchById (final Long id)
	{
		this.log.trace ("fetchById: id={}", id);

		return this.manager.fetchById (id);
	}

	/**
	 * Retrieve a <code>List</code> of all of the <code>Activity</code>
	 * instances in the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

	@Override
	public List<Activity> fetchAll ()
	{
		this.log.trace ("fetchAll:");

		return this.manager.fetchAll ();
	}

	/**
	 * Get a <code>List</code> of all of the <code>Activity</code> instances
	 * which are associated with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 *
	 * @return      A <code>List</code> of <code>Activity</code> instances
	 */

	@Override
	public List<Activity> fetchAllForType (final ActivityType type)
	{
		this.log.trace ("fetchallForType: type={}", type);

		return this.manager.fetchAllForType (type);
	}
}
