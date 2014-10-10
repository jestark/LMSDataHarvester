package ca.uoguelph.socs.icc.edm.moodledb;

import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MoodleIntDateConverter implements AttributeConverter<Date, Long>
{
	@Override
	public Long convertToDatabaseColumn(Date time)
	{
		return new Long (time.getTime () / 1000);
	}

	@Override
	public Date convertToEntityAttribute(Long seconds)
	{
		return new Date (seconds * 1000);
	}
}
