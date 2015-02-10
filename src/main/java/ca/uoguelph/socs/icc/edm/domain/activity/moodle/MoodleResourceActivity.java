package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleResourceActivity extends GenericNamedActivity
{
	private static final long serialVersionUID = 1L;

	protected MoodleResourceActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 3067;
		final int mult = 349;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

