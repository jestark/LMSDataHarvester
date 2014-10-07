package ca.uoguelph.socs.icc.moodleapi;

import java.io.Serializable;

public abstract class LogReference<T extends LoggedActivity> implements Serializable
{
	private LogEntry entry;
	private T activity;

	protected LogReference ()
	{
		this.entry = null;
		this.activity = null;
	}

	protected LogReference (LogEntry entry, T activity)
	{
		this.entry = entry;
		this.activity = activity;
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj != null)
		{
			if (obj == this)
			{
				result = true;
			}
			else if (obj.getClass () == this.getClass ())
			{
				result = this.entry.equals (obj);
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		return this.entry.hashCode ();
	}

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	protected void setEntry (LogEntry entry)
	{
		this.entry = entry;
	}

	public Activity getActivity ()
	{
		return this.activity;
	}

	protected void setActivity (T activity)
	{
		this.activity = activity;
	}
}