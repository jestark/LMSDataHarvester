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

/**
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class ActivityManager extends Manager<Activity>
{
	/**
	 * Domain Model Type constant.  Used by the domain model to determine which
	 * implementation classes to use, and for instantiation of this manager.
	 */

	public static final DomainModelType TYPE = DomainModelType.ACTIVITY;

	/**
	 * Get an instance of the ActivityManager for the specified domain model.
	 *
	 * @param model The instance of the Domain model for which the ActivityManager
	 * is to be retrieved.
	 * @return The ActivityManager instance for the specified domain model.
	 * 
	 * @throws IllegalArguementException If the domain model is null.
	 */

	public static ActivityManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException ();
		}

		return (ActivityManager) model.getManager (ActivityManager.TYPE);
	}

	/**
	 * Create the Activity manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this Activity manager.
	 */

	protected ActivityManager (DomainModel model)
	{
		super (model, ActivityManager.TYPE);
	}
	
	/**
	 * Get a list of all of the activities which are associated with a particular
	 * ActivityType.
	 *
	 * @param type The ActivityType.
	 */

	public List<Activity> fetchAllForType (ActivityType type)
	{
		return null;
	}

	/**
	 * Modify the value of the stealth flag on a given activity.
	 *
	 * @param activity The activity to modify.
	 * @param stealth The new value of the stealth flag.
	 */

	public void setStealth (Activity activity, Boolean stealth)
	{
	}
}
