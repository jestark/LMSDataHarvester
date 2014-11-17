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

/**
 *
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class ActivityTypeManager extends Manager<ActivityType>
{
	/**
	 * Domain Model Type constant.  Used by the domain model to determine which
	 * implementation classes to use, and for instantiation of this manager.
	 */

	public static final DomainModelType TYPE = DomainModelType.ACTIVITYTYPE;

	/**
	 * Get an instance of the ActivityTypeManager for the specified domain model.
	 *
	 * @param model The instance of the Domain model for which the ActivityTypeManager
	 * is to be retrieved.
	 * @return The ActivityTypeManager instance for the specified domain model.
	 * 
	 * @throws IllegalArguementException If the domain model is null.
	 */

	public static ActivityTypeManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException ();
		}

		return (ActivityTypeManager) model.getManager (ActivityTypeManager.TYPE);
	}

	/**
	 * Create the ActivityType manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this ActivityType manager.
	 */

	protected ActivityTypeManager (DomainModel model)
	{
		super (model, ActivityTypeManager.TYPE);
	}

	/**
	 * Retrieve the ActivityType object from the underlying data store which has
	 * the specified ActivitySource and name.
	 *
	 * @param source The ActivitySource containing the ActivityType
	 * @param name The name of the ActivityType
	 * @return The ActivityType object which is associated with the specified
	 * source and name.
	 */

	public ActivityType fetchByName (ActivitySource source, String name)
	{
		return null;
	}

	/**
	 * Create an association between an Activity Typs and an Action.  Both the 
	 * ActivityType and the action must be owned by the datastore which is 
	 * associated with the ActivityTypeManager that to create the association
	 * between the ActivityType and the Action.
	 *
	 * @param type The ActivityType to modify.
	 * @param action The Action to be associated with the ActivityType.
	 */

	public void addAction (ActivityType type, Action action)
	{
	}

	/**
	 * Break an association between an ActivityType and an Action.  To break an
	 * association between the specified ActivityType and Action, both the 
	 * ActivityType and Action must be members of the the domain model with which
	 * this ActivityType manager is associated.  Futhermore there must be an
	 * existing association between the ActivityType and the Action, and there
	 * must not exist any log entries containing both the specified ActivityType
	 * and the specified action.
	 *
	 * @param type The ActivityType to modify.
	 * @param action The Action to remove from the ActivityType.
	 */

	public void removeAction (ActivityType type, Action action)
	{
	}
}
