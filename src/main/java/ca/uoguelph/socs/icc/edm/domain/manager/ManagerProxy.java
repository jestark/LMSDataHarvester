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

package ca.uoguelph.socs.icc.edm.domain.manager;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

public final class ManagerProxy<T extends Element>
{
	private final AbstractManager<T> manager;

	protected ManagerProxy (final AbstractManager<T> manager)
	{
		assert manager != null : "manager is NULL";

		this.manager = manager;
	}

	/**
	 * Test an instance of an <code>Element</code> to determine if a reference to
	 * that <code>Element</code> instance exists in the <code>DataStore</code>.
	 *
	 * @param  element The <code>Element</code> instance to test, not null
	 *
	 * @return          <code>True</code> if the <code>DataStore</code> instance
	 *                  contains a reference to the <code>Element</code>,
	 *                  <code>False</code> otherwise
	 */

	public boolean contains (final T element)
	{
		assert element != null : "element is NULL";

		return this.manager.contains (element);
	}

	/**
	 * Retrieve an <code>Element</code> from the <code>DataStore</code> which
	 * identifies the same as the specified <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> to retrieve, not null
	 *
	 * @return         A reference to the <code>Element</code> in the
	 *                 <code>DataStore</code>, may be null
	 */

	public T fetch (final T element)
	{
		assert element != null : "element is NULL";

		return this.manager.fetch (element);
	}
}
