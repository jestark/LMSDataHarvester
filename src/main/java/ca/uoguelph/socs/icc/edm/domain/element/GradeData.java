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

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;

/**
 * Implementation of the <code>Grade</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Grade</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Grade</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class GradeData extends Grade
{
	/**
	 * Representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (Element.Definition.class)
	public final class Definition extends Grade.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (GradeData.class, GradeData::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The grade */
	private Integer grade;

	/** The enrolment to which the grade is assigned */
	private Enrolment enrolment;

	/** The activity for which the grade is assigned */
	private Activity activity;

	/**
	 * Create the <code>Grade</code> with null values.
	 */

	protected GradeData ()
	{
		this.grade = null;
		this.activity = null;
		this.enrolment = null;
	}

	/**
	 * Create an <code>Grade</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	private GradeData (final Builder builder)
	{
		super (builder);

		this.enrolment = Preconditions.checkNotNull (builder.getEnrolment (), "enrolment");
		this.activity = Preconditions.checkNotNull (builder.getActivity (), "activity");
		this.grade = Preconditions.checkNotNull (builder.getGrade (), "grade");
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Grade</code>
	 * instance.  Since <code>GradedActivity</code> is dependent on the
	 * <code>Enrolment</code> instance for its <code>DataStore</code>
	 * identifier, the identifier from the associated <code>Enrolment</code>
	 * will be returned.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.enrolment.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.    This method is a no-op as
	 * the associated <code>Enrolment</code> provides the ID.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
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
		return this.propagateDomainModel (this.activity);
	}

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	@Override
	protected void setActivity (final Activity activity)
	{
		assert activity != null : "activity is NULL";

		this.activity = activity;
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
		return this.propagateDomainModel (this.enrolment);
	}

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used to initialize a
	 * new <code>Grade</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	@Override
	protected void setEnrolment (final Enrolment enrolment)
	{
		assert enrolment != null : "enrolment is NULL";

		this.enrolment = enrolment;
	}

	/**
	 * Get the grade that the student received for the <code>Activity</code>.
	 * The grade will be an <code>Integer</code> with a value on the range of
	 * [0, 100].
	 *
	 * @return An <code>Integer</code> containing the assigned grade.
	 */

	@Override
	public Integer getGrade()
	{
		return this.grade;
	}

	/**
	 * Set the numeric grade assigned to the <code>Enrolment</code> for the
	 * <code>Activity</code>.  This method is intended to be used to initialize
	 * a new <code>Grade</code> instance.
	 *
	 * @param  grade The grade, on the interval [0, 100], not null
	 */

	@Override
	protected void setGrade (final Integer grade)
	{
		assert grade != null : "grade is NULL";
		assert grade >= 0 : "grade can not be negative";

		this.grade = grade;
	}
}
