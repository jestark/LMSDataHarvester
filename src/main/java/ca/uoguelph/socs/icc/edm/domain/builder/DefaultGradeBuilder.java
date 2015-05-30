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

public final class DefaultGradeBuilder extends AbstractBuilder<Grade> implements GradeBuilder
{
	/**
	 * Implementation of the <code>BuilderFactory</code> to create a
	 * <code>DefaultGradeBuilder</code>.
	 */

	private static class Factory implements BuilderFactory<GradeBuilder, Grade>
	{
		/**
		 * Create the <code>GradeBuilder</code> for the specified
		 * <code>DataStore</code>.
		 *
		 * @param  datastore The <code>DataStore</code> into which new
		 *                   <code>Grade</code> will be inserted
		 *
		 * @return           The <code>GradeBuilder</code>
		 */

		@Override
		public GradeBuilder create (final DataStore datastore)
		{
			return new DefaultGradeBuilder (datastore);
		}
	}

	/** The activity associated with the grade */
	private Activity activity;

	/** The enrolment associated with the grade */
	private Enrolment enrolment;

	/** The grade */
	private Integer grade;

	/**
	 * static initializer to register the <code>DefaultGradeBuilder</code> with
	 * the factory
	 */

	static
	{
		AbstractBuilder.registerBuilder (GradeBuilder.class, DefaultGradeBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>DefaultGradeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code> into which the newly
	 *                   created <code>Grade</code> instance will be inserted
	 */

	protected DefaultGradeBuilder (final DataStore datastore)
	{
		super (Grade.class, datastore);

		this.clear ();
	}

	@Override
	protected Grade buildElement ()
	{
		if (this.activity == null)
		{
			this.log.error ("Can not build: The activity is not set");
			throw new IllegalStateException ("activity not set");
		}

		if (this.enrolment == null)
		{
			this.log.error ("Can not build: The enrolment is not set");
			throw new IllegalStateException ("enrolment not set");
		}

		if (this.grade == null)
		{
			this.log.error ("Can not build: The grade is not set");
			throw new IllegalStateException ("grade not set");
		}

		return null; //this.factory.create (this.enrolment, this.activity, this.grade);
	}

	/**
	 * Reset the <code>ElementBuilder</code>.  This method will set all of the
	 * fields for the <code>Element</code> to be built to <code>null</code>.
	 */

	@Override
	public void clear ()
	{
		this.log.trace ("Reseting the builder");

		super.clear ();
		this.activity = null;
		this.enrolment = null;
		this.grade = null;
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
		this.log.trace ("Load Grade: {}", grade);

		super.load (grade);
		this.setActivity (grade.getActivity ());
		this.setEnrolment (grade.getEnrolment ());
		this.setGrade (grade.getGrade ());
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>, may be null
	 */

	@Override
	public Activity getActivity ()
	{
		return this.activity;
	}

	@Override
	public GradeBuilder setActivity (final Activity activity)
	{
		if (grade == null)
		{
			this.log.error ("The specified activity is NULL");
			throw new NullPointerException ("The specified activity is NULL");
		}

		this.activity = activity;

		return this;
	}

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>, may be null
	 */

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	@Override
	public GradeBuilder setEnrolment (final Enrolment enrolment)
	{
		if (grade == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ("The specified Enrolment is NULL");
		}

		this.enrolment = enrolment;

		return this;
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
		return this.grade;
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
	public GradeBuilder setGrade (final Integer grade)
	{
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

		this.grade = grade;

		return this;
	}
}
