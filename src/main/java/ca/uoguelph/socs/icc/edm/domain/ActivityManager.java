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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Activity
 */

public final class ActivityManager extends DomainModelManager<Activity>
{
	/** The logger */
	private final Log log;

	/**
	 * Get an instance of the <code>ActivityManager</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The instance of the <code>DomainModel</code> for which the
	 *               <code>ActivityManager</code>is to be retrieved, not null
	 * @return       The <code>ActivityManager</code> instance for the specified
	 *               domain model
	 * @see    DomainModel#getManager

	 */

	public static ActivityManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ();
		}

		return model.getManager (ActivityManager.class);
	}

	/**
	 * Create the Activity manager.
	 *
	 * @param  model The instance of the <code>DomainModel</code> upon which the
	 *               <code>ActivityManager</code> is to be created, not null
	 * @param  query The <code>DataStoreQuery</code> to be used to access the
	 *               data-store, not null
	 */

	protected ActivityManager (DomainModel model, DataStoreQuery<Activity> query)
	{
		super (model, query);

		this.log = LogFactory.getLog (UserManager.class);
	}

	/**
	 * Get an instance of the builder.
	 *
	 * @return An instance of the <code>ActivityBuilder</code>
	 */

	public ActivityBuilder getBuilder ()
	{
		return (ActivityBuilder) this.builder;
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
