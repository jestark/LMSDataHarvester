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

/**
 * An ID number generator.  Classes implementing this interface will provide
 * ID numbers suitable for use with the underlying <code>DataStore</code>.
 * Each class implementing this interface is responsible for determining how
 * the  ID numbers are calculated, with different classes providing different
 * distributions of ID numbers.  
 *
 * @author  James E. Stark
 * @version 1.0
 */

public interface IdGenerator
{
	/**
	 * Return the next available ID number.
	 *
	 * @return A <code>Long</code> containing the ID number
	 */

	public abstract Long nextId ();
}
