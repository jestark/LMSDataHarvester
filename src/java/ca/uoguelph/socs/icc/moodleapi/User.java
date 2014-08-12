package ca.uoguelph.socs.icc.moodleapi;

import java.util.ArrayList;

public interface User
{
	public int getIDNumber();
	public String getUsername();
	public String getFirstName();
	public String getLastname();
	public String getFullName();
	public ArrayList getEnrolments();
	public String toString();
}