/* Copyright (C) 2014 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Calendar;
import java.util.Date;

import com.google.common.base.MoreObjects;

/**
 * A representation of a semester.  The semesters are four months in length
 * and span the entire calendar year without and breaks.  Semesters begin
 * and end at the respective beginning and end of a calendar month.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public enum Semester
{
	/** The Winter semester running from January to April */
	WINTER (Calendar.JANUARY, Calendar.APRIL, "Winter"),

	/** The Spring semester running from May to August */
	SPRING (Calendar.MAY, Calendar.AUGUST, "Spring"),

	/** The Fall semester running from September to December */
	FALL   (Calendar.SEPTEMBER, Calendar.DECEMBER, "Fall");

	/** The starting month of the semester */
	private final int start;

	/** The ending month of the semester */
	private final int end;

	/** The name of the semester */
	private final String name;

	/**
	 * Find the <Code>Semester</code> that encapsulates the specified date.
	 *
	 * @param  date The <code>Date</code> for which the <code>Semester</code>
	 *              is to be found, not null
	 * @return      The <code>Semester</code> containing the specified date
	 */

	public static Semester getSemesterByDate (final Date date)
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

	/**
	 * Create the <code>Semester</code> enumeration.
	 *
	 * @param  start The first calendar month of the <code>Semester</code>
	 * @param  end   The last calendar month of the <code>Semester</code>
	 * @param  name  The name of the <code>Semester</code>, not null
	 */

	private Semester (final int start, final int end, final String name)
	{
		this.start = start;
		this.end = end;
		this.name = name;
	}

	/**
	 * Get the month in which the <code>Semester</code> starts.  The returned
	 * value will be an integer corresponding the month constants defined in
	 * <code>Calendar</code>.
	 *
	 * @return The integer representation of the starting month
	 * @see    java.util.Calendar
	 */

	public int getStartMonth ()
	{
		return this.start;
	}

	/**
	 * Get the month in which the <code>Semester</code> ends.  The returned
	 * value will be an integer corresponding the month constants defined in
	 * <code>Calendar</code>.
	 *
	 * @return The integer representation of the ending month
	 * @see    java.util.Calendar
	 */

	public int getEndMonth ()
	{
		return this.end;
	}

	/**
	 * Get the name of the <code>Semester</code> as a <code>String</code> which
	 * is suitable for display.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Semester</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Override the <code>toString</code> method from <code>Object</code> to
	 * display the name of the <code>Semester</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Semester</code>
	 */

	@Override
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("name", this.name)
			.toString ();
	}
}
