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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * An <code>IdGenerator</code> which return ID numbers from a sequence.  ID
 * numbers returned by this <code>IdGenerator</code> come from a sequence which
 * is incremented by one before the new ID number is returned.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class SequentialIdGenerator implements IdGenerator
{
	/**
	 * Implementation of the <code>IdGeneratorImplFatory</code> to create
	 * <code>RamdomIdGenerator</code> instances.
	 */

	private static final class Factory implements IdGeneratorImplFactory
	{
		/**
		 * Create the <code>IdGenerator</code> using the specified
		 * <code>DataStoreQuery</code>.
		 *
		 * @param  query The <code>DataStoreQuery</code>, not null
		 *
		 * @return The <code>IdGenerator</code> instance
		 */

		public IdGenerator create (DataStoreQuery<?> query)
		{
			return new SequentialIdGenerator (query.queryMaxId ());
		}
	}

	/** The next value to be returned by the generator. */
	private long currentid;

	/**
	 * Static initializer to register the <code>IdGenerator</code> with the
	 * <code>IdGeneratorFactory</code>.
	 */

	static
	{
		(IdGeneratorFactory.getInstance ()).registerClass (SequentialIdGenerator.class, new Factory ());
	}

	/**
	 * Create a new <code>SequentialIdGenerator</code>.
	 */

	public SequentialIdGenerator ()
	{
		this.currentid = 0;
	}

	/**
	 * Create a new <code>SequentialIdGenerator</code>, with a specified
	 * starting value for the sequence.  The first value returned by the
	 * <code>IdGenerator</code> will be one greater than the specified starting
	 * value.
	 *
	 * @param  startingid The starting value of the sequence, not null
	 */

	public  SequentialIdGenerator (Long startingid)
	{
		this.currentid = startingid.longValue ();
	}

	/**
	 * Return the next available id number.  This method increments the ID
	 * number sequence and returns the newly calculated number.
	 *
	 * @return A <code>Long</code> containing the next id number
	 */

	public Long nextId ()
	{
		Long result = new Long (this.currentid);

		this.currentid += 1;

		return result;
	}
}
