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

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

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

	/** Helper to substitute <code>Course</code> instances */
	private final Retriever<Course> courseRetriever;

	/** Helper to substitute <code>ActivityType</code> instances */
	private final Retriever<ActivityType> typeRetriever;

	/** Helper to operate on <code>ActivityReference</code> instances*/
	private final Persister<ActivityReference> persister;

	/** Method reference to the constructor of the implementation class */
	private final Supplier<ActivityReference> supplier;

	/** The loaded or previously built <code>ActivityReference</code> */
	private ActivityReference reference;

	/** The <code>DataStore</code> id number for the <code>Activity</code> */
	private Long id;

	/** The associated <code>ActivityType</code> */
	private ActivityType type;

	/** The associated <code>Course</code> */
	private Course course;

	/**
	 * Create the <code>ActivityReferenceBuilder</code>.
	 *
	 * @param  supplier        Method reference to the constructor of the
	 *                         implementation class, not null
	 * @param  persister       The <code>Persister</code> used to store the
	 *                         <code>ActivityReference</code>, not null
	 * @param  typeRetriever   <code>Retriever</code> for
	 *                         <code>ActivityType</code> instances, not null
	 * @param  courseRetriever <code>Retriever</code> for <code>Course</code>
	 *                         instances, not null
	 */

	protected ActivityReferenceBuilder (final Supplier<ActivityReference> supplier, final Persister<ActivityReference> persister, final Retriever<ActivityType> typeRetriever, final Retriever<Course> courseRetriever)
	{
		assert supplier != null : "supplier is NULL";
		assert persister != null : "persister is NULL";
		assert typeRetriever != null : "typeRetriever is NULL";
		assert courseRetriever != null : "courseRetriever is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.typeRetriever = typeRetriever;
		this.courseRetriever = courseRetriever;
		this.persister = persister;
		this.supplier = supplier;

		this.id = null;
		this.course = null;
		this.type = null;
		this.reference = null;
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

		ActivityReference result = this.supplier.get ();
		result.setId (this.id);
		result.setType (this.type);
		result.setCourse (this.course);

		this.reference = this.persister.insert (this.reference, result);

		return this.reference;
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
		this.reference = null;

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
		this.reference = reference;

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

		this.course = this.courseRetriever.fetch (course);

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

		this.type = this.typeRetriever.fetch (type);

		if (this.type == null)
		{
			this.log.error ("This specified ActivityType does not exist in the DataStore");
			throw new IllegalArgumentException ("ActivityType is not in the DataStore");
		}

		return this;
	}
}
