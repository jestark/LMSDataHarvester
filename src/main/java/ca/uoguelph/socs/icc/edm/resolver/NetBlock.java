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

package ca.uoguelph.socs.icc.edm.resolver;

/**
 * Representation of an address block from a who-is query.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class NetBlock
{
	/** The <code>CIDRAddress</code> of the network block */
	private final CIDRAddress address;

	/** The name of the organization */
	private final String owner;

	/**
	 * Create the <code>QueryResult</code>.
	 *
	 * @param  owner   The name of the owning organization, not null
	 * @param  address The <code>CIDRAddress</code> of the network block, not
	 *                 null
	 */

	protected NetBlock (final String owner, final CIDRAddress address)
	{
		assert owner != null : "owner is NULL";
		assert address != null : "address is NULL";

		this.owner = owner;
		this.address = address;
	}

	/**
	 * Get the name of the organization which owns the <code>NetBlock</code>.
	 *
	 * @return The name of the organization
	 */

	public String getOwner ()
	{
		return this.owner;
	}

	/**
	 * Get the <code>CIDRAddress</code> of the <code>NetBlock</code>.
	 *
	 * @return The <code>CIDRAddress</code> of the network
	 */

	public CIDRAddress getAddress ()
	{
		return this.address;
	}
}
