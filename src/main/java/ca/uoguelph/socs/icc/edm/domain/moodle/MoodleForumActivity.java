package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleForumActivity extends GenericGroupedActivity<MoodleForumActivityDiscussion>
{
	protected MoodleForumActivity ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2017;
		final int mult = 677;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

