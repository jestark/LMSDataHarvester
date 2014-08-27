package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleQuizActivity extends GenericNamedActivity
{
	protected MoodleQuizActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3061;
		final int mult = 353;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
