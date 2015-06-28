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

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.element.metadata.Selector;

/**
 * A representation of the <code>Action</code> that a <code>User</code>
 * performed upon an <code>Activity</code> as recored in a
 * <code>LogEntry</code>.  Some instances of the <code>Action</code> interface
 * are general (such as viewing the content associated with an instance of the
 * <code>Activity</code> interface) and will be associated with may instances
 * of the <code>ActivityType</code> interface.  Other instances of the
 * <code>Action</code> may be specific to one particular instance of a
 * <code>ActvityType</code>, such as submitting an assignment.
 * <p>
 * Within the domain model the <code>Action</code> interface is a root level
 * element, as such instances of the <code>Action</code> interface are not
 * dependant upon any other domain model element to exist.  An associated
 * instance of the <code>Action</code> interface is required for an instance of
 * the <code>LogEntry</code> interface to exist.  If a particular instance of
 * the <code>Action</code> interface is deleted, then all of the associated
 * instances of the <code>LogEntry</code> interface must be deleted as well.
 * <p>
 * While the <code>Action</code> interface is associated with the
 * <code>ActivityType</code> interface, there is no dependency between them.
 * Instances of <code>ActivityType</code> and <code>Action</code> may exist
 * without any associations. However, each instance of the <code>Action</code>
 * interface should be associated with at least one instance of the
 * <code>ActivityType</code> interface, to be used within an instance of the
 * <code>LogEntry</code> interface.
 * <p>
 * With the exception of adding and removing associations to instance of the
 * <code>ActivityType</code> interface, instances of the <code>Action</code>
 * interface are immutable once created.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActionLoader
 * @see     ActionBuilder
 */

public interface Action extends Element
{
	/**
	 * Constants representing all of the properties of an <code>Action</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>Action</code> instance.
	 */

	public static class Properties extends Element.Properties
	{
		/** The name of the <code>Action</code> */
		public static final Property<String> NAME = Property.getInstance (Action.class, String.class, "name", false, true);
	}

	/**
	 * Constants representing all of the selectors of an <code>Action</code>.
	 * A <code>Selector</code> represents the <code>Set</code> of
	 * <code>Property</code> instances used to load an <code>Action</code>
	 * from the <code>DataStore</code>.
	 */

	public static class Selectors extends Element.Selectors
	{
		/** Select an <code>Action</code> instance by its name */
		public static final Selector NAME = Selector.getInstance (Action.class, true, Action.Properties.NAME);
	}

	/**
	 * Get the <code>Set</code> of <code>ActivityType</code> instances
	 * containing the <code>Action</code>.
	 *
	 * @return A <code>Set</code> of <code>ActivityType</code> instances
	 */

	public abstract Set<ActivityType> getTypes ();

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	public abstract String getName ();
}

