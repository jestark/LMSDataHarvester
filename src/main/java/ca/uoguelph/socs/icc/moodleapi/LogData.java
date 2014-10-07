package ca.uoguelph.socs.icc.moodleapi;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LogData implements LogEntry, Serializable
{
	private Long id;
	private Enrolment enrolment;
	private Activity activity;
	private Action action;
	private Date time;
	private String ip;
	private LogReference reference;

	protected LogData ()
	{
		this.id = null;
		this.ip = null;
		this.action = null;
		this.activity = null;
		this.enrolment = null;
		this.reference = null;
		this.time = null;
	}

	protected LogData (Enrolment enrolment, Activity activity, Action action)
	{
		this ();
		this.action = action;
		this.activity = activity;
		this.enrolment = enrolment;
		this.time = new Date ();
	}

	@Override
	public Long getId ()
	{
		return this.id;
	}

	protected void setid (Long id)
	{
		this.id = id;
	}

	@Override
	public Enrolment getEnrolment ()
	{
		return this.enrolment;
	}

	protected void setEnrolment (Enrolment enrolment)
	{
		this.enrolment = enrolment;
	}

	protected Activity getActivitydb ()
	{
		return this.activity;
	}

	protected void setActivitydb (Activity activity)
	{
		this.activity = activity;
	}

	protected LogReference getReference ()
	{
		return this.reference;
	}

	protected void setReference (LogReference reference)
	{
		this.reference = reference;
	}

	@Override
	public Action getAction ()
	{
		return this.action;
	}

	protected void setAction (Action action)
	{
		this.action = action;
	}

	@Override
	public Date getTime ()
	{
		return (Date) this.time.clone ();
	}

	protected void setTime (Date time)
	{
		this.time = time;
	}

	public String getIPAddress ()
	{
		return this.ip;
	}

	protected void setIPAddress (String ip)
	{
		this.ip = ip;
	}

	@Override
	public Activity getActivity ()
	{
		Activity result = this.activity;

		if (this.reference != null)
		{
			result = this.reference.getActivity ();
		}

		return result;
	}

	@Override
	public Course getCourse ()
	{
		return this.activity.getCourse ();
	}

	@Override
	public String toString ()
	{
		String result = new String (this.enrolment + ": " + this.getActivity () + ", " + this.action + ", " + this.time);

		if (this.ip != null)
		{
			result = new String (result + ", " + this.ip);
		}

		return result;
	}
}
