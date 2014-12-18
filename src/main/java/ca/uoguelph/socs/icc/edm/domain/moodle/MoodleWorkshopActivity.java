package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleWorkshopActivity extends GenericGroupedActivity<MoodleWorkshopActivitySubmission>
{
	protected MoodleWorkshopActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2063;
		final int mult = 593;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

