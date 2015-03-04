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
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;

/**
 * Factory interface to create new <code>Activity</code> instances.
 * Implementations of this interface provide the functionality required to
 * create new instances of a class containing only the minimum data required to
 * implement the <code>Activity</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityBuilder
 */

public interface GenericActivityElementFactory extends ActivityElementFactory
{
	/**
	 * Create a new <code>Activity</code> instance.
	 * 
	 * @param  type   The <code>ActivityType</code> of the <code>Activity</code>,
	 *                not null
	 * @param  course The <code>Course</code> which is associated with the
	 *                <code>Activity</code> instance, not null
	 *
	 * @return        The new <code>Activity</code> instance
	 */

	public abstract Activity create (ActivityType type, Course course);
} 
