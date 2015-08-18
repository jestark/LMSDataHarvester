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

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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

public abstract class Network extends Element
{
	/** The name of the <code>Network</code> */
	public static final Property<String> NAME;

	/** Select an <code>Network</code> instance by its name */
	public static final Selector SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>Network</code>.
	 */

	static
	{
		NAME = Property.getInstance (String.class, "name", false, true);

		SELECTOR_NAME = Selector.getInstance (NAME, true);

		Definition.getBuilder (Network.class, Element.class)
			.addProperty (ID, Network::getId, Network::setId)
			.addProperty (NAME, Network::getName, Network::setName)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get the name of the <code>Network</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Role</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>Network</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Network</code> instance
	 * is loaded.
	 *
	 * @param name The name of the <code>Network</code>
	 */

	protected abstract void setName (String name);
}

