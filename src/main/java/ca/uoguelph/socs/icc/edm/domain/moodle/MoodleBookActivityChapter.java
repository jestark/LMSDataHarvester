package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleBookActivityChapter extends GenericGroupedActivityMember<MoodleBookActivity>
{
	protected MoodleBookActivityChapter ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2011;
		final int mult = 683;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

