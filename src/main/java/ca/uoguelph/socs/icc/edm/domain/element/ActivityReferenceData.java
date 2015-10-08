/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of <code>ActivityReference</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ActivityReferenceData extends ActivityReference implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the <code>Activity</code> */
	private Long id;

	/** The associated <code>Course</code> */
	private Course course;

	/** The type of the <code>Activity</code> */
	private ActivityType type;

	/** The <code>Activity</code> which contains the actual data */
	private Activity activity;

	/**
	 * Static initializer to register the <code>ActivityReferenceData</code>
	 * class with the factories.
	 */

	static
	{
		Implementation.getInstance (ActivityReference.class, ActivityReferenceData.class, ActivityReferenceData::new);
	}

	/**
	 * Create the <code>ActivityReferenceData</code> with null values.
	 */

	protected ActivityReferenceData ()
	{
		this.id = null;
		this.activity = null;
		this.course = null;
		this.type = null;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.  Some <code>Element</code> interfaces are dependent on other
	 * <code>Element</code> interfaces for their identification.  The dependent
	 * interface implementations should return the <code>DataStore</code>
	 * identifier from the interface on which they depend.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Element</code>
	 * instance is loaded, or by the <code>ElementBuilder</code> implementation
	 * to set the <code>DataStore</code> identifier, prior to storing a new
	 * <code>Element</code> instance.
	 * <p>
	 * <code>Element</code> implementations which are dependent on other
	 * <code>Element</code> interfaces for their <code>DataStore</code>
	 * identifier should throw an <code>UnsupportedOperationException</code>
	 * when this method is called.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.propagateDomainModel (this.course);
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	@Override
	protected void setCourse (final Course course)
	{
		assert course != null : "course is NULL";

		this.course = course;
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return this.propagateDomainModel (this.type);
	}

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	@Override
	protected void setType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		this.type = type;
	}

	/**
	 * Get the <code>Activity</code> that this <code>ActivityReference</code>
	 * is representing.
	 *
	 * @return The <code>Activity</code>
	 */

	@Override
	public Activity getActivity ()
	{
		return this.propagateDomainModel (this.activity);
	}

	/**
	 * Set the reference to the <code>Activity</code> which contains the actual
	 * data.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	@Override
	protected void setActivity (final Activity activity)
	{
		assert activity != null : "activity is NULL";

		this.activity = activity;
	}
}
