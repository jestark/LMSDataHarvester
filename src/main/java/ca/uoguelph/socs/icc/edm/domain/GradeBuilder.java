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
 * Create new <code>Grade</code> instances.  This class extends
 * <code>AddingBuilder</code>, adding the functionality required to
 * create <code>Grade</code> instances.  The "grade" field of existing grade
 * instances may be modified in place.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Grade
 */

public final class GradeBuilder implements Builder<Grade>
{
	/** The Logger */
	private final Logger log;

	/** The <code>DataStore</code> */
	private final DataStore datastore;

	/** Helper to substitute <code>Enrolment</code> instances */
	private final DataStoreProxy<Enrolment> enrolmentProxy;

	/** Helper to operate on <code>Grade</code> instances */
	private final DataStoreProxy<Grade> gradeProxy;

	/** The loaded or previously built <code>Grade</code> instance */
	private Grade oldGrade;

	/** The associated <code>Activity</code> */
	private Activity activity;

	/** The associated <code>Enrolment</code> */
	private Enrolment enrolment;

	/** The value of the <code>Grade</code> */
	private Integer grade;

	/**
	 * Create the <code>GradeBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected GradeBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;

		this.enrolmentProxy = DataStoreProxy.getInstance (Enrolment.class, Enrolment.SELECTOR_ID, datastore);
		this.gradeProxy = DataStoreProxy.getInstance (Grade.class, Grade.SELECTOR_PKEY, datastore);

		this.activity = null;
		this.enrolment = null;
		this.grade = null;
		this.oldGrade = null;
	}

	/**
	 * Create an instance of the <code>Grade</code>.
	 *
	 * @return                       The new <code>Grade</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public Grade build ()
	{
		this.log.trace ("build:");

		if (this.activity == null)
		{
			this.log.error ("Attempting to create an Grade without an Activity");
			throw new IllegalStateException ("activity is NULL");
		}

		if (this.enrolment == null)
		{
			this.log.error ("Attempting to create an Grade without an Enrolment");
			throw new IllegalStateException ("enrolment is NULL");
		}

		if (this.grade == null)
		{
			this.log.error ("Attempting to create an Grade without a Grade");
			throw new IllegalStateException ("grade is NULL");
		}

		if ((this.oldGrade == null)
				|| (this.oldGrade.getActivity () != this.activity)
				|| (this.oldGrade.getEnrolment () != this.enrolment))
		{
			Grade result = this.gradeProxy.create ();
			result.setActivity (this.activity);
			result.setEnrolment (this.enrolment);
			result.setGrade (this.grade);

			this.oldGrade = this.gradeProxy.insert (this.oldGrade, result);

			if (! this.oldGrade.getGrade ().equals (this.grade))
			{
				this.log.error ("Grade is already in the datastore with a value of: {} vs. the specified value: {}", this.oldGrade.getGrade (), this.grade);
				throw new IllegalStateException ("Grade already exists but with a different value");
			}
		}
		else
		{
			this.oldGrade.setGrade (this.grade);
		}

		return oldGrade;
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>Element</code> to be built to <code>null</code>.
	 *
	 * @return This <code>GradeBuilder</code>
	 */

	public GradeBuilder clear ()
	{
		this.log.trace ("clear:");

		this.activity = null;
		this.enrolment = null;
		this.grade = null;
		this.oldGrade = null;

		return this;
	}

	/**
	 * Load a <code>Grade</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>Grade</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  grade                    The <code>Grade</code>, not null
	 *
	 * @return                          This <code>GradeBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>Grade</code> instance to be
	 *                                  loaded are not valid
	 */

	public GradeBuilder load (final Grade grade)
	{
		this.log.trace ("load: grade={}", grade);

		if (grade == null)
		{
			this.log.error ("Attempting to load a NULL Grade");
			throw new NullPointerException ();
		}

		this.setActivity (grade.getActivity ());
		this.setEnrolment (grade.getEnrolment ());
		this.setGrade (grade.getGrade ());

		this.oldGrade = grade;

		return this;
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	public Activity getActivity ()
	{
		return this.activity;
	}

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  activity                 The <code>Activity</code>, not null
	 *
	 * @return                          This <code>GradeBuilder</code>
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 * @throws IllegalArgumentException if the <code>Activity</code> is not a
	 *                                  <code>NamedActivity</code>
	 */

	public GradeBuilder setActivity (final Activity activity)
	{
		this.log.trace ("setActivity: activity={}", activity);

		if (activity == null)
		{
			this.log.error ("The specified activity is NULL");
			throw new NullPointerException ("The specified activity is NULL");
		}

		this.activity = DataStoreProxy.getInstance (Activity.class,
				Activity.getActivityClass (activity.getType ()),
				Activity.SELECTOR_ID,
				this.datastore)
			.fetch (activity);

		if (this.activity == null)
		{
			this.log.error ("The specified Activity does not exist in the DataStore");
			throw new IllegalArgumentException ("Activity is not in the DataStore");
		}

		if (! (this.activity instanceof NamedActivity))
		{
			this.log.error ("Only NamedActivity instances can be assigned Grades");
			throw new IllegalArgumentException ("Not a NamedActivity");
		}

		return this;
	}

	/**
	 * Get the <code>Enrolment</code>, for the student, to which the
	 * <code>Grade</code> is assigned
	 *
	 * @return The associated <code>Enrolment</code>
	 */

	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.
	 *
	 * @param  enrolment                The <code>Enrolment</code>, not null
	 *
	 * @return                          This <code>GradeBuilder</code>
	 * @throws IllegalArgumentException if the <code>Activity</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public GradeBuilder setEnrolment (final Enrolment enrolment)
	{
		this.log.trace ("setEnrolment: enrolment={}", enrolment);

		if (enrolment == null)
		{
			this.log.error ("The specified Enrolment is NULL");
			throw new NullPointerException ("The specified Enrolment is NULL");
		}

		this.enrolment = this.enrolmentProxy.fetch (enrolment);

		if (this.enrolment == null)
		{
			this.log.error ("The specified Enrolment does not exist in the DataStore");
			throw new IllegalArgumentException ("Enrolment is not in the DataStore");
		}

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

	public GradeBuilder setGrade (final Integer grade)
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

		this.grade = grade;

		return this;
	}
}
