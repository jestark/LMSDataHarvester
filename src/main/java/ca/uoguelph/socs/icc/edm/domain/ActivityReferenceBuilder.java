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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>ActivityReference</code> instances.  This is an internal
 * class used to create <code>ActivityReference</code> instances.  Generally,
 * it should only be used by the <code>ActivityBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ActivityReference
 */

final class ActivityReferenceBuilder implements Builder<ActivityReference>
{
	/** The Logger */
	private final Logger log;

	/** Helper to operate on <code>ActivityReference</code> instances*/
	private final DataStoreProxy<ActivityReference> referenceProxy;

	/** Helper to substitute <code>Course</code> instances */
	private final DataStoreProxy<Course> courseProxy;

	/** Helper to substitute <code>ActivityType</code> instances */
	private final DataStoreProxy<ActivityType> typeProxy;

	/** The loaded or previously built <code>SubActivity</code> instance */
	private ActivityReference oldReference;

	/** The <code>DataStore</code> id number for the <code>Activity</code> */
	private Long id;

	/** The associated <code>ActivityType</code> */
	private ActivityType type;

	/** The associated <code>Course</code> */
	private Course course;

	/**
	 * Create the <code>ActivityReferenceBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected ActivityReferenceBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.typeProxy = DataStoreProxy.getInstance (ActivityType.class, ActivityType.SELECTOR_NAME, datastore);
		this.courseProxy = DataStoreProxy.getInstance (Course.class, Course.SELECTOR_OFFERING, datastore);
		this.referenceProxy = DataStoreProxy.getInstance (ActivityReference.class, ActivityReference.SELECTOR_ID, datastore);

		this.id = null;
		this.course = null;
		this.type = null;
		this.oldReference = null;
	}

	/**
	 * Create an instance of the <code>Activity</code>.
	 *
	 * @return                       The new <code>Activity</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public ActivityReference build ()
	{
		this.log.trace ("build:");

		if (this.course == null)
		{
			this.log.error ("course is NULL");
			throw new IllegalStateException ("course is NULL");
		}

		ActivityReference result = referenceProxy.create ();
		result.setId (this.id);
		result.setType (this.type);
		result.setCourse (this.course);

		this.oldReference = this.referenceProxy.insert (this.oldReference, result);

		return this.oldReference;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Activity</code> to be built to <code>null</code>.
	 *
	 * @return This <code>ActionBuilder</code>
	 */

	public ActivityReferenceBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.course = null;
		this.type = null;
		this.oldReference = null;

		return this;
	}

	/**
	 * Load a <code>Activity</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Activity</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Activity</code> instance to be
	 *                                  loaded are not valid
	 */

	public ActivityReferenceBuilder load (final ActivityReference reference)
	{
		this.log.trace ("load: reference={}", reference);

		if (reference == null)
		{
			this.log.error ("Attempting to load a NULL Activityreference");
			throw new NullPointerException ();
		}

		this.id = reference.getId ();
		this.setType (reference.getType ());
		this.setCourse (reference.getCourse ());
		this.oldReference = reference;

		return this;
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public final Course getCourse ()
	{
		return this.course;
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException If the <code>Course</code> does not
	 *                                  exist in the <code>DataStore</code>
	 */

	public final ActivityReferenceBuilder setCourse (final Course course)
	{
		this.log.trace ("setCourse: course={}", course);

		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		this.course = this.courseProxy.fetch (course);

		if (this.course == null)
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public final ActivityType getType ()
	{
		return this.type;
	}

	/**
	 * Set the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @param  type                     The <code>ActivityType</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If the <code>ActivityType</code>
	 *                                  does not exist in the
	 *                                  <code>DataStore</code>
	 */

	public final ActivityReferenceBuilder setType (final ActivityType type)
	{
		this.log.trace ("setType: type={}", type);

		if (type == null)
		{
			this.log.error ("type is NULL");
			throw new NullPointerException ("type is NULL");
		}

		this.type = this.typeProxy.fetch (type);

		if (this.type == null)
		{
			this.log.error ("This specified ActivityType does not exist in the DataStore");
			throw new IllegalArgumentException ("ActivityType is not in the DataStore");
		}

		return this;
	}
}
