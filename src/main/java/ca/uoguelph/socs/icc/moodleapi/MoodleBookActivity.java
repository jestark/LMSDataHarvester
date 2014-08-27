package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleBookActivity extends GenericGroupedActivity<MoodleBookActivityChapter>
{
	protected MoodleBookActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2003;
		final int mult = 691;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
