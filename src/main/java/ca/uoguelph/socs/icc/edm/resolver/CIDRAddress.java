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
import java.net.UnknownHostException;
import java.util.Objects;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic representation of a Classless Inter-Domain Routing address.  This
 * class can represent an IPV4 or IPV6 host or network address.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class CIDRAddress implements Comparable<CIDRAddress>
{
	/**
	 * Builder for the <code>CIDRAddress</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Builder
	{
		/** The log*/
		private final Logger log;

		/** The base address of the network block */
		private @Nullable InetAddress address;

		/** The length of the network mask */
		private int length;

		/**
		 * Create the <code>Builder</code>.
		 */

		private Builder ()
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			this.address = null;
			this.length = -1;
		}

		/**
		 * Build the <code>CIDRAddress</code>.
		 *
		 * @return The new <code>CIDRAddress</code>
		 */

		public CIDRAddress build ()
		{
			this.log.trace ("build:");

			Preconditions.checkState (this.address != null, "address is NULL");

			if (this.length < 0)
			{
				this.length = this.address.getAddress ().length * 8;
			}

			return new CIDRAddress (this);
		}

		/**
		 * Get the base address for the <code>CIDRAddress</code>.
		 *
		 * @return An <code>InetAddress</code> representing the base address
		 *         for the block, may be null
		 */

		@CheckReturnValue
		public InetAddress getAddress ()
		{
			return this.address;
		}

		/**
		 * Set the base address for the <code>CIDRAddress</code>.
		 *
		 * @param  address The base address, not null
		 * @return         This <code>Builder</code>
		 */

		public Builder setAddress (final InetAddress address)
		{
			this.log.trace ("setAddress: address={}", address);

			this.address = Preconditions.checkNotNull (address, "address");

			return this;
		}

		/**
		 * Set the base address for the <code>CIDRAddress</code>.
		 *
		 * @param  address The base address, not null
		 * @return         This <code>Builder</code>
		 */

		public Builder setAddress (final String address) throws UnknownHostException
		{
			this.log.trace ("setAddress: address={}", address);

			this.address = InetAddress.getByName (Preconditions.checkNotNull (address, "address"));

			return this;
		}

		/**
		 * Get the length of the netmask for the <code>CIDRAddress</code>.
		 *
		 * @return The number of set bits in the netmask
		 */

		public int getLength ()
		{
			return this.length;
		}

		/**
		 * Set the length of the netmask for the <code>CIDRAddress</code>.
		 *
		 * @param  length The number of set bits in the netmask, must not be
		 *                negative.
		 * @return        This <code>Builder</code>
		 */

		public Builder setLength (final int length)
		{
			this.log.trace ("setLength: length={}", length);

			Preconditions.checkArgument (length >= 0, "length is negative");
			this.length = length;

			return this;
		}
	}

	/** The log */
	private final Logger log;

	/** The Internet Protocol address */
	private final InetAddress address;

	/** The number of set bits in the net mask */
	private final int length;

	/** Integer representation of the ip address */
	private final BigInteger ipAddress;

	/** The netmask */
	private final BigInteger mask;

	/**
	 * Get a new <code>Builder</code> instance.
	 *
	 * @return The <code>Builder</code>
	 */

	public static Builder builder ()
	{
		return new Builder ();
	}

	/**
	 * Create a new <code>CIDRAddress</code> for a host with the specified
	 * <code>InetAddress</code>.  This is a convenience method to create a
	 * <code>CIDRAddress</code> from an <code>InetAddress</code>.  The network
	 * mask in the resulting <code>CIDRAddress</code> will have all of its bits
	 * set.
	 *
	 * @param  address The <code>InetAddress</code>, not null
	 * @return         The <code>CIDRAddress</code>
	 */

	public static CIDRAddress create (final InetAddress address)
	{
		return new Builder ()
			.setAddress (address)
			.build ();
	}

	/**
	 * Get a representation of the IP Address as an integer from the
	 * <code>InetAddress</code>.
	 *
	 * @param  address The <code>InetAddress</code>, not null
	 * @return         The IP Address
	 */

	private static BigInteger calculateIPAddress (final InetAddress address)
	{
		assert address != null : "addr is NULL";

		BigInteger result = BigInteger.ZERO;

		for (byte b : address.getAddress ())
		{
			result = result.shiftLeft (8);
			result = result.or (BigInteger.valueOf (b & 0xFF));
		}

		return result;
	}

	/**
	 * Calculate the Network mask for the network.
	 *
	 * @param  address The <code>InetAddress</code>, not null
	 * @param  cidrlen The number of set bits in the mask
	 * @return         The netmask
	 */

	private static BigInteger calculateNetMask (final InetAddress address, final int cidrlen)
	{
		BigInteger result = BigInteger.ZERO;

		for (int i = 0; i < address.getAddress ().length / 4; i ++)
		{
			result = result.shiftLeft (32);

			if (cidrlen - (i * 32) >= 32)
			{
				result = result.or (BigInteger.valueOf (0xFFFFFFFFL));
			}
			else if (cidrlen - (i * 32) > 0)
			{
				result = result.or (BigInteger.valueOf ((0xFFFFFFFFL << (((i + 1) * 32) - cidrlen)) & 0x00000000FFFFFFFFL));
			}
		}

		return result;
	}

	/**
	 * Create the <code>CIDRAddress</code> from the <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	private CIDRAddress (final Builder builder)
	{
		assert builder != null : "builder is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.address = builder.address;
		this.length = builder.length;

		this.ipAddress = CIDRAddress.calculateIPAddress (this.address);
		this.mask = CIDRAddress.calculateNetMask (this.address, length);
	}

	/**
	 * Determine if two <code>CIDRAddress</code> instances have the same value.
	 * The addresses are compared solely based on the output of
	 * <code>getAddress</code>.
	 *
	 * @param  obj The <code>CIDRAddress</code> to test
	 * @return     <code>True</code> if the two <code>CIDRAddress</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof CIDRAddress)
			&& Objects.equals (this.ipAddress, ((CIDRAddress) obj).ipAddress);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>CIDRAddress</code> instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.ipAddress);
	}

	/**
	 * Compare a <code>CIDRAddress</code> instance to the one represented by
	 * this class.
	 *
	 * @return A negative integer, zero, or a positive integer as this
	 *         <code>CIDRAddress</code> is less than, equal to, or greater than
	 *         the specified <code>CIDRAddress</code>.
	 */

	@Override
	public int compareTo (final CIDRAddress address)
	{
		Preconditions.checkNotNull (address, "address");

		return this.ipAddress.compareTo (address.ipAddress);
	}

	/**
	 * Get a <code>String</code> representation of the <code>CIDRAddress</code>.
	 *
	 * @return A <code>String</code> representing the <code>CIDRAddress</code>
	 */

	@Override
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("address", this.address)
			.add ("length", this.length)
			.toString ();
	}

	/**
	 * Get the <code>InetAddress</code> which corresponds to the
	 * <code>CIDRAddress</code> instance.
	 *
	 * @return The <code>InetAddress</code>
	 */

	public final InetAddress getAddress ()
	{
		return this.address;
	}

	/**
	 * Get the IP address as a <code>String</code>.
	 *
	 * @return A <code>String</code> containing the IP Address.
	 */

	public final String getHostAddress ()
	{
		return this.address.getHostAddress ();
	}

	/**
	 * Get the number of bits set in the network mask.
	 *
	 * @return The number of set bits
	 */

	public final int getLength ()
	{
		return this.length;
	}

	/**
	 * Determine if the specified <code>CIDRAddress</code> is a member of the
	 * network represented by this <code>CIDRAddress</code>.
	 *
	 * @param  address The <code>CIDRAddress</code> to test, not null
	 */

	public boolean hasMember (final CIDRAddress address)
	{
		this.log.trace ("hasMember: address={}", address);

		Preconditions.checkNotNull (address, "address");

		return this.ipAddress.equals (address.ipAddress.and (this.mask));
	}
}
