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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.metadata.Accessor;

/**
 * An <code>IdGenerator</code> which passes through the original value in the
 * <code>Element</code> instances unchanged.  This <code>IdGenerator</code> is
 * intended for situations where the application logic requires that the ID
 * number assigned to the <code>Element</code> instance is determined by a
 * process outside of the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class PassThruIdGenerator<T extends Element> extends IdGenerator<T>
{
	/**
	 * Create the <code>PassThruIdGenerator</code>.
	 *
	 * @param  accessor The <code>Accessor</code> for the ID, not null
	 */

	protected PassThruIdGenerator (final Accessor<T, Long> accessor)
	{
		super (accessor);
	}

	/**
	 * Assign an ID number to the specified <code>Element</code> instance.
	 * This method overrides the <code>setIdMethod</code> in the superclass to
	 * make it do nothing, since this <code>IdGenerator</code> is supposed to
	 * leave the existing ID unchanged.
	 *
	 * @param  metadata The <code>MetaData</code>, not null
	 * @param  element  The <code>Element</code>, not null
	 */

	@Override
	public void setId (final T element)
	{
		assert element != null : "element is NULL";

		// Does nothing
	}

	/**
	 * Return the next available ID number.  This method will always return
	 * null.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		return null;
	}
}
