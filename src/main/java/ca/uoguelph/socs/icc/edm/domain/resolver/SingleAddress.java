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

package ca.uoguelph.socs.icc.edm.domain.resolver;

import java.net.InetAddress;

/**
 * Representation of a single Internet Protocol address.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class SingleAddress extends NetAddress
{
	/** The Internet Protocol address */
	private final InetAddress address;

	/**
	 * Create the <code>SingleAddress</code>.
	 *
	 * @param  address The raw Internet Protocol address, not null
	 */

	public SingleAddress (final InetAddress address)
	{
		assert address != null : "address is NULL";

		this.address = address;
	}

	/**
	 * Get the raw Internet Protocol address
	 *
	 * @return A byte array containing the IP address
	 */

	@Override
	public byte[] getAddress ()
	{
		return this.address.getAddress ();
	}

	/**
	 * Determine if the specified <code>NetAddress</code> is a member of the
	 * network represented by this <code>NetAddress</code>.  For a
	 * <code>SingleAddress</code> this is the same as a call to
	 * <code>equals</code>.
	 *
	 * @param  address The <code>NetAddress</code> to test, not null
	 */

	@Override
	public boolean hasMember (final NetAddress address)
	{
		return this.equals (address);
	}

	/**
	 * Get a <code>String</code> representation of the <code>NetAddress</code>.
	 *
	 * @return A <code>String</code> representing the <code>NetAddress</code>
	 */

	@Override
	public String toString ()
	{
		return this.address.toString ();
	}
}
