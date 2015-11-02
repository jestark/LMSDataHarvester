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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import ca.uoguelph.socs.icc.edm.domain.Network;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>Network</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Network</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Network</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class NetworkData extends Network implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>Network</code> */
	private Long id;

	/** The name of the <code>Network</code> */
	private String name;

	/**
	 * Static initializer to register the <code>NetworkData</code> class with
	 * the factories.
	 */

	static
	{
		Implementation.getInstance (Network.class, NetworkData.class, NetworkData::new);
	}

	/**
	 * Create the <code>Network</code> with null values.
	 */

	protected NetworkData ()
	{
		this.id = null;
		this.name = null;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Network</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Network</code>
	 * instance is loaded, or by the <code>NetworkBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Network</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Network</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Network</code>
	 */

	@Override
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Network</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Network</code> instance
	 * is loaded.
	 *
	 * @param name The name of the <code>Network</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}
}
