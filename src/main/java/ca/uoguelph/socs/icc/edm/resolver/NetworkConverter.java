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

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Network;
import ca.uoguelph.socs.icc.edm.domain.NetworkBuilder;

/**
 *
 */

public final class NetworkConverter
{
	/** The log */
	private final Logger log;

	/** Cache of <code>Network</code> instances */
	private final Map<String, Network> cache;

	/** Builder to create <code>Network</code> instances */
	private final NetworkBuilder builder;

	/** The IP address to <code>Network</code> resolver */
	private final Resolver resolver;

	/**
	 * Create the <code>NetworkConverter</code>
	 *
	 * @param  dest   The destination <code>DomainModel</code>, not null
	 */

	public NetworkConverter (final DomainModel dest)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.builder = Network.builder (dest);

		this.resolver = Resolver.getInstance ();

		this.cache = new HashMap<> ();
	}

	/**
	 *
	 * @param  ipAddress The Internet Protocol Address, not null
	 *
	 * @return           The <code>Network</code>
	 */

	public Network convert (final String ipAddress)
	{
		this.log.trace ("convert: ipAddress={}", ipAddress);

		if (ipAddress == null)
		{
			throw new NullPointerException ();
		}

		String netName = this.resolver.getOrgName (ipAddress);

		if (! this.cache.containsKey (netName))
		{
			this.cache.put (netName, this.builder.setName (netName)
					.build ());
		}

		return this.cache.get (netName);
	}
}
