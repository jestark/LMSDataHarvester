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

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Checklist
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
 * <li>ActivityType   = Checklist
 * <li>ClassName      = Checklist
 * <li>Builder        = DefaultNamedActivityBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Checklist extends GenericNamedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Checklist</code>.
	 */

	private static final class Factory extends GenericNamedActivity.Factory
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

		public Activity create (final Activity instance, final String name)
		{
			assert instance != null : "instance is NULL";
			assert name != null : "name is NULL";

			return new Checklist (instance, name);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Checklist</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Checklist.class, DefaultNamedActivityBuilder.class, new Factory (), "moodle", "Checklist");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Checklist ()
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

	public Checklist (final Activity instance, final String name)
	{
		super (instance, name);
	}
}
