package ca.uoguelph.socs.icc.moodleapi;

class EnrolledCourse extends AbstractEnrolment
{
	protected EnrolledCourse ()
	{
		super ();
	}

	public String getName ()
	{
		String result = new String ("(unset)");
		Long id = this.getId ();

		if (id != null)
		{
			result = id.toString ();
		}

		return result;
	}

	protected void setUsable (Boolean usable)
	{
		this.usable =usable;
	}

	protected void setActive (Boolean active)
	{
		this.active = active;
	}

	protected void setFinalGrade (Integer finalgrade)
	{
		this.finalgrade = finalgrade;
	}

	public String toString ()
	{
		return new String ((this.getCourse ()).toString () + ": " + this.getName ());
	}
}