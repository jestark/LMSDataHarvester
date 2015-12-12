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

import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache mapping <code>AddressBlock</code> instances to the name of the owning
 * organization.  This class maintains a cache of <code>AddressBlock</code>
 * instances, mapped to the owning organization.  When the cache is queried
 * with a <code>NetAddress</code> it find the <code>AddressBlock</code> that
 * contains the <code>NetAddress</code> and returns the name of the associated
 * owner.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class AddressCache
{
	/** The log */
	private final Logger log;

	/** The address cache */
	private final NavigableMap<NetAddress, String> cache;

	/**
	 * Create the <code>AddressCache</code>.
	 */

	@Inject
	public AddressCache ()
	{
		this.log = LoggerFactory.getLogger (AddressCache.class);

		this.cache = new TreeMap<> ();
	}

	/**
	 * Determine if the specified <code>NetAddress</code> matches any entries
	 * in the cache.
	 */

	public boolean hasAddress (final NetAddress address)
	{
		this.log.trace ("hasAddress: address={}", address);

		NetAddress key = this.cache.floorKey (address);

		return ((key != null) && key.hasMember (address));
	}

	/**
	 * Get the name of the organization which owns the specified
	 * <code>NetAddress</code>.
	 *
	 * @param  address The <code>NetAddress</code>
	 *
	 * @return         The name of the owning organization, or
	 *                 <code>null</code> if there is no matching entry in the
	 *                 cache
	 */

	public String getOrg (final NetAddress address)
	{
		this.log.trace ("getOrg: address={}", address);

		NetAddress key = this.cache.floorKey (address);

		return ((key != null) && key.hasMember (address)) ? this.cache.get (key) : null;
	}

	/**
	 * Add an address to organization mapping to the cache.
	 *
	 * @param  address The address to cache, not null
	 * @param  org     The name of tho owning organization, not null
	 */

	public void addOrg (final AddressBlock address, final String org)
	{
		this.log.trace ("addOrg: address={}, org={}", address, org);

		assert address != null : "address is NULL";
		assert org != null : "org is NULL";
		assert ! this.cache.containsKey (address) : "address is already registered";

		this.cache.put (address, org);
	}

	/**
	 * Get the <code>Set</code> of cached addresses.
	 *
	 * @return A <code>Set</code> containing all of the cached addresses
	 */

	public Set<NetAddress> getAddresses ()
	{
		return this.cache.keySet ();
	}
}
