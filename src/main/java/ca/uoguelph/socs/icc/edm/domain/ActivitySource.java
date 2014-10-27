package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface ActivitySource
{
	public abstract String getName ();
	public abstract Set<ActivityType> getTypes ();
}
