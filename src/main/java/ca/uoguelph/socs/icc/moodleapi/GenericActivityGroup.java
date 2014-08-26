package ca.uoguelph.socs.icc.moodleapi;

import java.util.Set;

public interface GenericActivityGroup<E extends GenericActivityGroupMember> extends Activity 
{
	public Set<E> getChildren();
	public void addChild(E child);
}