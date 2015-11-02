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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * An <code>IdGenerator</code> which always returns a null reference.  This
 * <code>IdGenerator</code> is intended for situations where the application
 * logic requires that an ID number is assigned, but the actual ID number will
 * be determined though other means (such as being automatically assigned by an
 * underlying database).
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class NullIdGenerator extends IdGenerator
{
	/**
	 * Create the <code>NullIdGenerator</code>.
	 */

	private NullIdGenerator ()
	{
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
