package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserData implements User
{
	private Long id;
	private Integer idnumber;
	private String username;
	private String firstname;
	private String lastname;
	private Set<Enrolment> enrolments;

	protected UserData ()
	{
		this.id = null;
		this.idnumber = null;
		this.username = null;
		this.lastname = null;
		this.firstname= null;
		this.enrolments = null;
	}

	public UserData (Integer idnumber, String username, String firstname, String lastname)
	{
		this ();
		this.idnumber = idnumber;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.enrolments = new HashSet<Enrolment> ();
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
				ebuilder.append (this.idnumber, ((UserData) obj).idnumber);
				ebuilder.append (this.username, ((UserData) obj).username);
				ebuilder.append (this.lastname, ((UserData) obj).lastname);
				ebuilder.append (this.firstname, ((UserData) obj).firstname);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1063;
		final int mult = 929;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.idnumber);
		hbuilder.append (this.username);
		hbuilder.append (this.lastname);
		hbuilder.append (this.firstname);

		return hbuilder.toHashCode ();
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	@Override
	public Integer getIdNumber ()
	{
		return this.idnumber;
	}

	protected void setIdNumber (Integer idnumber)
	{
		this.idnumber = idnumber;
	}

	@Override
	public String getUsername ()
	{
		return this.username;
	}

	protected void setUsername (String username)
	{
		this.username = username;
	}

	@Override
	public String getFirstname()
	{
		return this.firstname;
	}

	protected void setFirstname (String firstname)
	{
		this.firstname = firstname;
	}

	@Override
	public String getLastname()
	{
		return this.lastname;
	}

	protected void setLastname (String lastname)
	{
		this.lastname = lastname;
	}

	@Override
	public String getName()
	{
		return new String (this.firstname + " " + this.lastname);
	}

	@Override
	public Enrolment getEnrolment (Course course)
	{
		Enrolment result = null;

		for (Enrolment i : this.enrolments)
		{
			if (course == i.getCourse ())
			{
				result = i;
				break;
			}
		}

		return result;
	}

	@Override
	public Set<Enrolment> getEnrolments()
	{
		return new HashSet<Enrolment> (this.enrolments);
	}

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	public Enrolment addEnrolment (Course course, Role role)
	{
		Enrolment enrolment = new EnrolledUser (this, course, role);

		if (!this.enrolments.add (enrolment))
		{
			enrolment = null;
		}

		return enrolment;
	}

	@Override
	public String toString()
	{
		return this.getName();
	}
}