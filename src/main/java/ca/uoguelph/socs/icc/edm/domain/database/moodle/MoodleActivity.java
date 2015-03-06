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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import org.apache.commons.lang3.builder.EqualsBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.builder.GenericActivityElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultGenericActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractElement;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance;

/**
 * Moodle specific implementation of the core <code>Activity</code> data.
 * Rather than have the primary key of the tables containing the module
 * (<code>Activity</code>) data reference the core <code>Activity</code> table
 * (<code>ActivityInstance</code>), the Moodle database has a separate primary
 * key for each module table and stores a reference to that key in the course
 * modules table (core <code>Activity</code> data).  Such a design does not
 * allow the module (<code>Activity</code>) data to be loaded via a join to the
 * core <code>Activity</code> instance data (<code>ActivityInstance</code>).
 * To workaround the limitation imposed by the moodle database, this class
 * extends <code>ActivityInstance</code> to store the reference to the module
 * data so that the <code>MoodleActivityManager</code> can load the
 * <code>Activity</code> data in a second step.
 * <p>
 * Since the Moodle database is intended to be read-only, this class does not
 * implement an <code>ElementFactory</code> or require a builder capable of
 * setting the <code>instanceid</code> as should never be needed.
 *
 * @author  James E. Stark
 * @version 1.1
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.core.ActivityInstance
 * @see     MoodleActivityManager
 */

public class MoodleActivity extends ActivityInstance
{
	/**
	 * Implementation of the <code>ActivityElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>MoodleActivityInstance</code>.
	 */

	private static final class Factory extends ActivityInstance.Factory implements GenericActivityElementFactory
	{
		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  type    The <code>ActivityType</code> of the
		 *                 <code>Activity</code>, not null
		 * @param  course  The <code>Course</code> which is associated with the
		 *                 <code>Activity</code> instance, not null
		 *
		 * @return         The new <code>Activity</code> instance
		 */

		@Override
		public Activity create (final ActivityType type, final Course course)
		{
			assert type != null : "type is NULL";
			assert course != null : "course is NULL";

			return new MoodleActivity (type, course);
		}

	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The instance data identifier */
	private Long instanceid;

	/**
	 * Static initializer to register the <code>MoodleActivityInstance</code>
	 * class with the factories.
	 */

	static
	{
		AbstractElement.registerElement (Activity.class, MoodleActivity.class, DefaultGenericActivityBuilder.class, GenericActivityElementFactory.class, new Factory ());
	}

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public MoodleActivity ()
	{
		super ();
		this.instanceid = null;
	}

	/**
	 * Create a new <code>Activity</code> instance.  This constructor is provided
	 * for compatibility with the <code>ActivityInstance</code> superclass.  It
	 * should never actually be called, since the moodle database is read-only.
	 * As such, there is no facility to initialize the <code>instanceid</code>.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 */

	public MoodleActivity (final ActivityType type, final Course course)
	{
		super (type, course);
		this.instanceid = null;
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof MoodleActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.instanceid, ((MoodleActivity) obj).instanceid);
			
			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Get the name of the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances have names.  For those <code>Activity</code> instances which do
	 * not have names, the name of the associated <code>ActivityType</code> will
	 * be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return (this.getType ()).getName ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.
	 *
	 * @return A <code>Long</code> containing the identifier
	 */

	public Long getInstanceId ()
	{
		return this.instanceid;
	}

	/**
	 * Set the <code>DataStore</code> identifier for the <code>Element</code>
	 * containing the instance specific data for the <code>Activity</code>.  This
	 * method is intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  instanceid The identifier
	 */

	protected void setInstanceId (final Long instanceid)
	{
		assert instanceid != null : "instanceid is NULL";

		this.instanceid = instanceid;
	}
}
