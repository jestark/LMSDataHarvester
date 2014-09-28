package ca.uoguelph.socs.icc.moodleapi;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GradedActivity implements Grade, Serializable
{
	private Integer grade;
	private Enrolment enrolment;
	private Activity activity;

	protected GradedActivity ()
	{
		this.grade = null;
		this.activity = null;
		this.enrolment = null;
	}

	public GradedActivity (Enrolment enrolment, Activity activity, Integer grade)
	{
		this.activity = activity;
		this.enrolment = enrolment;
		this.grade = grade;
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.append (this.activity, ((GradedActivity) obj).activity);
				ebuilder.append (this.enrolment, ((GradedActivity) obj).enrolment);
				ebuilder.append (this.grade, ((GradedActivity) obj).grade);

				result = ebuilder.isEquals ();
			}
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