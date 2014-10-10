package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.GenericGroupedActivityMember;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleWorkshopActivitySubmission extends GenericGroupedActivityMember<MoodleWorkshopActivity>
{
	protected MoodleWorkshopActivitySubmission ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2069;
		final int mult = 587;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

