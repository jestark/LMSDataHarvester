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
import java.util.Arrays;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of a block of Internet Protocol addresses.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class AddressBlock extends NetAddress
{
	/**
	 * Builder for the <code>AddressBlock</code>.
	 */

	public static final class Builder
	{
		/** The log*/
		private final Logger log;

		/** The base address of the network block */
		private InetAddress address;

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
		 * Build the <code>AddressBlock</code>.
		 *
		 * @return The new <code>AddressBlock</code>
		 */

		public AddressBlock build ()
		{
			this.log.trace ("build:");

			Preconditions.checkState (this.address != null, "address is NULL");

			if (this.length < 0)
			{
				this.length = this.address.getAddress ().length * 8;
			}

			return new AddressBlock (this);
		}

		/**
		 * Get the base address for the <code>AddressBlock</code>.
		 *
		 * @return An <code>InetAddress</code> representing the base address
		 *         for the block, may be null
		 */

		public InetAddress getAddress ()
		{
			return this.address;
		}

		/**
		 * Set the base address for the <code>AddressBlock</code>.
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
		 * Set the base address for the <code>AddressBlock</code>.
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
		 * Get the length of the netmask for the <code>AddressBlock</code>.
		 *
		 * @return The number of set bits in the netmask
		 */

		public int getLength ()
		{
			return this.length;
		}

		/**
		 * Set the length of the netmask for the <code>AddressBlock</code>.
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

	/** Integer representation of the ip address */
	private final BigInteger ipAddress;

	/** The netmask */
	private final BigInteger mask;

	/** The number of set bits in the net mask */
	private final int length;

	public static Builder builder ()
	{
		return new Builder ();
	}

	public static AddressBlock create (final InetAddress address)
	{
		return AddressBlock.builder ()
			.setAddress (address)
			.build ();
	}

	/**
	 * Create the <code>AddressBlock</code>.
	 *
	 * @param  address The raw Internet Protocol Address, not null
	 * @param  length  The number of set bits in the network mask, must be
	 *                 greater than zero
	 */

	private AddressBlock (final Builder builder)
	{
		assert builder != null : "builder is NULL";

		this.log = LoggerFactory.getLogger (AddressBlock.class);

		this.address = builder.address;
		this.length = builder.length;

		this.ipAddress = this.calculateIPAddress (this.address);
		this.mask = this.calculateNetMask (this.address.getAddress ().length, length);
	}

	/**
	 * Get a representation of the IP Address as an integer from the
	 * <code>InetAddress</code>.
	 *
	 * @param  addr The <code>InetAddress</code>, not null
	 * @return      The IP Address
	 */

	private BigInteger calculateIPAddress (final InetAddress addr)
	{
		assert addr != null : "addr is NULL";

		BigInteger result = BigInteger.ZERO;

		for (byte b : addr.getAddress ())
		{
			result = result.shiftLeft (8);
			result = result.or (BigInteger.valueOf (b & 0xFF));
		}

		return result;
	}

	/**
	 * Calculate the Netmask for the network.
	 *
	 * @param  len     The number of bytes in the IP Address
	 * @param  cidrlen The number of set bits in the mask
	 * @return         The netmask
	 */

	private BigInteger calculateNetMask (final int len, final int cidrlen)
	{
		BigInteger result = BigInteger.ZERO;

		for (int i = 0; i < len / 4; i ++)
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
	 * Get the raw Internet Protocol address
	 *
	 * @return A byte array containing the IP address
	 */

	@Override
	public BigInteger getAddress ()
	{
		return this.ipAddress;
	}

	/**
	 * Get the IP address as a <code>String</code>.
	 *
	 * @return A <code>String</code> containing the IP Address.
	 */

	@Override
	public String getHostAddress ()
	{
		return this.address.getHostAddress ();
	}

	/**
	 * Get the <code>InetAddress</code> which corresponds to the
	 * <code>NetAddress</code> instance.
	 *
	 * @return The <code>InetAddress</code>
	 */

	@Override
	public InetAddress getInetAddress ()
	{
		return this.address;
	}

	/**
	 * Get the length (in bytes) of the IP Address.
	 *
	 * @return The number of bytes in the IP Address
	 */

	@Override
	public int getLength ()
	{
		return this.address.getAddress ().length;
	}

	/**
	 * Determine if the specified <code>NetAddress</code> is a member of the
	 * network represented by this <code>NetAddress</code>.
	 *
	 * @param  address The <code>NetAddress</code> to test, not null
	 */

	@Override
	public boolean hasMember (final NetAddress address)
	{
		this.log.trace ("hasMember: address={}", address);

		return this.ipAddress.equals (address.getAddress ().and (this.mask));
	}

	/**
	 * Get a <code>String</code> representation of the <code>NetAddress</code>.
	 *
	 * @return A <code>String</code> representing the <code>NetAddress</code>
	 */

	@Override
	public String toString ()
	{
		return String.format ("%s/%d", this.address.toString (), this.length);
	}
}
