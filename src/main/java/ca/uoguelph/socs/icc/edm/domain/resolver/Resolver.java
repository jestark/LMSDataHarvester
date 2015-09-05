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

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 */

class NetResolver
{
	/** The singleton instance */
	private static final NetResolver instance;

	/** The address cache */
	private final NavigableMap<NetAddress, String> cache;

	/**
	 * static initializer to create the singleton instance.
	 */

	static
	{
		instance = new NetResolver ();
	}

	/**
	 * Create the <code>NetResolver</code>.
	 */

	private NetResolver ()
	{
		this.cache = new TreeMap<> ();
	}


}
