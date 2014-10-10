package ca.uoguelph.socs.icc.moodleapi;

import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SemesterConverter implements AttributeConverter<Semester, Long>
{
	@Override
	public Long convertToDatabaseColumn(Semester semester)
	{
		return new Long (-1);
	}

	@Override
	public Semester convertToEntityAttribute(Long seconds)
	{
		return Semester.getSemesterByDate(new Date (seconds.intValue () * 1000));
	}
}