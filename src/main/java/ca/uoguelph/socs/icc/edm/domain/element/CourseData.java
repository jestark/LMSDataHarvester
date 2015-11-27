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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Semester;
import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;

/**
 * Implementation of the <code>Course</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Course</code>
 * interface, along with the relevant manager and builder.  See the
 * <code>Course</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class CourseData extends Course
{
	/**
	 * <code>Builder</code> for <code>CourseData</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.Course.Builder
	 */

	public static final class Builder extends Course.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  persister The <code>Persister</code> used to store the
		 *                   <code>Role</code>, not null
		 */

		private Builder (final Persister<Course> persister)
		{
			super (persister);
		}

		/**
		 * Create an instance of the <code>Course</code>.
		 *
		 * @return The new <code>Course</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Course createElement ()
		{
			this.log.trace ("createElement");

			return new CourseData (this);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the course. */
	private @Nullable Long id;

	/** The name of the course. */
	private String name;

	/** The <code>Semester</code> in which the course was offered. */
	private Semester semester;

	/** The year in which the course was offered. */
	private Integer year;

	/** The <code>List</code> of <code>Activity</code> instances */
	private List<ActivityReference> activities;

	/** The <code>List</code> of individuals which are enrolled in the course. */
	private Set<Enrolment> enrolments;

	/**
	 * Create the <code>Course</code> with null values.
	 */

	protected CourseData ()
	{
		this.id = null;
		this.name = null;
		this.semester = null;
		this.year = null;

		this.activities = new ArrayList<ActivityReference> ();
		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Create an <code>Course</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected CourseData (final Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.name = Preconditions.checkNotNull (builder.getName (), "name");
		this.semester = Preconditions.checkNotNull (builder.getSemester (), "semester");
		this.year = Preconditions.checkNotNull (builder.getYear (), "year");

		this.activities = new ArrayList<ActivityReference> ();
		this.enrolments = new HashSet<Enrolment> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Course</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>Course</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Course</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Course</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Course</code>.  This method is intended to be
	 * used to initialize a new <code>Course</code> instance.
	 *
	 * @param  name The name of the <code>Course</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>Semester</code> in which the <code>Course</code> was
	 * offered.
	 *
	 * @return The <code>Semester</code> of offering
	 */

	@Override
	public Semester getSemester ()
	{
		return this.semester;
	}

	/**
	 * Set the <code>Semester</code> in which the <code>Course</code> was
	 * offered.  This method is intended to be used to initialize a new
	 * <code>Course</code> instance.
	 *
	 * @param  semester The <code>Semester</code> in which the
	 *                  <code>Course</code> was offered
	 */

	@Override
	protected void setSemester (final Semester semester)
	{
		assert semester != null : "semester is NULL";

		this.semester = semester;
	}

	/**
	 * Get the year in which the <code>Course</code> was offered.
	 *
	 * @return An <code>Integer</code> containing the year of offering
	 */

	@Override
	public Integer getYear ()
	{
		return this.year;
	}

	/**
	 * Set the year in which the <code>Course</code> was offered.  This method
	 * is intended to be used to initialize a new <code>Course</code> instance.
	 *
	 * @param  year The year in which the <code>Course</code> was offered
	 */

	@Override
	protected void setYear (final Integer year)
	{
		assert year != null : "year is NULL";

		this.year = year;
	}

	/**
	 * Get the <code>List</code> of <code>Activity</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if there are no <code>Activity</code> instances associated with
	 * the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Activity</code> instances
	 */

	@Override
	public List<Activity> getActivities ()
	{
		return Collections.unmodifiableList (this.activities.stream ()
				.map (x -> this.propagateDomainModel (x).getActivity ())
				.collect (Collectors.toList ()));
	}

	/**
	 * Get the <code>List</code> <code>ActivityReference</code> instances which
	 * are associated with the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>ActivityReference</code> instances
	 */

	@Override
	protected List<ActivityReference> getActivityReferences ()
	{
		this.activities.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.activities);
	}

	/**
	 * Initialize the <code>List</code> of <code>ActivityReferences</code>
	 * instances associated with the <code>Course</code> instance.  This method
	 * is intended to be used to initialize a new <code>Course</code> instance.
	 *
	 * @param  activities The <code>List</code> of <code>Activity</code>
	 *                    instances, not null
	 */

	@Override
	protected void setActivityReferences (final List<ActivityReference> activities)
	{
		assert activities != null : "activities is NULL";

		this.activities = activities;
	}

	/**
	 * Add the specified <code>ActivityReference</code> to the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>ActivityReference</code> to add, not null
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addActivityReference (final ActivityReference activity)
	{
		assert activity != null : "activity is NULL";

		return this.activities.add (activity);
	}

	/**
	 * Remove the specified <code>ActivityReference</code> from the
	 * <code>Course</code>.
	 *
	 * @param  activity The <code>ActivityReference</code> to remove,  not null
	 * @return          <code>True</code> if the <code>ActivityReference</code>
	 *                  was successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeActivityReference (final ActivityReference activity)
	{
		assert activity != null : "activity is NULL";

		return this.activities.remove (activity);
	}

	/**
	 * Get the <code>List</code> of <code>Enrolment</code> instances which are
	 * associated with the <code>Course</code>.  The <code>List</code> will be
	 * empty if no one is enrolled in the <code>Course</code>.
	 *
	 * @return A <code>List</code> of <code>Enrolment</code> instances
	 */

	@Override
	public Set<Enrolment> getEnrolments ()
	{
		this.enrolments.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableSet (this.enrolments);
	}

	/**
	 * Initialize the <code>List</code> of <code>Enrolment</code> instances
	 * associated with the <code>Course</code> instance.  This method is
	 * intended to be used to intialize a new <code>Course</code> instance.
	 *
	 * @param  enrolments The <code>List</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	@Override
	protected void setEnrolments (final Set<Enrolment> enrolments)
	{
		assert enrolments != null : "enrolments is NULL";

		this.enrolments = enrolments;
	}

	/**
	 * Add the specified <code>Enrolment</code> to the <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add, not null
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		return this.enrolments.add (enrolment);
	}

	/**
	 * Remove the specified <code>Enrolment</code> from the
	 * <code>Course</code>.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove, not null
	 * @return           <code>True</code> if the <code>Enrolment</code> was
	 *                   successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		return this.enrolments.remove (enrolment);
	}
}
