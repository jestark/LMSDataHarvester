package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleChoiceActivity extends GenericNamedActivity
{
	protected MoodleChoiceActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3019;
		final int mult = 383;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
