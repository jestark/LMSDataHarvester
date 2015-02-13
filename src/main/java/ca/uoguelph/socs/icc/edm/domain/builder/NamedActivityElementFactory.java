/* Copyright (C) 2015 James E. Stark
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

import ca.uoguelph.socs.icc.edm.domain.Activity;

/**
 * Factory interface to create new <code>Activity</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class providing supplementary data for the
 * <code>Activity</code> domain model interface.  It is intended to be used
 * along with an implementation of the <code>ActivityElementFactory</code>
 * interface.  Since instances of the classes that provide supplementary data
 * for the <code>Activity</code> interface do not have <code>DataStore</code>
 * ID's, the implementations of the <code>setId</code> method must throw a
 * <code>UnsupportedOperationException</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityBuilder
 * @see     java.lang.UnsupportedOperationException
 */

public interface NamedActivityElementFactory extends ElementFactory<Activity>
{
	/**
	 * Create a new <code>Activity</code> instance.
	 *
	 * @param  instance The <code>Activity</code> containing the instance data,
	 *                  not null
	 * @param  name     The name of the <code>Activity</code>, not null
	 *
	 * @return          The new <code>Activity</code> instance
	 */

	public abstract Activity create (Activity instance, String name);
}
