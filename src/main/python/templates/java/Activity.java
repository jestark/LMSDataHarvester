package ca.uoguelph.socs.icc.edm.domain.$Package;

import ca.uoguelph.socs.icc.edm.domain.$ActivityBaseClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class $ActivityClass extends $ActivityBaseClass$ActivityBaseParameters
{
	protected $ActivityClass ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = $HashBase;
		final int mult = $HashMult;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

