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

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

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

	/** Helper to substitute <code>Activity</code> instances */
	private final Retriever<Activity> activityRetriever;

	/** Helper to substitute <code>Enrolment</code> instances */
	private final Retriever<Enrolment> enrolmentRetriever;

	/** Helper to operate on <code>Grade</code> instances */
	private final Persister<Grade> persister;

	/** Method reference to the constructor of the implementation class */
	private final Supplier<Grade> supplier;

	/** The loaded or previously built <code>Grade</code> instance */
	private Grade grade;

	/** The associated <code>Activity</code> */
	private Activity activity;

	/** The associated <code>Enrolment</code> */
	private Enrolment enrolment;

	/** The value of the <code>Grade</code> */
	private Integer value;

	/**
	 * Create the <code>GradeBuilder</code>.
	 *
	 * @param  supplier           Method reference to the constructor of the
	 *                            implementation class, not null
	 * @param  persister          The <code>Persister</code> used to store the
	 *                            <code>Enrolment</code>, not null
	 * @param  activityRetriever  <code>Retriever</code> for <code>Role</code>
	 *                            instances, not null
	 * @param  enrolmentRetriever <code>Retriever</code> for
	 *                            <code>Enrolment</code> instances, not null
	 */

	protected GradeBuilder (final Supplier<Grade> supplier, final Persister<Grade> persister, final Retriever<Activity> activityRetriever, final Retriever<Enrolment> enrolmentRetriever)
	{
		assert supplier != null : "supplier is NULL";
		assert persister != null : "persister is NULL";
		assert activityRetriever != null : "activityRetriever is NULL";
		assert enrolmentRetriever != null : "enrolmentRetriever is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.activityRetriever = activityRetriever;
		this.enrolmentRetriever = enrolmentRetriever;
		this.persister = persister;
		this.supplier = supplier;

		this.grade = null;
		this.activity = null;
		this.enrolment = null;
		this.value = null;
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

		if (this.value == null)
		{
			this.log.error ("Attempting to create an Grade without a Grade");
			throw new IllegalStateException ("grade is NULL");
		}

		if ((this.grade == null)
				|| (this.grade.getActivity () != this.activity)
				|| (this.grade.getEnrolment () != this.enrolment))
		{
			Grade result = this.supplier.get ();
			result.setActivityReference (this.activity.getReference ());
			result.setEnrolment (this.enrolment);
			result.setGrade (this.value);

			this.grade = this.persister.insert (this.grade, result);

			if (! this.grade.equalsAll (result))
			{
				this.log.error ("Grade is already in the datastore with a value of: {} vs. the specified value: {}", this.grade.getGrade (), this.value);
				throw new IllegalStateException ("Grade already exists but with a different value");
			}
		}
		else
		{
			this.grade.setGrade (this.value);
		}

		return this.grade;
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

		this.grade = null;
		this.activity = null;
		this.enrolment = null;
		this.value = null;

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

		this.grade = grade;
		this.setActivity (grade.getActivity ());
		this.setEnrolment (grade.getEnrolment ());
		this.setGrade (grade.getGrade ());

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

		this.activity = this.activityRetriever.fetch (activity);

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

		this.enrolment = this.enrolmentRetriever.fetch (enrolment);

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
		return this.value;
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

		this.value = grade;

		return this;
	}
}
