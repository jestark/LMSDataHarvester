package ca.uoguelph.socs.icc.edm.moodledb;

import java.util.Date;

import ca.uoguelph.socs.icc.edm.domain.Course;

public class MoodleDBLogEntry
{
	private long id;
	private Date timestamp;
	private Integer user;
	private String ip;
	private Course course;
	private String module;
	private Integer activity;
	private String action;
	private String info;

	protected MoodleDBLogEntry ()
	{
		this.id = -1;
		this.timestamp = null;
		this.user = null;
		this.ip = null;
		this.course = null;
		this.module = null;
		this.activity = null;
		this.action = null;
		this.info = null;
	}

	public long getId ()
	{
		return this.id;
	}

	protected void setId (long id)
	{
		this.id = id;
	}

	public Date getTime ()
	{
		return this.timestamp;
	}

	protected void setTime (Date time)
	{
		this.timestamp = time;
	}

	public Integer getUser ()
	{
		return this.user;
	}

	protected void setUser (Integer user)
	{
		this.user = user;
	}

	public String getIp ()
	{
		return this.ip;
	}

	protected void setIp (String ip)
	{
		this.ip = ip;
	}

	public Course getCourse ()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	public String getModule ()
	{
		return this.module;
	}

	protected void setModule (String module)
	{
		this.module = module;
	}

	public Integer getActivity ()
	{
		return this.activity;
	}

	protected void setActivity (Integer activity)
	{
		this.activity = activity;
	}

	public String getAction ()
	{
		return this.action;
	}

	protected void setAction (String action)
	{
		this.action = action;
	}

	public String getInfo ()
	{
		return this.info;
	}

	protected void setInfo (String info)
	{
		this.info = info;
	}

	public String toString ()
	{
		return new String (id + ", " + timestamp + ", " + user + ", " + course + " (" + module + ", " + activity + ", " + action + ": " + info + "), " + ip);
	}
}
