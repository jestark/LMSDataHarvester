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
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivity;
import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Workshop
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>ActivityGroup</code> interface,
 * along with the relevant manager, and builder.  See the
 * <code>ActivityGroup</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Workshop
 * <li>ClassName      = Workshop
 * <li>ChildClass     = WorkshopSubmission
 * <li>Builder        = DefaultNamedActivityBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Workshop extends GenericGroupedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Workshop</code>.
	 */

	private static final class Factory extends GenericGroupedActivity.Factory
	{
		/**
		 * Create an instance of the <code>Factory</code>, passing the child
		 * <code>Class</code> to the super class.
		 */

		protected Factory ()
		{
			super (WorkshopSubmission.class);
		}

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

			return new Workshop (instance, name);
		}

	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Workshop</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Workshop.class, DefaultNamedActivityBuilder.class, new Factory (), "moodle", "Workshop");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Workshop ()
	{
		super ();
	}

	/**
	 * Create a new <code>Activity</code> instance.
	 *
	 * @param  instance The <code>Activity</code> containing the instance data,
	 *                  not null
	 * @param  name     The name of the <code>Activity</code>, not null
	 */

	public Workshop (final Activity instance, final String name)
	{
		super (instance, name);
	}
}
