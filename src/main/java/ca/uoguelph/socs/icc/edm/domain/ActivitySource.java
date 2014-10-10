package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface ActivitySource
{
	public String getName ();
	public Set<ActivityType> getTypes ();
}