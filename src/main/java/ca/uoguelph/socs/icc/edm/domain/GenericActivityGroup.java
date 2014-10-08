package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface GenericActivityGroup<E extends GenericGroupedActivityMember> extends Activity
{
	public Set<E> getChildren();
	public void addChild(E child);
}
