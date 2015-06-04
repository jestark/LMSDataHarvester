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

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultGradeBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>Grade</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Grade</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Grade</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultGradeBuilder
 */

public class GradedActivity extends AbstractElement implements Grade, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The grade */
	private Integer grade;

	/** The enrolment to which the grade is assigned */
	private Enrolment enrolment;

	/** The activity for which the grade is assigned */
	private Activity activity;

	/**
	 * Static initializer to register the <code>GradedActivity</code> class
	 * with the factories.
	 */

	static
	{
		DefinitionBuilder<GradedActivity, Grade.Properties> builder = DefinitionBuilder.newInstance (Grade.class, GradedActivity.class, Grade.Properties.class);
		builder.setCreateMethod (GradedActivity::new);

		builder.addAttribute (Grade.Properties.ACTIVITY, Activity.class, true, false, GradedActivity::getActivity, GradedActivity::setActivity);
		builder.addAttribute (Grade.Properties.ENROLMENT, Enrolment.class, true, false, GradedActivity::getEnrolment, GradedActivity::setEnrolment);
		builder.addAttribute (Grade.Properties.GRADE, Integer.class, true, true, GradedActivity::getGrade, GradedActivity::setGrade);

		AbstractElement.registerElement (builder.build (), DefaultGradeBuilder.class);
	}

	/**
	 * Create the <code>Grade</code> with null values.
	 */

	public GradedActivity ()
	{
		this.grade = null;
		this.activity = null;
		this.enrolment = null;
	}

	/**
	 * Create a new <code>Grade</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to which the grade is
	 *                   assigned, not null
	 * @param  activity  The <code>Activity</code> for which the grade is
	 *                   assigned, not null
	 * @param  grade     The assigned grade, on the interval [0, 100], not null
	 */

	public GradedActivity (final Enrolment enrolment, final Activity activity, final Integer grade)
	{
		assert enrolment != null : "enrolment is NULL";
		assert activity != null : "grade is NULL";
		assert grade != null : "grade is NULL";
		assert grade >= 0 : "grade can not be negative";

		this.grade = grade;
		this.activity = activity;
		this.enrolment = enrolment;
	}

	/**
	 * Compare two <code>Grade</code> instances to determine if they are equal.
	 * The <code>Grade</code> instances are compared based upon the associated
	 * <code>Activity</code> and the associated <code>Enrolment</code>.
	 *
	 * @param  obj The <code>Grade</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Grade</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof Grade)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.activity, ((Grade) obj).getActivity ());
			ebuilder.append (this.enrolment, ((Grade) obj).getEnrolment ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Grade</code> instance.
	 * The hash code is computed based upon associated <code>Activity</code>
	 * and the associated <code>Enrolment</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1049;
		final int mult = 947;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.activity);
		hbuilder.append (this.enrolment);

		return hbuilder.toHashCode ();
	}

	/**
	 * Determine if two <code>Grade</code> instances are identical.  This
	 * method acts as a stricter form of the equals method.  The equals method
	 * only compares properties that are required to be unique (and therefore
	 * immutable) for the <code>Grade</code> instance, while this method
	 * compares all of the properties.
	 *
	 * @param  element The <code>Element</code> to compare to the current
	 *                 instance
	 *
	 * @return         <code>True</code> if the <code>Element</code> instances
	 *                 are logically identical, <code>False</code> otherwise
	 */

	@Override
	public boolean identicalTo (final Element element)
	{
		boolean result = false;

		if (element == this)
		{
			result = true;
		}
		else if (element instanceof Grade)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.activity, ((Grade) element).getActivity ());
			ebuilder.append (this.enrolment, ((Grade) element).getEnrolment ());
			ebuilder.append (this.grade, ((Grade) element).getGrade ());

			result = ebuilder.isEquals ();
		}

		return result;
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

	public Long getId ()
	{
		return this.enrolment.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.   Since
	 * <code>GradedActivity</code> is dependent on the <code>Enrolment</code>
	 * instance for its <code>DataStore</code> identifier, this method throws
	 * an <code>UnsupportedOperationException</code>.
	 *
	 * @param  id                            The <code>DataStore</code>
	 *                                       identifier, not null
	 * @throws UnsupportedOperationException unconditionally
	 */

	protected void setId (final Long id)
	{
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the <code>Activity</code> for which the <code>Grade</code> is
	 * assigned.
	 *
	 * @return The associated <code>Activity</code>
	 */

	@Override
	public Activity getActivity()
	{
		return this.activity;
	}

	/**
	 * Set the <code>Activity</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected void setActivity (final Activity activity)
	{
		assert activity != null : "grade is NULL";

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
		return this.enrolment;
	}

	/**
	 * Set the <code>Enrolment</code> which is associated with the
	 * <code>Grade</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	public void setEnrolment (final Enrolment enrolment)
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
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Grade</code> instance is loaded.
	 *
	 * @param  grade The grade, on the interval [0, 100], not null
	 */

	protected void setGrade (final Integer grade)
	{
		assert grade != null : "grade is NULL";
		assert grade >= 0 : "grade can not be negative";

		this.grade = grade;
	}

	/**
	 * Get the name of the <code>Enrolment</code> to which the
	 * <code>Grade</code> is assigned.  This is a convenience method which
	 * return the result from the <code>getName</code> method on the associated
	 * <code>Enrolment</code> instance.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Enrolment</code>
	 * @see    Enrolment#getName
	 */

	@Override
	public String getName ()
	{
		return this.enrolment.getName ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>Grade</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Grade</code>
	 *         instance
	 */

	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.enrolment);
		builder.append ("activity", this.activity);
		builder.append ("grade", this.grade);

		return builder.toString ();
	}
}