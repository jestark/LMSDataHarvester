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

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * An instance of this class is used to generate unique random ID numbers.
 * The id numbers are generated using a 64bit crypographically secure pseudo
 * random number generation algorithm.  All id's returned by an instance of
 * this class are cached to ensure that no Id number is returned twice.  The
 * user has the option of specifiying a list of previously used id numbers,
 * which the instance will avoid returning.
 *
 * @author James E. Stark
 * @version 1.0
 */

public class RandomIdGenerator implements IdGenerator
{
	/**
	 * The set of previously used id numbers.
	 */

	private Set<Long> usedids;
	
	/**
	 * The random number generator.
	 */

	private SecureRandom generator;

	/**
	 * Creates a new Random Id number generator.  This constructor assums that
	 * no id numbers have been generated previously.
	 */

	public RandomIdGenerator ()
	{
		this.usedids = new HashSet<Long> ();
		this.generator = new SecureRandom ();
	}

	/**
	 * Creates a new Ramdom Id number generator with a set of previously used 
	 * Ids.  
	 *
	 * @param usedids A set containing all of the previously gernerated Id numbers
	 */

	public RandomIdGenerator (Set<Long> usedids)
	{
		this ();
		this.usedids.addAll (usedids);
	}

	/**
	 * Returns the next available id number.
	 *
	 * @return A randomly generated Long ID number.
	 */

	@Override
	public Long nextId ()
	{
		Long result = null;

		do
		{
			result = new Long (this.generator.nextLong ());
		} while (! this.usedids.add (result));

		return result;
	}

	/**
	 * Return a copy of the set of previously used Id numbers.  Includes all of
	 * Id gererated by this instance of the Id generator, and the set of 
	 * previously used Id numbers which was specified when the class was
	 * instantiated.
	 *
	 * @return The set of perviously used id numbers.
	 */

	public Set<Long> getUsedIds ()
	{
		return new HashSet<Long> (this.usedids);
	}
}
