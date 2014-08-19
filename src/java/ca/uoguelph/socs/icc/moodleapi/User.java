package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;
import java.util.HashSet;

public class User implements PersistentData
{
	private Long id;
	private Integer idnumber;
	private String username;
	private String firstname;
	private String lastname;
	private Set<Enrolment> enrolments;

	User(Long id, Integer idnumber, String username, String firstname, String lastname)
	{
	}

	public Long getId ()
	{
		return new Long (this.id);
	}

	public Integer getIDNumber()
	{
		return new Integer (this.idnumber);
	}

	public String getUsername()
	{
		return new String (this.username);
	}

	public String getFirstName()
	{
		return new String (this.firstname);
	}

	public String getLastname()
	{
		return new String (this.lastname);
	}

	public String getName()
	{
		return new String (this.firstname + " " + this.lastname);
	}

	public Set<Enrolment> getEnrolments()
	{
		return new HashSet (this.enrolments);
	}

	public String toString()
	{
		return null;
	}
}