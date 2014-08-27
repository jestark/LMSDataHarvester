package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleFeedbackActivity extends GenericNamedActivity
{
	protected MoodleFeedbackActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3023;
		final int mult = 379;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
