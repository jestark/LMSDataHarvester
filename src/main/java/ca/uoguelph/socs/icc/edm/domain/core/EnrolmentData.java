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
import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Enrolment;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.Role;
import ca.uoguelph.socs.icc.edm.domain.User;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultEnrolmentBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.EnrolmentElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.EnrolmentFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultEnrolmentManager;

public class EnrolmentData implements Enrolment, Serializable
{
	private static final class EnrolmentDataFactory implements EnrolmentElementFactory
	{
		@Override
		public Enrolment create (User user, Course course, Role role, Integer grade, Boolean usable)
		{
			return new EnrolmentData (course, role, grade, usable);
		}

		@Override
		public void setId (Enrolment enrolment, Long id)
		{
			((EnrolmentData) enrolment).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Course course;
	private Role role;
	protected Integer finalgrade;
	protected Boolean usable;
	protected Set<Grade> grades;
	protected List<LogEntry> log;

	static
	{
		(EnrolmentFactory.getInstance ()).registerElement (EnrolmentData.class, DefaultEnrolmentManager.class, DefaultEnrolmentBuilder.class, new EnrolmentDataFactory ());
	}

	public EnrolmentData ()
	{
		this.id = null;
		this.log = null;
		this.role = null;
		this.course = null;
		this.usable = new Boolean (false);
		this.finalgrade = null;
		this.grades = null;
	}

	public EnrolmentData (Course course, Role role, Integer grade, Boolean usable)
	{
		this ();

		this.course = course;
		this.role = role;
		this.finalgrade = grade;
		this.usable = usable;

		this.grades = new HashSet<Grade> ();
		this.log = new ArrayList<LogEntry> ();
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
				EqualsBuilder ebuilder = new EqualsBuilder ();
				ebuilder.append (this.course, ((EnrolmentData) obj).course);
				ebuilder.append (this.role, ((EnrolmentData) obj).role);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1091;
		final int mult = 907;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.course);
		hbuilder.append (this.role);

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
	public Course getCourse()
	{
		return this.course;
	}

	protected void setCourse (Course course)
	{
		this.course = course;
	}

	@Override
	public Role getRole()
	{
		return this.role;
	}

	protected void setRole (Role role)
	{
		this.role = role;
	}

	@Override
	public String getName ()
	{
		String result = new String ("(unset)");
		Long id = this.getId ();

		if (id != null)
		{
			result = id.toString ();
		}

		return result;
	}

	@Override
	public Grade getGrade (Activity activity)
	{
		Grade result = null;

		for (Grade i : this.grades)
		{
			if (activity == i.getActivity ())
			{
				result = i;
				break;
			}
		}

		return result;
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

	protected boolean addGrade (Grade grade)
	{
		return this.grades.add (grade);
	}

	@Override
	public Integer getFinalGrade ()
	{
		return this.finalgrade;
	}

	protected void setFinalGrade (Integer finalgrade)
	{
		this.finalgrade = finalgrade;
	}

	@Override
	public Boolean isUsable ()
	{
		return this.usable;
	}

	protected void setUsable (Boolean usable)
	{
		this.usable =usable;
	}

	@Override
	public List<LogEntry> getLog ()
	{
		return this.log;
	}

	protected void setLog (List<LogEntry> log)
	{
		this.log = log;
	}

	protected boolean addLog (LogEntry entry)
	{
		return this.log.add (entry);
	}

	@Override
	public String toString ()
	{
		return new String ((this.getCourse ()).toString () + ": " + this.getName ());
	}
}
