package ca.uoguelph.socs.icc.edm.domain;

public class EnrolledUser extends AbstractEnrolment
{
	private User user;

	protected EnrolledUser ()
	{
		super ();
		this.user = null;
	}

	protected EnrolledUser (User user, Course course, Role role)
	{
		super (course, role);
		this.user = user;
	}

	public User getUser ()
	{
		return this.user;
	}

	protected void setUser (User user)
	{
		this.user = user;
	}

	@Override
	public String getName ()
	{
		return this.user.getName ();
	}

	public Grade addGrade (Activity activity, Integer grade)
	{
		Grade agrade = new GradedActivity (this, activity, grade);

		if (!this.grades.add (agrade))
		{
			agrade = null;
		}

		return agrade;
	}

	public void setFinalGrade (Integer finalgrade)
	{
		this.finalgrade = finalgrade;
	}

	public void setUsable (Boolean usable)
	{
		this.usable = usable;
	}

	public void setActive (Boolean active)
	{
		this.active = active;
	}

/*	public LogEntry addLog (Activity activity, Action action)
	{
		LogEntry entry = ((AbstractActivity) activity).addLog (this, action);

		this.log.add (entry);

		return entry;
	}*/

	@Override
	public String toString ()
	{
		return new String ((this.getCourse ()).toString () + ": " + this.user.toString ());
	}
}
