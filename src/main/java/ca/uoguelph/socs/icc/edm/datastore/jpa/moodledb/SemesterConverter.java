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
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ca.uoguelph.socs.icc.edm.domain.Semester;

/**
 * Convert the start date representation of a course between seconds since 
 * epoch and a semester.   Moodle represents the time of offering of courses
 * as a start date and an end date, both of which are expressed in seconds
 * since epoch (Jan 1 1970, 00:00:00 UTC).  Meanwhile, the course database 
 * represents the time of offering of a course as a semester and a year.  This
 * class, along with the year converter, converts the start date of the
 * course from the moodle database to the semester and year of offering.
 *
 * Since the conversion from a date to a semester is lossy (as the year of
 * offering is thrown away), a complete conversion from a year to a date is
 * not possible.  The moodle database is specified to be read-only so the
 * loss of data shouldn't be a problem.  JPA requires conversionmethods to be
 * implemented in both directions so the conversion of a semester to a date 
 * will always be the given semester in 1970
 *
 * @see ca.uoguelph.socs.icc.edm.datastore.moodledb.DateYearConverter
 * @author James E. Stark
 * @version 1.0
 */

@Converter
public class SemesterConverter implements AttributeConverter<Semester, Long>
{
	/**
	 * Convert the Semester in which the course was offered to a date expressed
	 * as seconds since epoch.
	 *
	 * @param semester The semester in which the course was offered.
	 * @return The semester of offering (in 1970) expressed in seconds.
	 */

	@Override
	public Long convertToDatabaseColumn(Semester semester)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.set (1970, semester.getStartMonth (), 1, 0, 0, 0);

		return new Long (calendar.getTimeInMillis () / 1000);
	}

	/**
	 * Convert the start time of the course from seconds since epoch to a 
	 * semester.
	 *
	 * @param seconds The start time of the course in seconds since epoch.
	 * @return The semester in which the course was offered.
	 */

	@Override
	public Semester convertToEntityAttribute(Long seconds)
	{
		return Semester.getSemesterByDate(new Date (seconds.intValue () * 1000));
	}
}
