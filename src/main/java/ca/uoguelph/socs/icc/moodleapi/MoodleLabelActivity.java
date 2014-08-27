package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleLabelActivity extends GenericNamedActivity
{
	protected MoodleLabelActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3041;
		final int mult = 367;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
