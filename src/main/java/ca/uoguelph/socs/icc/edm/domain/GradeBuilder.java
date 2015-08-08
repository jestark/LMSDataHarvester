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

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Creator;

/**
 * Create new <code>Grade</code> instances.  This class extends
 * <code>AddingBuilder</code>, adding the functionality required to
 * create <code>Grade</code> instances.  The "grade" field of existing grade
 * instances may be modified in place.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Grade
 */

public final class GradeBuilder extends AbstractBuilder<Grade>
{
	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static GradeBuilder getInstance (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return AbstractBuilder.getInstance (datastore, Grade.class, GradeBuilder::new);
	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DataStore</code>, loaded with the data from the specified
	 * <code>Grade</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 * @param  grade                 The <code>Grade</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static GradeBuilder getInstance (final DataStore datastore, Grade grade)
	{
		assert datastore != null : "datastore is NULL";
		assert grade != null : "grade is NULL";

		GradeBuilder builder = GradeBuilder.getInstance (datastore);
		builder.load (grade);

		return builder;
	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */


	public static GradeBuilder getInstance (final DomainModel model)
	{
		return GradeBuilder.getInstance (AbstractBuilder.getDataStore (model));
	}

	/**
	 * Get an instance of the <code>GradeBuilder</code> for the specified
	 * <code>DomainModel</code>, loaded with the data from the specified
	 * <code>Grade</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 * @param  grade                 The <code>Grade</code>, not null
	 *
	 * @return                       The <code>GradeBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>Grade</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static GradeBuilder getInstance (final DomainModel model, Grade grade)
	{
		if (grade == null)
		{
			throw new NullPointerException ("grade is NULL");
		}

		GradeBuilder builder = GradeBuilder.getInstance (model);
		builder.load (grade);

		return builder;
	}

	/**
	 * Create the <code>GradeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 * @param  metadata  The meta-data <code>Creator</code> instance, not null
	 */

	protected GradeBuilder (final DataStore datastore, final Creator<Grade> metadata)
	{
		super (datastore, metadata);
	}

	/**
	 * Load a <code>Grade</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Grade</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  grade                    The <code>Grade</code>, not null
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

	public Activity getActivity ()
	{
		return this.builder.getPropertyValue (Grade.ACTIVITY);
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

	public void setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("The specified activity is NULL");
			throw new NullPointerException ("The specified activity is NULL");
		}

		this.builder.setProperty (Grade.ACTIVITY, activity);
	}

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public Enrolment getEnrolment ()
	{
		return this.builder.getPropertyValue (Grade.ENROLMENT);
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

	public void setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ("The specified Enrolment is NULL");
		}

		this.builder.setProperty (Grade.ENROLMENT, enrolment);
	}

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade, may be
	 *         null
	 */

	public Integer getGrade ()
	{
		return this.builder.getPropertyValue (Grade.GRADE);
	}

	/**
	 * Set the value of the <code>Grade</code>.
	 *
	 * @param  grade                    The value of the <code>Grade</code>,
	 *                                  not null
	 *
	 * @throws IllegalArgumentException If the value is less than zero or
	 *                                  greater than 100
	 */

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

		this.builder.setProperty (Grade.GRADE, grade);
	}
}
