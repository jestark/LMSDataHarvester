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

package ca.uoguelph.socs.icc.edm.domain.activity.moodle;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractNoIdElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.NamedActivityElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Resource
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>NamedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Resource
 * <li>ClassName      = Resource
 * <li>Builder        = DefaultNamedActivityBuilder
 * <li>HashBase       = 3067
 * <li>HashMult       = 349
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Resource extends GenericNamedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Resource</code>.
	 */

	private static final class Factory extends AbstractNoIdElementFactory<Activity> implements NamedActivityElementFactory
	{
		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  instance The <code>Activity</code> containing the instance data,
		 *                  not null
		 * @param  name     The name of the <code>Activity</code>, not null
		 *
		 * @return          The new <code>Activity</code> instance
		 */

		public Activity create (Activity instance, String name)
		{
			return new Resource (instance, name);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Resource</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Resource.class, DefaultNamedActivityBuilder.class, new Factory (), "moodle", "Resource");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Resource ()
	{
		super ();
	}

	/**
	 * Create the <code>Activity</code> instance.
	 *
	 * @param  instance The <code>Activity</code> containing the instance data,
	 *                  not null
	 * @param  name     The name of the <code>Activity</code>, not null
	 */

	public Resource (Activity instance, String name)
	{
		super (instance, name);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>Resource</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 3067;
		final int mult = 349;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

