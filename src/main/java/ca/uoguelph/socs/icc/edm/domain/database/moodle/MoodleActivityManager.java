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

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;

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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>MoodleActivityManager</code>
		 * @return       The <code>MoodleActivityManager</code>
		 */

		@Override
		public ActivityManager create (DomainModel model)
		{
			return new MoodleActivityManager (model);
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
		(ActivityFactory.getInstance ()).registerManager (MoodleActivityManager.class, new MoodleActivityManagerFactory ());
	}

	/**
	 * Create the <code>MoodleActvityManager</code>.
	 *
	 * @param  model The <code>DomainModel</code> instance for this manager, not
	 *               null
	 */

	public MoodleActivityManager (DomainModel model)
	{
		super (Activity.class, model);

		this.log = LoggerFactory.getLogger (MoodleActivityManager.class);
		this.manager = new DefaultActivityManager (model);
	}

	/**
	 * Retrieve an object from the data store based on its primary key.
	 *
	 * @param  id The value of the primary key of the object to retrieve, not null
	 * @return    The requested object.
	 */

	@Override
	public Activity fetchById (Long id)
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
	public List<Activity> fetchAllForType (ActivityType type)
	{
		this.log.trace ("Fetch all Activities (with activity data) for ActivityType: {}", type);

		List<Activity> activities = this.manager.fetchAllForType (type);

		return activities;
	}

	/**
	 * Insert an entity into the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to insert into the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   inserted, <code>false</code> otherwise, not null
	 * @return           A reference to the inserted entity
	 */

	@Override
	public Activity insert (Activity entity, Boolean recursive)
	{
		return this.manager.insert (entity, recursive);
	}

	/**
	 * Remove an entity from the domain model and the underlying data store.
	 *
	 * @param  entity    The entity to remove from the domain model, not null
	 * @param  recursive <code>true</code> if dependent entities should also be
	 *                   removed, <code>false</code> otherwise, not null
	 */

	@Override
	public void remove (Activity entity, Boolean recursive)
	{
		this.manager.remove (entity, recursive);
	}

	/**
	 * Modify the value of the stealth flag on a given activity.
	 *
	 * @param  activity The <code>Activity</code> to modify, not null
	 * @param  stealth  The new value of the stealth flag, not null
	 */

	@Override
	public void setStealth (Activity activity, Boolean stealth)
	{
		this.manager.setStealth (activity, stealth);
	}
}
