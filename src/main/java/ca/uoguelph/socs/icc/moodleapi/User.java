package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User
{
	private long id;
	private Integer idnumber;
	private String username;
	private String firstname;
	private String lastname;
	private Set<Enrolment> enrolments;

	protected User ()
	{
		this.id = -1;
		this.idnumber = null;
		this.username = null;
		this.lastname = null;
		this.firstname= null;
	}

	public User (Integer idnumber, String username, String firstname, String lastname)
	{
		this ();
		this.idnumber = idnumber;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
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
				ebuilder.append (this.idnumber, ((User) obj).idnumber);
				ebuilder.append (this.username, ((User) obj).username);
				ebuilder.append (this.lastname, ((User) obj).lastname);
				ebuilder.append (this.firstname, ((User) obj).firstname);

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

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public Integer getIdNumber ()
	{
		return this.idnumber;
	}

	protected void setIdNumber (Integer idnumber)
	{
		this.idnumber = idnumber;
	}

	public String getUsername ()
	{
		return this.username;
	}

	protected void setUsername (String username)
	{
		this.username = username;
	}

	public String getFirstName()
	{
		return this.firstname;
	}

	protected void setFirstName (String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastName()
	{
		return this.lastname;
	}

	protected void setLastName (String lastname)
	{
		this.lastname = lastname;
	}

	public String getName()
	{
		return new String (this.firstname + " " + this.lastname);
	}

	public Set<Enrolment> getEnrolments()
	{
		return new HashSet<Enrolment>(this.enrolments);
	}

	protected void setEnrolments (Set<Enrolment> enrolments)
	{
		this.enrolments = enrolments;
	}

	public void addEnrolment (Enrolment enrolment)
	{
		this.enrolments.add (enrolment);
	}

	@Override
	public String toString()
	{
		return this.getName();
	}
}