package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface ActivityType
{
	public String getName ();
	public ActivitySource getSource ();
	public Set<Action> getActions ();
}
