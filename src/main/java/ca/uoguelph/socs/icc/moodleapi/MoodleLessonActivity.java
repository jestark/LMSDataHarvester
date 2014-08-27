package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleLessonActivity extends GenericGroupedActivity<MoodleLessonActivityPage>
{
	protected MoodleLessonActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2039;
		final int mult = 601;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
