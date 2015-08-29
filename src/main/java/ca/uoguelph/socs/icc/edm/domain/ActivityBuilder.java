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

import ca.uoguelph.socs.icc.edm.domain.element.GenericActivity;

/**
 * Create <code>Activity</code> instances. This class creates instances for the
 * generic <code>Activity</code> implementations which only contain the
 * associated <code>ActivityType</code> and <code>Course</code>, and acts as
 * the common base for all of the builders which produce <code>Activity</code>
 * instances with additional parameters.
 * <p>
 * To create builders for <code>Activity</code> instances, the
 * <code>ActivityType</code> must be supplied when the builder is created.  The
 * <code>ActivityType</code> is needed to determine which <code>Activity</code>
 * implementation class is to be created by the builder.  It is possible to
 * specify an <code>ActivityType</code> which does not match the selected
 * builder.  In this case the builder will be created successfully, but an
 * exception will occur when a field is set in the builder that does not exist
 * in the implementation, or when the implementation is built and a required
 * field is determined to be missing.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Activity
 */

public class ActivityBuilder implements Builder<Activity>
{
	/** The Logger */
	protected final Logger log;

	/** Helper to operate on <code>SubActivity</code> instances*/
	protected final DataStoreProxy<Activity> activityProxy;

	/** Helper to substitute <code>Course</code> instances */
	protected final DataStoreProxy<Course> courseProxy;

	/** The associated <code>ActivityType</code> */
	protected final ActivityType type;

	/** The loaded or previously built <code>SubActivity</code> instance */
	protected Activity oldActivity;

	/** The <code>DataStore</code> id number for the <code>Activity</code> */
	protected Long id;

	/** The associated <code>Course</code> */
	protected Course course;

	/**
	 * Get an instance of the <code>ActivityBuilder</code> for the specified
	 * <code>DomainModel</code> and <code>ActivityType</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  type                  The <code>ActivityType</code>, not null
	 *
	 * @return                       The <code>ActivityBuilder</code>
	 *                               instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Activity</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static ActivityBuilder getInstance (final DomainModel model, ActivityType type)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		if (type == null)
		{
			throw new NullPointerException ("type is NULL");
		}

		return new ActivityBuilder (model.getDataStore (), type);
	}

	/**
	 * Create the <code>AbstractActivityBuilder</code>.
	 *
	 * @param  datastore                The <code>DataStore</code>, not null
	 * @param  metadata                 The meta-data <code>Creator</code>
	 *                                  instance, not null
	 *
	 * @throws IllegalArgumentException If the <code>ActivityType</code> does
	 *                                  not exist in the <code>DataStore</code>
	 */

	protected ActivityBuilder (final DataStore datastore, final ActivityType type)
	{
		assert datastore != null : "datastore is NULL";
		assert type != null : "type is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.courseProxy = DataStoreProxy.getInstance (Course.class, Course.SELECTOR_OFFERING, datastore);

		this.type = DataStoreProxy.getInstance (ActivityType.class, ActivityType.SELECTOR_NAME, datastore)
			.fetch (type);

		if (this.type == null)
		{
			this.log.error ("This specified ActivityType does not exist in the DataStore");
			throw new IllegalArgumentException ("ActivityType is not in the DataStore");
		}

		this.activityProxy = DataStoreProxy.getInstance (Activity.class, Activity.getActivityClass (this.type), Activity.SELECTOR_ID, datastore);

		this.id = null;
		this.course = null;
		this.oldActivity = null;
	}

	/**
	 * Create an instance of the <code>Activity</code>.
	 *
	 * @return                       The new <code>Activity</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Activity build ()
	{
		this.log.trace ("build:");

		if (this.course == null)
		{
			this.log.error ("course is NULL");
			throw new IllegalStateException ("course is NULL");
		}

		Activity result = this.activityProxy.create ();
		result.setId (this.id);
		result.setType (this.type);
		result.setCourse (this.course);

		this.oldActivity = this.activityProxy.insert (this.oldActivity, result);

		return this.oldActivity;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Activity</code> to be built to <code>null</code>.
	 *
	 * @return This <code>ActionBuilder</code>
	 */

	public ActivityBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.course = null;
		this.oldActivity = null;

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

	public ActivityBuilder load (final Activity activity)
	{
		this.log.trace ("load: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("Attempting to load a NULL Activity");
			throw new NullPointerException ();
		}

		if (! (this.getActivityType ()).equals (activity.getType ()))
		{
			this.log.error ("Invalid ActivityType:  required {}, received {}", this.getActivityType (), activity.getType ());
			throw new IllegalArgumentException ("Invalid ActivityType");
		}

		this.id = activity.getId ();
		this.setCourse (activity.getCourse ());
		this.oldActivity = activity;

		return this;
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public final ActivityType getActivityType ()
	{
		return this.type;
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

	public final ActivityBuilder setCourse (final Course course)
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
}
