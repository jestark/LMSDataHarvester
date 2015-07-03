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

package ca.uoguelph.socs.icc.edm.domain;

import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation the network from which logged <code>Action</code> originated.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     NetworkBuilder
 * @see     NetworkLoader
 */

public interface Network extends Element
{
	/**
	 * Constants representing all of the properties of an <code>Network</code>.
	 * A <code>Property</code> represents a piece of data contained within the
	 * <code>Network</code> instance.
	 */

	public static class Properties extends Element.Properties
	{
		/** The name of the <code>Network</code> */
		public static final Property<String> NAME = Property.getInstance (Network.class, String.class, "name", false, true);
	}

	/**
	 * Constants representing all of the selectors of an <code>Network</code>.
	 * A <code>Selector</code> represents the <code>Set</code> of
	 * <code>Property</code> instances used to load an <code>Network</code>
	 * from the <code>DataStore</code>.
	 */

	public static class Selectors extends Element.Selectors
	{
		/** Select an <code>Network</code> instance by its name */
		public static final Selector NAME = Selector.getInstance (Network.class, true, Network.Properties.NAME);
	}

	/**
	 * Get the name of the <code>Network</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();
}

