package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodlePageActivity extends GenericNamedActivity
{
	protected MoodlePageActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3049;
		final int mult = 359;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
