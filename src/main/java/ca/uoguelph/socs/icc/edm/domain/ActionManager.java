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
 * Create, insert and remove actions from the domain model.  Through
 * implementations of this interface, Actions can be added to or removed
 * from the domain model.
 *
 * @author James E. Stark
 * @version 1.0
 */

public final class ActionManager extends AbstractManager<Action>
{
	/**
	 * Create the Action manager.
	 *
	 * @param model A reference to the instance of the domain model which owns
	 * this Action manager.
	 */

	protected ActionManager (DomainModel model)
	{
		super (model);
	}

	/**
	 * Retrieve the Action with the specified name from the datastore.
	 *
	 * @param name The name of the action to retrive
	 * @return The Action object associated with the specified name.
	 */

	public Action fetchByName (String name)
	{
		return null;
	}
}
