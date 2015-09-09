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
import java.net.UnknownHostException;

import java.util.Set;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolve IP addresses to the name of the organization which "owns" them.
 * This class performs a "whois" query on an IP address, and returns the name
 * for the organization field of the "whois" data.  Since it is expected that
 * the "whois" data will be queried for many IP addresses in the same network
 * the responses to the "whois" query is cached such that only one query will
 * be issued per network.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class Resolver
{
	/** The singleton instance */
	private static final Resolver instance;

	/** The log */
	private final Logger log;

	/** The address cache */
	private final AddressCache cache;

	/** Query to fetch whois data*/
	private final WhoisQuery query;

	/**
	 * static initializer to create the singleton instance.
	 */

	static
	{
		instance = new Resolver ();
	}

	/**
	 * Get an instance of the <code>Resolver</code>.
	 *
	 * @return The <code>Resolver</code>
	 */

	public static Resolver getInstance ()
	{
		return Resolver.instance;
	}

	/**
	 * Create the <code>NetResolver</code>.
	 */

	private Resolver ()
	{
		this.log = LoggerFactory.getLogger (Resolver.class);

		this.cache = new AddressCache ();
		this.query = new ARINQuery (this.cache);
	}

	/**
	 * Get the name of the Organization which "owns" the specified IP address.
	 *
	 * @param  address The IP address, not null
	 *
	 * @return         The name of the owning organization
	 */

	public String getOrgName (final InetAddress address)
	{
		this.log.trace ("getOwner: address={}", address);

		if (address == null)
		{
			throw new NullPointerException ();
		}

		String result = null;
		NetAddress addr = new SingleAddress (address);

		if (this.cache.hasAddress (addr))
		{
			this.log.debug ("Address {} is cached", address);
			result = this.cache.getOrg (addr);
		}
		else
		{
			this.log.debug ("Address {} is not cached, executing Whois query", address);
			result = this.query.getOrg (addr);
		}

		return result;
	}

	/**
	 * Get the name of the Organization which "owns" the specified IP address.
	 *
	 * @param  address The IP address, not null
	 *
	 * @return         The name of the owning organization
	 */

	public String getOrgName (final String address)
	{
		this.log.trace ("getOwner: address={}", address);

		if (address == null)
		{
			throw new NullPointerException ();
		}

		try
		{
			return this.getOrgName (InetAddress.getByName (address));
		}
		catch (UnknownHostException ex)
		{
			this.log.error ("The address ({}) is invalid", address);
			throw new IllegalArgumentException (ex);
		}
	}

	/**
	 * Get the <code>Set</code> of IP addresses which have been cached by the
	 * <code>Resolver</code>
	 *
	 * @return A <code>Set</code> of IP addresses
	 */

	public Set<InetAddress> getAddresses ()
	{
		return this.cache.getAddresses ()
			.stream ()
			.map (NetAddress::getInetAddress)
			.collect (Collectors.toSet ());
	}
}
