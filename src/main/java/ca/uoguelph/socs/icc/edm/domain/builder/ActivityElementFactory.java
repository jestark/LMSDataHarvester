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
 * Abstract factory interface for creating <code>Activity</code>. This
 * interface contains definitions for the methods which are common across the
 * <code>ElementFactory</code> implementations for <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityElementFactory
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityBuilder
 */

public interface ActivityElementFactory extends AbstractActivityElementFactory
{
	/**
	 * Add the specified <code>Grade</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> to which the
	 *                  <code>Grade</code> is to be added, not null
	 * @param  grade    The <code>Grade</code> to add to the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added to the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean addGrade (Activity activity, Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  activity The <code>Activity</code> from which the
	 *                  <code>Grade</code> is to be removed, not null
	 * @param  grade    The <code>Grade</code> to remove from the
	 *                  <code>Activity</code>, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from the <code>Activity</code>,
	 *                  <code>False</code> otherwise
	 */

	public abstract boolean removeGrade (Activity activity, Grade grade);
}
