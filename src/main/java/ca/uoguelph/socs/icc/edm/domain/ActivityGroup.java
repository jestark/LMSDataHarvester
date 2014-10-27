package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;

public interface ActivityGroup<E extends GenericGroupedActivityMember> extends Activity
{
	public abstract Set<E> getChildren();
	public abstract void addChild(E child);
}
