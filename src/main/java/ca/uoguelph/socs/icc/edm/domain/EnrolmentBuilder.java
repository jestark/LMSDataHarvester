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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>Enrolment</code> instances.  This class extends
 * <code>AbstractBuilder</code>, adding the functionality required to
 * create <code>Enrolment</code> instances.  The "finalgrade" and "usable"
 * fields may be modified in place.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Enrolment
 */

public final class EnrolmentBuilder implements Builder<Enrolment>
{
	/** The Logger */
	private final Logger log;

	/** Helper to substitute <code>Course</code> instances */
	private DataStoreProxy<Course> courseProxy;

	/** Helper to operate on <code>Enrolment</code> instances */
	private DataStoreProxy<Enrolment> enrolmentProxy;

	/** Helper to substitute <code>Activity</code> instances */
	private DataStoreProxy<Role> roleProxy;

	/** The loaded or previously built <code>Enrolment</code> instance */
	private Enrolment oldEnrolment;

	/** The <code>DataStore</code> ID number for the <code>Enrolment</code> */
	private Long id;

	/** The associated <code>Course</code> */
	private Course course;

	/** The associated <code>Role</code> */
	private Role role;

	/** The final grade */
	private Integer finalGrade;

	/** Indication if the data is usable for research */
	private Boolean usable;

	/**
	 * Create the <code>EnrolmentBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected EnrolmentBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.courseProxy = DataStoreProxy.getInstance (Course.class, Course.SELECTOR_OFFERING, datastore);
		this.enrolmentProxy = DataStoreProxy.getInstance (Enrolment.class, Enrolment.SELECTOR_ID, datastore);
		this.roleProxy = DataStoreProxy.getInstance (Role.class, Role.SELECTOR_NAME, datastore);

		this.id = null;
		this.course = null;
		this.role = null;
		this.finalGrade = null;
		this.usable = null;
		this.oldEnrolment = null;
	}

	/**
	 * Create an instance of the <code>Enrolment</code>.
	 *
	 * @return                       The new <code>Enrolment</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Enrolment build ()
	{
		this.log.trace ("build:");

		if (this.course == null)
		{
			this.log.error ("Attempting to create an Enrolment without a Course");
			throw new IllegalStateException ("course is NULL");
		}

		if (this.role == null)
		{
			this.log.error ("Attempting to create an Enrolment without a Role");
			throw new IllegalStateException ("course is NULL");
		}

		if (this.usable == null)
		{
			this.log.error ("Attempting to create an Enrolment without setting the usability");
			throw new IllegalStateException ("usable is NULL");
		}

		if ((this.oldEnrolment == null)
				|| (! this.enrolmentProxy.contains (this.oldEnrolment))
				|| (this.oldEnrolment.getCourse () != this.course)
				|| (this.oldEnrolment.getRole () != this.role))
		{
			Enrolment result = this.enrolmentProxy.create ();
			result.setId (this.id);
			result.setCourse (this.course);
			result.setRole (this.role);
			result.setFinalGrade (this.finalGrade);
			result.setUsable (this.usable);

			this.oldEnrolment = this.enrolmentProxy.insert (this.oldEnrolment, result);
		}
		else
		{
			this.oldEnrolment.setFinalGrade (this.finalGrade);
			this.oldEnrolment.setUsable (this.usable);
		}

		return this.oldEnrolment;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>EnrolmentBuilder</code>
	 */

	public EnrolmentBuilder clear ()
	{
		this.log.trace ("clear:");

		this.id = null;
		this.course = null;
		this.role = null;
		this.finalGrade = null;
		this.usable = null;
		this.oldEnrolment = null;

		return this;
	}

	/**
	 * Load a <code>Enrolment</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Enrolment</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Enrolment</code> instance to be
	 *                                  loaded are not valid
	 */

	public EnrolmentBuilder load (final Enrolment enrolment)
	{
		this.log.trace ("load: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Attempting to load a NULL Enrolment");
			throw new NullPointerException ();
		}

		this.id = enrolment.getId ();
		this.setCourse (enrolment.getCourse ());
		this.setFinalGrade (enrolment.getFinalGrade ());
		this.setRole (enrolment.getRole ());
		this.setUsable (enrolment.isUsable ());
		this.oldEnrolment = enrolment;

		return this;
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	public Course getCourse ()
	{
		return this.course;
	}

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Course</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public EnrolmentBuilder setCourse (final Course course)
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
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	public Role getRole ()
	{
		return this.role;
	}

	/**
	 * Set the <code>Role</code> of the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  role                      The <code>Role</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Role</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public EnrolmentBuilder setRole (final Role role)
	{
		this.log.trace ("setRole: role={}", role);

		if (role == null)
		{
			this.log.error ("Role is NULL");
			throw new NullPointerException ("Role is NULL");
		}

		this.role = this.roleProxy.fetch (role);

		if (this.role == null)
		{
			this.log.error ("This specified Role does not exist in the DataStore");
			throw new IllegalArgumentException ("Role is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the final grade for the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.  The
	 * final grade will be null if no final grade was assigned for the
	 * <code>User</code> associated with this <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the final grade, or null if
	 *         there is no final grade
	 */

	public Integer getFinalGrade ()
	{
		return this.finalGrade;
	}

	/**
	 * Set the final grade for the <code>User</code> in the
	 * <code>Course</code>.
	 *
	 * @param  finalgrade The final grade for the <code>User</code> in the
	 *                    course, on the interval [0, 100]
	 *
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

	public EnrolmentBuilder setFinalGrade (final Integer finalgrade)
	{
		this.log.trace ("setFinalGrade: finalgrade={}", finalgrade);

		if ((finalgrade != null) && ((finalgrade < 0) || (finalgrade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.finalGrade = finalgrade;

		return this;
	}

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	public Boolean isUsable ()
	{
		return this.usable;
	}

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	public EnrolmentBuilder setUsable (final Boolean usable)
	{
		this.log.trace ("setUsable: usable={}", usable);

		if (usable == null)
		{
			this.log.error ("usable is NULL");
			throw new NullPointerException ("usable is NULL");
		}

		this.usable = usable;

		return this;
	}
}
