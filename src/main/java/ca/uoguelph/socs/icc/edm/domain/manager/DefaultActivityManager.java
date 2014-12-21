/* Copyright (C) 2014 James E. Stark
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.AbstractManager;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityManager;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;

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

	private static final class DefaultActivityManagerFactory implements ManagerFactory<ActivityManager>
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
	private final Log log;

	/**
	 * Static initializer to register the manager with its
	 * <code>AbstractManagerFactory</code> implementation.
	 */

	static
	{
		(ActivityFactory.getInstance ()).registerManagerFactory (DefaultActivityManager.class, new DefaultActivityManagerFactory ());
	}

	/**
	 * Create the Activity manager.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivityManager</code> is to be created, not null
	 */

	protected DefaultActivityManager (DomainModel model)
	{
		super (model);

		this.log = LogFactory.getLog (ActivityManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>ActivityBuilder</code>
	 */

	public ActivityBuilder getBuilder ()
	{
		return (ActivityBuilder) this.fetchBuilder ();
	}

	/**
	 * Get a list of all of the activities which are associated with a particular
	 * <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	public List<Activity> fetchAllForType (ActivityType type)
	{
		return null;
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
