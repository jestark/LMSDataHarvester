package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleSchedulerActivity extends GenericNamedActivity
{
	protected MoodleSchedulerActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3079;
		final int mult = 347;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

