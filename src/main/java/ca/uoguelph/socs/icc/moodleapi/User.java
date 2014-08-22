package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public class User
{
	private long id;
	private Integer idnumber;
	private String username;
	private String firstname;
	private String lastname;
//	private Set<Enrolment> enrolments;

	User ()
	{

	}

	User(long id, Integer idnumber, String username, String firstname, String lastname)
	{
		this.id = id;
		this.idnumber = idnumber;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
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

//	protected void setName (String name)
//	{
		// do nothing
//	}

//	public Set<Enrolment> getEnrolments()
//	{
//		return new HashSet<Enrolment>(this.enrolments);
//	}

	public String toString()
	{
		return this.getName();
	}
}