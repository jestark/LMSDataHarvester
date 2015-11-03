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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.metadata.Accessor;

/**
 * An ID number generator.  This class and its subclasses provide ID numbers
 * suitable for use with the underlying <code>DataStore</code>.  Each class
 * implementing this interface is responsible for determining how the  ID
 * numbers are calculated, with different classes providing different
 * distributions of ID numbers.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class IdGenerator<T extends Element>
{
	private Accessor<T, Long> accessor;

	/**
	 * Create the <code>IdGenerator</code>.
	 *
	 * @param  accessor The <code>Accessor</code> for the ID, not null
	 */

	protected IdGenerator (final Accessor<T, Long> accessor)
	{
		assert accessor != null : "accessor is NULL";

		this.accessor = accessor;
	}

	/**
	 * Assign an ID number to the specified <code>Element</code> instance.
	 * This method writes the next available ID number into the specified
	 * <code>Element</code> instance using the supplied <code>MetaData</code>
	 * instance.
	 *
	 * @param  <T>      The type of the <code>Element</code>
	 * @param  element  The <code>Element</code>, not null
	 */

	public void setId (final T element)
	{
		assert element != null : "element is NULL";

		accessor.setValue (element, this.nextId ());
	}

	/**
	 * Return the next available ID number.
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();
}
