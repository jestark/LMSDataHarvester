/* Copyright (C) 2014 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;
import java.util.Set;
import java.util.List;

import java.util.HashSet;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivityManager;

public class ActivityInstance extends AbstractActivity implements Serializable
{
	private static final class ActivityInstanceFactory implements ActivityElementFactory
	{
		@Override
		public Activity create (ActivityType type, Course course, Boolean stealth)
		{
			return new ActivityInstance (type, course, stealth);
		}

		@Override
		public void setId (Activity activity, Long id)
		{
			((ActivityInstance) activity).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key for the activity */
	private Long id;

	/** Flag indicting if the activity is a "system" activity */
	private Boolean stealth;
	
	/** The course with which the activity is associated */
	private Course course;

	/** The data associated with the activity */
	private Activity activity;

	/** The type of the activity*/
	private ActivityType type;

	/** The set of grades for the activity */
	private Set<Grade> grades;

	/** The log entries associated with the activity*/
	private List<LogEntry> log;

	static
	{
		(ActivityFactory.getInstance ()).registerElement (ActivityInstance.class, DefaultActivityManager.class, DefaultActivityBuilder.class, new ActivityInstanceFactory ());
	}

	public ActivityInstance ()
	{
		super ();
		this.id = null;
		this.log = null;
		this.type = null;
		this.course = null;
		this.activity = null;
		this.grades = null;

		this.stealth = new Boolean (false);
	}

	public ActivityInstance (ActivityType type, Course course, Boolean stealth)
	{
		this ();

		this.type = type;
		this.course = course;
		this.stealth = stealth;

		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivityInstance)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.type, ((ActivityInstance) obj).type);
			ebuilder.append (this.course, ((ActivityInstance) obj).course);
			ebuilder.append (this.activity, ((ActivityInstance) obj).activity);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1039;
		final int mult = 953;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.type);
		hbuilder.append (this.course);
		hbuilder.append (this.activity);

		return hbuilder.toHashCode ();
	}

	public Long getId ()
	{
		return this.id;
	}

	protected void setId (Long id)
	{
		this.id = id;
	}

	@Override
	public Course getCourse ()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	@Override
	public ActivityType getType ()
	{
		return this.type;
	}

	protected void setType (ActivityType type)
	{
		this.type = type;
	}

	public Activity getActivity ()
	{
		return this.activity;
	}

	protected void setActivity (Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public Boolean isStealth ()
	{
		return this.stealth;
	}

	public void setStealth (Boolean stealth)
	{
		this.stealth = stealth;
	}

	@Override
	public Set<Grade> getGrades ()
	{
		return new HashSet<Grade> (this.grades);
	}

	protected void setGrades (Set<Grade> grades)
	{
		this.grades = grades;
	}

	@Override
	protected boolean addGrade (Grade grade)
	{
		return this.grades.add (grade);
	}

	@Override
	public List<LogEntry> getLog ()
	{
		return new ArrayList<LogEntry> (this.log);
	}

	protected void setLog (List<LogEntry> log)
	{
		this.log = log;
	}

	@Override
	protected boolean addLog (LogEntry entry)
	{
		return false;
	}

	@Override
	public String getName ()
	{
		String name = this.type.getName ();

		if (this.activity != null)
		{
			name = this.activity.getName();
		}

		return name;
	}

	@Override
	public String toString ()
	{
		String string = this.getName ();

		if (this.stealth)
		{
			string = new String ("<" + string + ">");
		}

		return string;
	}
}
