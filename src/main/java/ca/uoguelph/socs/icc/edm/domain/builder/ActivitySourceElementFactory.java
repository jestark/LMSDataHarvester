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
 * Factory interface to create new <code>ActivitySource</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class implementing the <code>ActivitySource</code>
 * domain model interface.  It also provides the functionality required to set
 * the <code>DataStore</code> ID for the <code>ActivitySource</code> instance,
 * as well as adding and removing any dependant <code>ActivityType</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder
 */

public interface ActivitySourceElementFactory extends ElementFactory<ActivitySource>
{
	/**
	 * Create a new <code>ActivitySource</code> instance.
	 *
	 * @param  name The name of the <code>ActivitySource</code>, not null
	 *
	 * @return      The new <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource create (String name);

	/**
	 * Add the specified <code>ActivityType</code> to the specified
	 * <code>ActivitySource</code>.
	 *
	 * @param  source The <code>ActivitySource</code> to which the
	 *                <code>ActivityType</code> is to be added, not null
	 * @param  type   The <code>ActivityType</code> to add to the
	 *                <code>ActivitySource</code>, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully added to the <code>ActivitySource</code>,
	 *                <code>False</code> otherwise
	 */

	public abstract boolean addActivityType (ActivitySource source, ActivityType type);

	/**
	 * Remove the specified <code>ActivityType</code> from the specified
	 * <code>ActivitySource</code>. 
	 *
	 * @param  source The <code>ActivitySource</code> from which the
	 *                <code>ActivityType</code> is to be removed, not null
	 * @param  type   The <code>ActivityType</code> to remove from the
	 *                <code>ActivitySource</code>, not null
	 *
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully removed from the
	 *                <code>ActivitySource</code>, <code>False</code> otherwise
	 */

	public abstract boolean removeActivityType (ActivitySource source, ActivityType type);
}
