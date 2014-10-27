package ca.uoguelph.socs.icc.edm.domain;

public class UserEnrolmentData extends EnrolmentData
{
	private User user;

	protected UserEnrolmentData ()
	{
		super ();
		this.user = null;
	}

	protected UserEnrolmentData (User user, Course course, Role role)
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
}
