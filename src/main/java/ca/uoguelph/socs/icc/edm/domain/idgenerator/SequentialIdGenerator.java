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
 * An instance of this class will return a series of sequential ID numbers.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class SequentialIdGenerator implements IdGenerator
{
	/** The next value to be returned by the generator. */
	private long currentid;

	/**
	 * Creates a new sequential ID number generator, beginning the sequence at
	 * zero.
	 */

	public SequentialIdGenerator ()
	{
		this.currentid = 0;
	}

	/**
	 * Creates a new sequential ID number generator, while allowing the caller
	 * to specify the first value of the sequence.
	 *
	 * @param  startingid The first value to be returned by the generator.
	 */

	public  SequentialIdGenerator (Long startingid)
	{
		this.currentid = startingid.longValue ();
	}

	/**
	 * Increments the ID number sequence, which returning the next available ID.
	 *
	 * @return The next number in the sequence.
	 */

	public Long nextId ()
	{
		Long result = new Long (this.currentid);

		this.currentid += 1;

		return result;
	}
}
