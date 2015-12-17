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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Preconditions;

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

@Singleton
public final class Resolver
{
	/** The log */
	private final Logger log;

	/** The address cache */
	private final NavigableMap<CIDRAddress, String> cache;

	/** Query to fetch whois data*/
	private final WhoisQuery query;

	/**
	 * Create the <code>NetResolver</code>.
	 */

	@Inject
	Resolver (final WhoisQuery query)
	{
		this.log = LoggerFactory.getLogger (Resolver.class);

		assert query != null : "query is NULL";

		this.cache = new TreeMap<> ();
		this.query = query;
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

		final CIDRAddress addr = CIDRAddress.create (Preconditions.checkNotNull (address, "address"));

		String result = null;

		Map.Entry<CIDRAddress, String> entry = this.cache.floorEntry (addr);

		if ((entry != null) && (entry.getKey ().hasMember (addr)))
		{
			this.log.debug ("Address {} is cached, returning cached value: {}", address, entry.getValue ());
			result = entry.getValue ();
		}
		else
		{
			this.log.debug ("Address {} is not cached, executing Whois query", address);

			for (NetBlock block : this.query.getNetBlocks (addr))
			{
				if (! this.cache.containsKey (block.getAddress ()))
				{
					this.cache.put (block.getAddress (), block.getOwner ());
				}

				if (block.getAddress ().hasMember (addr))
				{
					result = block.getOwner ();
				}
			}

			assert result != null : "failed to get a Network";
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

		Preconditions.checkNotNull (address, "address");

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

	public Stream<InetAddress> getAddresses ()
	{
		return this.cache.keySet ()
			.stream ()
			.map (CIDRAddress::getAddress);
	}
}
