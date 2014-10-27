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

package ca.uoguelph.socs.icc.edm.datastore;

/**
 * An ID number generator.  Classes implementing this inteface will provide
 * ID numbers suitable for use with the relevant underlying datastore.  Each
 * class implementing this interface is responsible for determining how the 
 * ID numbers are calculated, with diffeerent classes providing different
 * distributions of ID numbers.  
 *
 * @author James E. Stark
 * @version 1.0
 */

public interface IdGenerator
{
	/**
	 * Returns the next available ID number.
	 *
	 * @return A Long containing the ID number.
	 */

	public abstract Long nextId ();
}
