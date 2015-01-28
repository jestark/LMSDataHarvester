/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.idgenerator;

/**
 * A ID number generator which always returns a null reference.  This ID
 * number generator is intended for situations where the application logic
 * requires that an ID number is assigned, but the actual ID number will be
 * determined though other means (such as being automatically assigned by an
 * underlying database).
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class NullIdGenerator implements IdGenerator
{

	/**
	 * Returns the next available Id number, which will always be a null 
	 * reference.
	 *
	 * @return null
	 */

	public Long nextId ()
	{
		return null;
	}
}
