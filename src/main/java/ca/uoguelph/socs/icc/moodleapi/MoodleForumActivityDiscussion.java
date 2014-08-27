package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleForumActivityDiscussion extends GenericGroupedActivityGroup<MoodleForumActivity, MoodleForumActivityPost>
{
	protected MoodleForumActivityDiscussion ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2029;
		final int mult = 661;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
