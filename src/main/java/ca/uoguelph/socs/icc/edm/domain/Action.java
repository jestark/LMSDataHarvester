package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface Action
{
	public abstract Set<ActivityType> getTypes ();
	public abstract String getName ();
}

