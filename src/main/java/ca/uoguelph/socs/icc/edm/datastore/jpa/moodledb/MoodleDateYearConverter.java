package ca.uoguelph.socs.icc.edm.moodledb;

import java.util.Calendar;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MoodleDateYearConverter implements AttributeConverter<Integer, Long>
{
	@Override
	public Long convertToDatabaseColumn(Integer year)
	{
		return new Long (-1);
	}

	@Override
	public Integer convertToEntityAttribute(Long seconds)
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (seconds * 1000);

		return new Integer (calendar.get (Calendar.YEAR));
	}
}
