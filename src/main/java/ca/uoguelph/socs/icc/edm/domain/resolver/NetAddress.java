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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof NetAddress)
		{
			result = new EqualsBuilder ()
				.append (this.getAddress (), ((NetAddress) obj).getAddress ())
				.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>NetAddress</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return new HashCodeBuilder (3, 5)
			.append (this.getAddress ())
			.toHashCode ();
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

		return new CompareToBuilder ()
			.append (this.getAddress (), address.getAddress ())
			.toComparison ();
	}

	/**
	 * Get the raw Internet Protocol address
	 *
	 * @return A byte array containing the IP address
	 */

	public abstract byte[] getAddress ();

	/**
	 * Determine if the specified <code>NetAddress</code> is a member of the
	 * network represented by this <code>NetAddress</code>.
	 *
	 * @param  address The <code>NetAddress</code> to test, not null
	 */

	public abstract boolean hasMember (NetAddress address);
}
