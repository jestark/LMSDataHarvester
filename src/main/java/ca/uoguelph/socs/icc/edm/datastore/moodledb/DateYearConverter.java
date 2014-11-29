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

package ca.uoguelph.socs.icc.edm.datastore.moodledb;

import java.util.Calendar;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Convert the start date representation of a course between seconds since
 * epoch and a year.  Moodle represents the time of offering of courses as a
 * start date and an end date, both of which are expressed in seconds since
 * epoch (Jan 1 1970, 00:00:00 UTC).  Meanwhile, the course database
 * represents the time of offering of a course as a semester and a year.  This
 * class, along with the <code>SemesterConverter</code>, converts the start
 * date of the course from the Moodle database to the <code>Semester</code> and
 * year of offering.
 *
 * Since the conversion from a date to a year is lossy (as all of the
 * components of the date other than the year are thrown away), a complete
 * conversion from a year to a date is not possible.  The Moodle database
 * is specified to be read-only so the loss of data shouldn't be a problem.
 * JPA requires conversion methods to be implemented in both directions so the
 * conversion of a year to a date will always be January 1 of the year of
 * offering.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     SemesterConverter
 */

@Converter
public class DateYearConverter implements AttributeConverter<Integer, Long>
{
	/**
	 * Convert the year of offering into a date expressed as seconds since epoch.
	 *
	 * @param  year An integer representation of the year of offering, not null
	 * @return      The year of offering in seconds since epoch
	 */

	@Override
	public Long convertToDatabaseColumn(Integer year)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.set (year, Calendar.JANUARY, 1, 0, 0, 0);

		return new Long (calendar.getTimeInMillis () / 1000);
	}

	/**
	 * Convert the start time of the course from seconds since epoch to the year
	 * of offering.
	 *
	 * @param  seconds Date in seconds since epoch, not null
	 * @return         An integer containing the year of offering
	 */

	@Override
	public Integer convertToEntityAttribute(Long seconds)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (seconds * 1000);

		return new Integer (calendar.get (Calendar.YEAR));
	}
}
