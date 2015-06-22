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

import ca.uoguelph.socs.icc.edm.domain.element.metadata.Property;

/**
 * Root level interface for all of the elements of the domain model.  The
 * primary purpose of the <code>Element</code> interface is to allow instances
 * of the <code>ElementBuilder</code> and <code>ElementLoader</code>
 * interfaces along with their supporting infrastructure to refer to use
 * bounded generic types when referring to the domain model interfaces and
 * their implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ElementBuilder
 * @see     ElementLoader
 */

public interface Element
{
	/**
	 * Constants representing all of the properties of an <code>Element</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>Element</code> instance.
	 */

	public static class Properties
	{
		/** The <code>DataStore</code> identifier of the <code>Element</code> */
		public static final Property<Long> ID = Property.getInstance (Element.class, Long.class, "id", false, false);
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.  Some <code>Element</code> interfaces are dependent on other
	 * <code>Element</code> interfaces for their identification.  The dependent
	 * interface implementations should return the <code>DataStore</code>
	 * identifier from the interface on which they depend.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	public abstract Long getId ();

	/**
	 * Determine if two <code>Element</code> instances are identical.  This
	 * method acts as a stricter form of the equals method.  The equals method
	 * only compares properties that are required to be unique (and therefore
	 * immutable) for the <code>Element</code> instance, while this method
	 * compares all of the properties.
	 *
	 * @param  element The <code>Element</code> to compare to the current
	 *                 instance
	 *
	 * @return         <code>True</code> if the <code>Element</code> instances
	 *                 are logically identical, <code>False</code> otherwise
	 */

	public abstract boolean identicalTo (Element element);
}
