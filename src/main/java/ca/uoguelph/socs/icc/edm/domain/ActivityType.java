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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

/**
 * A representation of the nature of a particular <code>Activity</code>.
 * Instances of the <code>ActivityType</code> interface serve to describe the
 * nature of the instances of the <code>Activity</code> interface with which
 * they are associated.  Example <code>ActivityType</code> instances include:
 * "Assignment," "Quiz," and "Presentation."  For instances of
 * <code>Activity</code> where the data was harvested from a Learning
 * Management System (such as Moodle) the <code>ActivityType</code> will be the
 * name of the module from which the data contained in the associated
 * <code>Activity</code> instances was harvested.
 * <p>
 * Instances <code>ActvityType</code> interface has a strong dependency on the
 * associated instances of the <code>ActivitySource</code> interface.  If an
 * instance of a particular <code>ActivitySource</code> is removed from the
 * <code>DataStore</code> then all of the associated instances of the
 * <code>ActivityType</code> interface must be removed as well.  Similarly,
 * instances of the <code>Activity</code> interface are dependent on the
 * associated instance of the <code>ActivityType</code> interface.  Removing an
 * instance of the <code>ActivityType</code> interface from the
 * <code>DataStore</code> will require the removal of the associated instances of
 * the <code>Activity</code> interface.
 * <p>
 * While the <code>ActvityType</code> interface is associated with the
 * <code>Action</code> interface, there is no dependency between them.  Instances
 * of <code>ActivityType</code> and <code>Action</code> may exist without any
 * associations.
 * <p>
 * With the exception of adding and removing associations to instance of the
 * <code>Action</code> interface, instances of the <code>ActivityType</code>
 * interface are immutable once created.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityTypeBuilder
 * @see     ActivityTypeManager
 */

public interface ActivityType extends Element
{
	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getSource ();

	/**
	 * Get the <code>Set</code> of <code>Action</code> instances which are
	 * associated with the <code>ActivityType</code>.  If there are no
	 * associated <code>Action</code> instances, then the <code>Set</code>
	 * will be empty.
	 *
	 * @return A <code>Set</code> of <code>Action</code> instances
	 */

	public abstract Set<Action> getActions ();
}
