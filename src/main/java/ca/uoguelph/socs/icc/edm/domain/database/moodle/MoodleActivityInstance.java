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

package ca.uoguelph.socs.icc.edm.domain.database.moodle;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;

public class MoodleActivityInstance extends AbstractActivity implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Course course;

	protected MoodleActivityInstance ()
	{
		this.id = null;
	}

	@Override
	public boolean equals (Object obj)
	{
	}

	@Override
	public int hashCode ()
	{
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
		return false;
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
