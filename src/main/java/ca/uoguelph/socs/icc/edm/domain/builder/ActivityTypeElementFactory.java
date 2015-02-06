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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Factory interface to create new <code>ActivityType</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>ActivityType</code>
 * domain model interface.  It also provides the functionality required to set
 * the <code>DataStore</code> ID for the <code>ActivityType</code> instance, as
 * well as adding and removing any associated <code>Action</code> instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder
 */

public interface ActivityTypeElementFactory extends ElementFactory<ActivityType>
{
	/**
	 * Create a new <code>ActivityType</code> instance.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>, not null
	 * @param  name   The name of the <code>ActivityType</code>, not null
	 * @return        The new <code>ActivityType</code> instance
	 */

	public abstract ActivityType create (ActivitySource source, String name);

	/**
	 * Add the specified <code>Action</code> to the specified
	 * <code>ActivityType</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to which the
	 *                <code>Action</code> is to be added, not null
	 * @param  action The <code>Action</code> to add to the
	 *                <code>ActivityType</code>, not null
	 *
	 * @return        <code>True</code> if the <code>Action</code> was
	 *                successfully added to the <code>ActivityType</code>,
	 *                <code>False</code> otherwise
	 */

	public abstract boolean addAction (ActivityType type, Action action);

	/**
	 * Remove the specified <code>Action</code> from the specified
	 * <code>ActivityType</code>. 
	 *
	 * @param  type   The <code>ActivityType</code> from which the <code>Action</code>
	 *                is to be removed, not null
	 * @param  action The <code>Action</code> to remove from the
	 *                <code>ActivityType</code>, not null
	 *
	 * @return        <code>True</code> if the <code>Action</code> was
	 *                successfully removed from the <code>ActivityType</code>,
	 *                <code>False</code> otherwise
	 */

	public abstract boolean removeAction (ActivityType type, Action action);
}
