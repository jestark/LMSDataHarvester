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
 * An <code>IdGenerator</code> which return ID numbers from a sequence.  ID
 * numbers returned by this <code>IdGenerator</code> come from a sequence which
 * is incremented by one before the new ID number is returned.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class SequentialIdGenerator implements IdGenerator
{
	/** The next value to be returned by the generator. */
	private long currentid;

	/**
	 * Create a new <code>SequentialIdGenerator</code>, with a specified
	 * starting value for the sequence.  The first value returned by the
	 * <code>IdGenerator</code> will be one greater than the specified starting
	 * value.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  element   The <code>Element</code>, not null
	 */

	SequentialIdGenerator (final DataStore datastore, final Class<? extends Element> element)
	{
		assert datastore != null : "datastore is NULL";
		assert element != null : "element is NULL";

		this.currentid = datastore.getAllIds (element)
			.parallelStream ()
			.reduce (0L, Long::max)
			.longValue () + 1;
	}

	/**
	 * Return the next available id number.  This method increments the ID
	 * number sequence and returns the newly calculated number.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	@Override
	public Long nextId ()
	{
		Long result = Long.valueOf (this.currentid);

		this.currentid += 1;

		return result;
	}
}
