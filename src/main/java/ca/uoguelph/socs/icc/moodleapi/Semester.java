package ca.uoguelph.socs.icc.moodleapi;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
				ebuilder.append (this.name, ((User) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashcode ()
	{
		final int base = 1087;
		final int mult = 911;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

		return hbuilder.toHashCode ();
	}

	public String getName()
	{
		return new String (this.name);
	}

	@Override
	public String toString ()
	{
		return new String (this.name);
	}
}