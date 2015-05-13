/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;

/**
 * Retrieve <code>Element</code> instances from the <code>DataStore</code>.
 * Implementations of this interface are responsible for fetching
 * <code>Element</code> instances from the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.1
 * @param   <T> The type of <code>Element</code> to be retrieved
 */

public interface ElementLoader<T extends Element>
{
	/**
	 * Retrieve a <code>Element</code> instance from the <code>DataStore</code>
	 * based upon is <code>DataStore</code> identifier.
	 *
	 * @param  id The <code>DataStore</code> identifier of the
	 *            <code>Element</code> instance to retrieve, not null
	 *
	 * @return    The requested <code>Element</code> instance
	 */

	public abstract T fetchById (Long id);

	/**
	 * Retrieve a <code>List</code> of all of the <code>Element</code>
	 * instances represented by the <code>ElementLoader</code> instance from
	 * the <code>DataStore</code>.
	 *
	 * @return A <code>List</code> of <code>Element</code> instances
	 */

	public abstract List<T> fetchAll ();
}
