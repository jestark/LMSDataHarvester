package ca.uoguelph.socs.icc.moodleapi;

public class GenericActivitySubGroup<E extends GenericGroupedActivityMember> extends GenericActivityGroup<E>
{
	private GenericGroupedActivityGroup outer;

	protected GenericActivitySubGroup (GenericGroupedActivityGroup outer)
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