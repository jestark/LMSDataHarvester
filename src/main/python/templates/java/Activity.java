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

package ca.uoguelph.socs.icc.edm.domain.$Package;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.$ActivityBaseClass;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;

/**
 * Instances of this class act as a handle for data related to an
 * <code>Activity</code>.  This class was generated from a template, since by
 * default, the only piece of data associated with any given activity is a
 * string containing its name.  All of the actual information is stored by the
 * superclass, and this class exists to allow database mappers (and similar
 * pieces of software) to act upon the data for operations such as loading the
 * data from, or storing it to, the appropriate database table.  If additional
 * data (beyond the name) for a given activity is to be stored, then this class
 * should be modified to hold and represent the additional information.  When
 * modifying this class, take care to reflect the changes in the relevant
 * schemas and database mappings.  Also, make sure that the class generation
 * program, will not replace the modified class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.Activity
 */

public class $ActivityClass extends $ActivityBaseClass$ActivityBaseParameters
{
	/**
	 * Register the class with the <code>ActivityFactory</code> on initialization.
	 */

	static
	{
		ActivityFactory factory = ActivityFactory.getInstance ();

		factory.registerActivityData ("$ActivitySource", "$ActivityType", $ActivityClass.class);
	}

	/**
	 * Create the activity data class.
	 */

	protected $ActivityClass ()
	{
		super ();
	}

	/**
	 * Override the hashCode method to supply unique values for the  base and
	 * multiplier for this class.  All of the actual computation is performed by
	 * the superclass.  The values supplied by this method ensure that no two
	 * activity data classes will compute the same hash, even if they have
	 * identical data.
	 *
	 * @return A unique hash code for the object
	 */

	@Override
	public int hashCode ()
	{
		final int base = $HashBase;
		final int mult = $HashMult;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

