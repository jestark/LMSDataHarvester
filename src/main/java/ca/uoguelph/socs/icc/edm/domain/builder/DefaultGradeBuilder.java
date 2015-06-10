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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.GradeBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Default implementation of the <code>GradeBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class DefaultGradeBuilder extends AbstractBuilder<Grade, Grade.Properties> implements GradeBuilder
{
	/**
	 * static initializer to register the <code>DefaultGradeBuilder</code> with
	 * the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (DefaultGradeBuilder.class, DefaultGradeBuilder::new);
	}

	/**
	 * Create the <code>DefaultGradeBuilder</code>.
	 *
	 * @param  impl      The implementation class of the <code>Element</code>
	 *                   to be built
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Grade</code> instance will be inserted
	 */

	protected DefaultGradeBuilder (final Class<?> impl, final DataStore datastore)
	{
		super (impl, datastore);
	}

	/**
	 * Load a <code>Grade</code> instance into the <code>GradeBuilder</code>.
	 * This method resets the <code>GradeBuilder</code> and initializes all of
	 * its parameters from the specified <code>Grade</code> instance.  The
	 * parameters are validated as they are set.
	 *
	 * @param  grade                    The <code>Grade</code> to load into the
	 *                                  <code>GradeBuilder</code>, not null
	 *
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Grade</code> instance to be
	 *                                  loaded are not valid
	 */

	@Override
	public void load (final Grade grade)
	{
		this.log.trace ("load: grade={}", grade);

		if (grade == null)
		{
			this.log.error ("Attempting to load a NULL Grade");
			throw new NullPointerException ();
		}

		super.load (grade);
		this.setActivity (grade.getActivity ());
		this.setEnrolment (grade.getEnrolment ());
		this.setGrade (grade.getGrade ());
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	@Override
	public Activity getActivity ()
	{
		return this.getPropertyValue (Activity.class, Grade.Properties.ACTIVITY);
	}

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	@Override
	public void setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("The specified activity is NULL");
			throw new NullPointerException ("The specified activity is NULL");
		}

		this.setPropertyValue (Grade.Properties.ACTIVITY, activity);
	}

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	@Override
	public Enrolment getEnrolment ()
	{
		return this.getPropertyValue (Enrolment.class, Grade.Properties.ENROLMENT);
	}

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	@Override
	public void setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ("The specified Enrolment is NULL");
		}

		this.setPropertyValue (Grade.Properties.ENROLMENT, enrolment);
	}

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade, may be
	 *         null
	 */

	@Override
	public Integer getGrade ()
	{
		return this.getPropertyValue (Integer.class, Grade.Properties.GRADE);
	}

	/**
	 * Set the value of the <code>Grade</code>.
	 *
	 * @param  grade                    The value of the <code>Grade</code>,
	 *                                  not null
	 *
	 * @return                          This <code>GradeBuilder</code>
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

	@Override
	public void setGrade (final Integer grade)
	{
		this.log.trace ("setGrade: grade={}", grade);

		if (grade == null)
		{
			this.log.error ("The specified grade is NULL");
			throw new NullPointerException ("The specified grade is NULL");
		}

		if (grade < 0)
		{
			this.log.error ("Grades can not be negative: {}", grade);
			throw new IllegalArgumentException ("Grade is negative");
		}

		if (grade > 100)
		{
			this.log.error ("Grades can not be greater than 100%: {}", grade);
			throw new IllegalArgumentException ("Grade is greater than 100%");
		}

		this.setPropertyValue (Grade.Properties.GRADE, grade);
	}
}
