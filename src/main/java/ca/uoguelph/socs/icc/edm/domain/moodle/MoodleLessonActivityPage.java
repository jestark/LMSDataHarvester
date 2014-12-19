package ca.uoguelph.socs.icc.edm.domain.moodle;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MoodleLessonActivityPage extends GenericGroupedActivityMember<MoodleLessonActivity>
{
	private static final long serialVersionUID = 1L;

	protected MoodleLessonActivityPage ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = 2053;
		final int mult = 599;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

