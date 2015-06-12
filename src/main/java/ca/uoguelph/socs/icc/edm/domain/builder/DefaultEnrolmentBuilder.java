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

package ca.uoguelph.socs.icc.edm.domain.builder;

import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.EnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.Role;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>EnrolmentBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultEnrolmentBuilder extends AbstractBuilder<Enrolment> implements EnrolmentBuilder
{
	/**
	 * static initializer to register the <code>DefaultEnrolmentBuilder</code>
	 * with the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultEnrolmentBuilder.class, DefaultEnrolmentBuilder::new);
	}

	/**
	 * Create the <code>DefaultEnrolmentBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Enrolment</code> instance will be
	 *                   inserted
	 */

	protected DefaultEnrolmentBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
	}

	/**
	 * Load a <code>Enrolment</code> instance into the
	 * <code>EnrolmentBuilder</code>.  This method resets the
	 * <code>EnrolmentBuilder</code> and initializes all of its parameters from
	 * the specified <code>Enrolment</code> instance.  The parameters are
	 * validated as they are set.
	 *
	 * @param  enrolment                The <code>Enrolment</code> to load into
	 *                                  the <code>EnrolmentBuilder</code>, not
	 *                                  null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Enrolment</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Enrolment enrolment)
	{
		this.log.trace ("load: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("Attempting to load a NULL Enrolment");
			throw new NullPointerException ();
		}

		super.load (enrolment);
		this.setCourse (enrolment.getCourse ());
		this.setFinalGrade (enrolment.getFinalGrade ());
		this.setRole (enrolment.getRole ());

		this.setPropertyValue ("id", enrolment.getId ());
	}

	/**
	 * Get the <code>Course</code> in which the <code>User</code> represented
	 * by the <code>Enrolment</code> instance is enrolled.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.getPropertyValue (Course.class, "course");
	}

	/**
	 * Set the <code>Course</code> in which the <code>User</code> is enrolled.
	 *
	 * @param  course                   The <code>Course</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Course</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	@Override
	public void setCourse (final Course course)
	{
		this.log.trace ("setCourse: course={}", course);

		if (course == null)
		{
			this.log.error ("Course is NULL");
			throw new NullPointerException ("Course is NULL");
		}

		if (! this.datastore.contains (course))
		{
			this.log.error ("This specified Course does not exist in the DataStore");
			throw new IllegalArgumentException ("Course is not in the DataStore");
		}

		this.setPropertyValue ("course", course);
	}

	/**
	 * Get the <code>Role</code> of the <code>User</code> represented by this
	 * <code>Enrolment</code>, in the associated <code>Course</code>.
	 *
	 * @return The <code>Role</code> instance
	 */

	@Override
	public Role getRole ()
	{
		return this.getPropertyValue (Role.class, "role");
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

	@Override
	public void setRole (final Role role)
	{
		this.log.trace ("setRole: role={}", role);

		if (role == null)
		{
			this.log.error ("Role is NULL");
			throw new NullPointerException ("Role is NULL");
		}

		if (! this.datastore.contains (role))
		{
			this.log.error ("This specified Role does not exist in the DataStore");
			throw new IllegalArgumentException ("Role is not in the DataStore");
		}

		this.setPropertyValue ("role", role);
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

	@Override
	public Integer getFinalGrade ()
	{
		return this.getPropertyValue (Integer.class, "finalgrade");
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

	@Override
	public void setFinalGrade (final Integer finalgrade)
	{
		this.log.trace ("setFinalGrade: finalgrade={}", finalgrade);

		if ((finalgrade != null) && ((finalgrade < 0) || (finalgrade > 100)))
		{
			this.log.error ("Grade must be between 0 and 100");
			throw new IllegalArgumentException ("Grade must be between 0 and 100");
		}

		this.setPropertyValue ("finalgrade", finalgrade);
	}

	/**
	 * Determine if the <code>User</code> has given their consent for the data
	 * associated with this <code>Enrolment</code> to be used for research.
	 *
	 * @return <code>True</code> if the <code>User</code> has consented,
	 *         <code>False</code> otherwise.
	 */

	@Override
	public Boolean isUsable ()
	{
		return this.getPropertyValue (Boolean.class, "usable");
	}

	/**
	 * Set the usable flag for the data related to the <code>User</code> in the
	 * <code>Course</code>. This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Enrolment</code> instance is
	 * loaded.
	 *
	 * @param  usable Indication if the data may be used for research, not null
	 */

	@Override
	public void setUsable (final Boolean usable)
	{
		this.log.trace ("setUsable: usable={}", usable);

		if (usable == null)
		{
			this.log.error ("usable is NULL");
			throw new NullPointerException ("usable is NULL");
		}

		this.setPropertyValue ("usable", usable);
	}
}
