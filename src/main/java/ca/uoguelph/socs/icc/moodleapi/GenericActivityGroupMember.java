public interface GenericActivityGroupMember<T extends  GenericActivityGroup> extends Activity 
{
	public T getParent();
}
