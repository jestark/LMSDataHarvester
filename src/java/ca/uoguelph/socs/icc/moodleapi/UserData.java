package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public class UserData implements User
{
	private int idnumber;
	private String username;
	private String firstname;
	private String lastname;
	private ArrayList  course;

	UserData(int idnumber, String username, String firstname, String lastname)
	{
	}

	public int getIDNumber()
	{
		return 0;
	}

	public String getUsername()
	{
		return null;
	}

	public String getFirstName()
	{
		return null;
	}

	public String getLastname()
	{
		return null;
	}

	public String getFullName()
	{
		return null;
	}

	public ArrayList getEnrollments()
	{
		return null;
	}

	public String toString()
	{
		return null;
	}
}