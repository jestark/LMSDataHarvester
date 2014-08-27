package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleAssignActivity extends GenericNamedActivity
{
	protected MoodleAssignActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3001;
		final int mult = 397;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
