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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

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

	private static final class MoodleActivityManagerFactory implements ManagerFactory<ActivityManager>
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

	/** The log */
	private final Logger log;

	/** Reference to the <code>DefaultActivityManager</code> */
	private final ActivityManager manager;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		AbstractManager.registerManager (ActivityManager.class, MoodleActivityManager.class, new MoodleActivityManagerFactory ());
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

		this.log = LoggerFactory.getLogger (MoodleActivityManager.class);
		this.manager = new DefaultActivityManager (datastore);
	}

	/**
	 * Get an instance of the <code>ActivityBuilder</code> interface, suitable for
	 * use with the <code>DataStore</code>.
	 *
	 * @return An <code>ActivityBuilder</code> instance
	 */

	@Override
	public ActivityBuilder getBuilder ()
	{
		return this.getBuilder (ActivityBuilder.class);
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
		this.log.trace ("Fetching Activity with the same identity as: {}", activity);

		return this.manager.fetch (activity);
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	@Override
	public Activity fetchById (final Long id)
	{
		this.log.trace ("Fetch Activity (with Activity Data) for ID: {}", id);

		Activity activity = this.manager.fetchById (id);

		return activity;
	}

	/**
	 * Retrieve a list of all of the entities from the underlying data store.
	 *
	 * @return A list of objects.
	 */

	@Override
	public List<Activity> fetchAll ()
	{
		this.log.trace ("Fetch all activities (with activity data)");

		List<Activity> activities = this.manager.fetchAll ();

		return activities;
	}

	/**
	 * Get a list of all of the activities which are associated with a particular
	 * <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	@Override
	public List<Activity> fetchAllForType (final ActivityType type)
	{
		this.log.trace ("Fetch all Activities (with activity data) for ActivityType: {}", type);

		List<Activity> activities = this.manager.fetchAllForType (type);

		return activities;
	}
}
