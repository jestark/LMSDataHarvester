/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultGradeBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.GradeElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;

public class GradedActivity implements Grade, Serializable
{
	private static final class GradedActivityFactory implements GradeElementFactory
	{
		@Override
		public Grade create (Enrolment enrolment, Activity activity, Integer mark)
		{
			return new GradedActivity (enrolment, activity, mark);
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

	static
	{
//		(EnrolmentFactory.getInstance ()).registerElement (GradedActivity.class, DefaultGradeBuilder.class, new GradedActivityFactory ());
	}

	public GradedActivity ()
	{
		this.grade = null;
		this.activity = null;
		this.enrolment = null;
	}

	public GradedActivity (Enrolment enrolment, Activity activity, Integer mark)
	{
		this ();

		this.grade = grade;
		this.activity = activity;
		this.enrolment = enrolment;
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof GradedActivity)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.activity, ((GradedActivity) obj).activity);
			ebuilder.append (this.enrolment, ((GradedActivity) obj).enrolment);
			ebuilder.append (this.grade, ((GradedActivity) obj).grade);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1049;
		final int mult = 947;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.activity);
		hbuilder.append (this.enrolment);
		hbuilder.append (this.grade);

		return hbuilder.toHashCode ();
	}

	@Override
	public Activity getActivity()
	{
		return this.activity;
	}

	protected void setActivity (Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	public void setEnrolment (Enrolment Enrolment)
	{
		this.enrolment = enrolment;
	}

	@Override
	public Integer getGrade()
	{
		return this.grade;
	}

	protected void setGrade (Integer grade)
	{
		this.grade = grade;
	}

	@Override
	public String getName ()
	{
		return this.enrolment.getName ();
	}

	@Override
	public String toString()
	{
		return new String (this.enrolment.toString () + ", " + this.activity.toString () + ": " +this.grade.toString ());
	}
}
