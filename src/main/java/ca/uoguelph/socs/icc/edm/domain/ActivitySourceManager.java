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
 * @see ca.uoguelph.socs.icc.edm.domain.ActivitySource The ActivitySource interface
 * @author James E. Stark
 * @version 1.0
 */

public final class ActivitySourceManager extends Manager<ActivitySource>
{
	/**
	 * Domain Model Type constant.  Used by the domain model to determine which
	 * implementation classes to use, and for instantiation of this manager.
	 */

	public static final DomainModelType TYPE = DomainModelType.ACTIVITYSOURCE;

	/**
	 * Get an instance of the ActivitySourceManager for the specified domain model.
	 *
	 * @param model The instance of the Domain model for which the ActivitySourceManager
	 * is to be retrieved.
	 * @return The ActivitySourceManager instance for the specified domain model.
	 * 
	 * @throws IllegalArguementException If the domain model is null.
	 */

	public static ActivitySourceManager getInstance (DomainModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException ();
		}

		return (ActivitySourceManager) model.getManager (ActivitySourceManager.TYPE);
	}

	/**
	 * Create the ActivitySource manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this ActivitySource manager.
	 */

	protected ActivitySourceManager (DomainModel model)
	{
		super (model, ActivitySourceManager.TYPE);
	}

	/**
	 * Retrive the ActivitySource object associated with the specified name
	 * from the underlying datastore.
	 *
	 * @param name The name of the ActivitySource to retrieve
	 * @return The ActivitySource object associated with the specified name.
	 */

	public ActivitySource fetchByName (String name)
	{
		return null;
	}
}
