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
 * A representation of the source of a <code>Activity</code> in the domain
 * model.  Instances of the <code>ActivitySource</code> interface represent the
 * system from which the data for an <code>Activity</code> with a particular
 * <code>ActivityType</code> was originally harvested.  For example, an
 * <code>ActivitySource</code> could be "moodle" for data collected from the
 * Moodle Learning Management System, or "manual" for data that was collected
 * by the instructor manually.
 * <p>
 * Within the domain model the <code>ActivitySource</code> interface is a root
 * level element, as such instances of the <code>ActivitySource</code>
 * interface are not dependant upon any other domain model element to exist.
 * An associated instance of the <code>ActivitySource</code> interface is
 * required for an instance of the <code>ActivityType</code> interface to
 * exist.  If a particular instance of the <code>ActivitySource</code>
 * interface is deleted, then all of the associated instances of the
 * <code>ActivityType</code> interface must be deleted as well.
 * <p>
 * Once created, <code>ActivitySource</code> instances are immutable.

 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivitySourceBuilder
 * @see     ActivitySourceLoader
 */

public interface ActivitySource extends Element
{
	/**
	 * Enumeration of all of the properties of an <code>ActivitySource</code>.
	 * Properties represent the data contained within the
	 * <code>ActivitySource</code> instance.
	 */

	public static enum Properties { ID, NAME };

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances for the
	 * <code>ActivitySource</code>.  If there are no <code>ActivityType</code>
	 * instances associated with the <code>ActivitySource</code> then the
	 * <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	public abstract Set<ActivityType> getTypes ();
}
