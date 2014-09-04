package ca.uoguelph.socs.icc.moodleapi;

public class GenericActivityBaseGroup<E extends GenericGroupedActivityMember> extends GenericActivityGroup<E>
{
	private GenericGroupedActivity outer;

	protected GenericActivityBaseGroup (GenericGroupedActivity outer)
	{
		super ();
		this.outer = outer;
	}

	@Override
	protected AbstractNamedActivity getOuter ()
	{
		return this.outer;
	}
}