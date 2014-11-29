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

import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Convert Dates between <code>Date</code> and seconds since epoch.  Moodle
 * stores all of its timestamps as seconds since epoch, while the domain
 * model uses <code>Date</code>.  This class allows JPA to automatically
 * convert between the two representations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@Converter
public class IntDateConverter implements AttributeConverter<Date, Long>
{
	/**
	 * Convert the given <code>Date</code> object to an integer representation as
	 * seconds since epoch.
	 *
	 * @param  time The time-stamp as a <code>Date</code>, not null
	 * @return      The time-stamp as seconds since epoch
	 */

	@Override
	public Long convertToDatabaseColumn(Date time)
	{
		return new Long (time.getTime () / 1000);
	}

	/**
	 * Convert a time-stamp from seconds since epoch to a <code>Date</code>.
	 *
	 * @param  seconds Time-stamp expressed as seconds since Epoch, not null
	 * @return         The time-stamp as a <code>Date</code>
	 */

	@Override
	public Date convertToEntityAttribute(Long seconds)
	{
		return new Date (seconds * 1000);
	}
}
