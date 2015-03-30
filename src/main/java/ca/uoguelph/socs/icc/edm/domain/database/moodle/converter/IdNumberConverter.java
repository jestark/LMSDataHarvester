/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.database.moodle.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Convert ID numbers between <code>String</code> and <code>Integer</code>
 * representations.  Moodle stores student ID numbers as strings, and the
 * domain model uses them as integers.  This class converts between the two
 * representations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

@Converter
public class IdNumberConverter implements AttributeConverter<Integer, String>
{
	/**
	 * Convert the integer representation of the ID number to the
	 * <code>String</code> representation.
	 *
	 * @param  integer The <code>Integer</code> representation of the id
	 *                 number, not null
	 * @return         The <code>String</code> representation of the id number
	 */

	@Override
	public String convertToDatabaseColumn (final Integer integer)
	{
		return integer.toString ();
	}

	/**
	 * Convert the string representation of the ID number to the
	 * <code>Integer</code> representation.
	 *
	 * @param  string The <code>String</code> representation of the ID number,
	 *                not null
	 * @return        The <code>Integer</code> representation of the ID number
	 */

	@Override
	public Integer convertToEntityAttribute (final String string)
	{
		return ((string != null) && (string.length () > 0)) ? Integer.valueOf (string) : 0;
	}
}
