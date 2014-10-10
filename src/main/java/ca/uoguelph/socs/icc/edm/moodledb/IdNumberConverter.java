package ca.uoguelph.socs.icc.edm.moodledb;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IdNumberConverter implements AttributeConverter<Integer, String>
{
	@Override
	public String convertToDatabaseColumn(Integer integer)
	{
		return integer.toString ();
	}

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
