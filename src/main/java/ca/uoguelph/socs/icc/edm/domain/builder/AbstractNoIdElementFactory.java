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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Abstract base class for <code>ElementFactory</code> which do not implement
 * the <code>setId</code> method.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of <code>Element</code> being created by the factory
 */

public abstract class AbstractNoIdElementFactory<T extends Element> implements ElementFactory<T>
{
	/**
	 * Write the specified <code>DataStore</code> ID number into the
	 * <code>Element</code>.
	 *
	 * @param  element                       The <code>Element</code> to which the
	 *                                       ID number is assigned, not null
	 * @param  id                            The ID number assigned to the
	 *                                       <code>Element</code>, not null
	 *
	 * @throws UnsupportedOperationException Method is no implemented
	 */

	public void setId (T element, Long id)
	{
		throw new UnsupportedOperationException ();
	}
}
