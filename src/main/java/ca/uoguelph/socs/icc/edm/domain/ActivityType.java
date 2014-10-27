package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface ActivityType
{
	public abstract String getName ();
	public abstract ActivitySource getSource ();
	public abstract Set<Action> getActions ();
}
