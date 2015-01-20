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

import java.util.Set;

/**
 * A representation of actions performed against activities 
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityType
 * @see     ActivityTypeManager
 * @see     ActionManager
 * @see     ActionBuilder
 */

public interface Action extends Element
{
	/**
	 * Get a Set of the Activity Types containing the action.
	 *
	 * @return A Set of classes implementing the <code>ActivityType</code>
	 *         interface.
	 */

	public abstract Set<ActivityType> getTypes ();

	/**
	 * Get the name of the action.
	 *
	 * @return A String containing the name of the action.
	 */

	public abstract String getName ();
}

