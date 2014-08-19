package ca.uoguelph.socs.icc.moodleapi;

import java.util.Calendar;
import java.util.Date;

public enum Semester
{
	WINTER (Calendar.JANUARY, Calendar.APRIL, "Winter"),
	SPRING (Calendar.MAY, Calendar.AUGUST, "Spring"),
	FALL   (Calendar.SEPTEMBER, Calendar.DECEMBER, "Fall");

	private final int start;
	private final int end;
	private final String name;

	public static Semester getSemesterByDate(Date date)
	{
		Calendar cal = Calendar.getInstance ();
		cal.setTime (date);

		int i = 0;
		Semester[] semesters = Semester.values ();

		while (cal.get(Calendar.MONTH) > semesters[i].end)
		{
			i ++;
		}

		return semesters[i];
	}

	private Semester(int start, int end, String name)
	{
		this.start = start;
		this.end = end;
		this.name = name;
	}

	public String getName()
	{
		return new String (this.name);
	}

	public String toString ()
	{
		return new String (this.name);
	}
}