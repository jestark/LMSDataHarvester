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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Activity
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
		 * @param  model The <code>DomainModel</code> to be associated with the
		 *               <code>DefaultActivityManager</code>
		 * @return       The <code>DefaultActivityManager</code>
		 */

		@Override
		public ActivityManager create (DomainModel model)
		{
			return new DefaultActivityManager (model);
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
		AbstractManager.registerManager (ActivityManager.class, DefaultActivityManager.class, new Factory ());
	}

	/**
	 * Create the Activity manager.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivityManager</code> is to be created, not null
	 */

	public DefaultActivityManager (DomainModel model)
	{
		super (Activity.class, model);

		this.log = LoggerFactory.getLogger (ActivityManager.class);
	}

	/**
	 * Get a list of all of the activities which are associated with a particular
	 * <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	public List<Activity> fetchAllForType (ActivityType type)
	{
		this.log.trace ("Fetching all Activities with ActivityType: {}", type);

		if (type == null)
		{
			this.log.error ("The specified ActivityType is NULL");
			throw new NullPointerException ();
		}

		Map<String, Object> params = new HashMap<String, Object> ();
		params.put ("type", type);

		return (this.fetchQuery ()).queryAll ("type", params);
	}

	/**
	 * Modify the value of the stealth flag on a given activity.
	 *
	 * @param  activity The <code>Activity</code> to modify, not null
	 * @param  stealth  The new value of the stealth flag, not null
	 */

	public void setStealth (Activity activity, Boolean stealth)
	{
	}
}
