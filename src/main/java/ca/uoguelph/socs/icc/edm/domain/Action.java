package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface Action
{
	public Set<ActivityType> getTypes ();
	public String getName ();
}

