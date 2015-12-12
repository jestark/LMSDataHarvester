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

/**
 * Carrier class for the results of a whois query.
 */

public final class QueryResult
{
	/** The <code>Set</code> of <code>AddressBlock</code> instances */
	private final Set<AddressBlock> blocks;

	/** The name of the organization */
	private final String orgName;

	/**
	 * Create the <code>QueryResult</code>.
	 *
	 * @param  orgName The name of the organization, not null
	 * @param  blocks  The <code>Set</code> of <code>AddressBlock</code>
	 *                 instances, not null
	 */

	protected QueryResult (final String orgName, final Set<AddressBlock> blocks)
	{
		this.orgName = orgName;
		this.blocks = blocks;
	}

	/**
	 * Get the name of the organization returned by the whois query.
	 *
	 * @return The name of the organization
	 */

	public String getName ()
	{
		return this.orgName;
	}

	/**
	 * Get the <code>Set</code> of <code>AddressBlock</code> instances
	 * returned by the whois query.
	 *
	 * @return A <code>Set</code> of <code>AddressBlock</code> instances
	 */

	public Set<AddressBlock> getBlocks ()
	{
		return this.blocks;
	}
}
