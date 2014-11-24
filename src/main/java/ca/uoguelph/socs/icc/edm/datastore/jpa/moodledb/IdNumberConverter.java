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

package ca.uoguelph.socs.icc.edm.moodledb;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Convert ID numbers between string and integer representations.  Moodle
 * stores student ID numbers as strings, and the domain model uses them as
 * integers.  This class converts between the two representations.
 *
 * @author James E. Stark
 * @version 1.0
 */

@Converter
public class IdNumberConverter implements AttributeConverter<Integer, String>
{
	/**
	 * Convert the integer representation of the ID number to the string
	 * representation.
	 *
	 * @param  interger The integer representation of the id number.
	 * @return The String representation of the id number.
	 */

	@Override
	public String convertToDatabaseColumn(Integer integer)
	{
		return integer.toString ();
	}

	/**
	 * Convert the string representation of the ID number to the integer
	 * representation.
	 *
	 * @param string The String representation of the ID number.
	 * @return The integer representation of the ID number.
	 */

	@Override
	public Integer convertToEntityAttribute(String string)
	{
		Integer result = null;

		try
		{
			result = new Integer (string);
		}
		catch (NumberFormatException ex)
		{
			result = new Integer (-1);
		}

		return result;
	}
}
