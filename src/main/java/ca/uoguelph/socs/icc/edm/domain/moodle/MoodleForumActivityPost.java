package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleForumActivityPost extends GenericGroupedActivityMember<MoodleForumActivityDiscussion>
{
	protected MoodleForumActivityPost ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2027;
		final int mult = 673;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

