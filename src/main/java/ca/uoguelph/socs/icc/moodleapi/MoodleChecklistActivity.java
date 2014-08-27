package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleChecklistActivity extends GenericNamedActivity
{
	protected MoodleChecklistActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3011;
		final int mult = 389;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
