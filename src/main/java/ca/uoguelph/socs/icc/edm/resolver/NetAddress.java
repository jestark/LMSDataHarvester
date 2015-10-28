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

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Objects;

/**
 * Generic representation of an Internet Protocol addresses and networks.
 *
 * @author  James E. Stark
 * @version 1.0
 */

abstract class NetAddress implements Comparable<NetAddress>
{
	/**
	 * Determine if two <code>NetAddress</code> instances have the same value.
	 * The addresses are compared solely based on the output of
	 * <code>getAddress</code>.
	 *
	 * @param  obj The <code>NetAddress</code> to test
	 *
	 * @return     <code>True</code> if the two <code>NetAddress</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof NetAddress)
			&& Objects.equals (this.getAddress (), ((NetAddress) obj).getAddress ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>NetAddress</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getAddress ());
	}

	/**
	 * Compare a <code>NetAddress</code> instance to the one represented by
	 * this class.
	 *
	 * @return A negative integer, zero, or a positive integer as this
	 *         <code>NetAddress</code> is less than, equal to, or greater than
	 *         the specified <code>NetAddress</code>.
	 */

	@Override
	public int compareTo (final NetAddress address)
	{
		if (address == null)
		{
			throw new NullPointerException ();
		}

		return this.getAddress ().compareTo (address.getAddress ());
	}

	/**
	 * Get the raw Internet Protocol address
	 *
	 * @return A byte array containing the IP address
	 */

	public abstract BigInteger getAddress ();

	/**
	 * Get the IP address as a <code>String</code>.
	 *
	 * @return A <code>String</code> containing the IP Address.
	 */

	public abstract String getHostAddress ();

	/**
	 * Get the <code>InetAddress</code> which corresponds to the
	 * <code>NetAddress</code> instance.
	 *
	 * @return The <code>InetAddress</code>
	 */

	public abstract InetAddress getInetAddress ();

	/**
	 * Get the length (in bytes) of the IP Address.
	 *
	 * @return The number of bytes in the IP Address
	 */

	public abstract int getLength ();

	/**
	 * Determine if the specified <code>NetAddress</code> is a member of the
	 * network represented by this <code>NetAddress</code>.
	 *
	 * @param  address The <code>NetAddress</code> to test, not null
	 */

	public abstract boolean hasMember (NetAddress address);
}
