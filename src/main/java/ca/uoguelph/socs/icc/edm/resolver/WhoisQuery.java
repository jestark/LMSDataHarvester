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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetch the "whois" data for a network.  Implementations of this calls are
 * responsible for performing a "whois" query, extracting the network data and
 * organization name from the results, and entering the data into the cache.
 *
 * @author  James E. Stark
 * @version 1.0
 */

interface WhoisQuery
{
	/**
	 * Execute a "whois" query for the specified IP address.
	 *
	 * @param  address The IP address, not null
	 *
	 * @return         The name of the organization which owns the IP Address
	 */

	public abstract QueryResult getOrg (NetAddress address);
}
