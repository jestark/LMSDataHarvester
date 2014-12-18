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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

public abstract class GenericNamedActivity extends AbstractNamedActivity implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ActivityInstance instance;

	protected GenericNamedActivity ()
	{
		super ();
		this.instance = null;
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
				ebuilder.appendSuper (super.equals (obj));

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1019;
		final int mult = 983;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	@Override
	public Course getCourse ()
	{
		return this.instance.getCourse ();
	}

	@Override
	public ActivityType getType ()
	{
		return this.instance.getType ();
	}

	@Override
	public Boolean isStealth ()
	{
		return this.instance.isStealth ();
	}

	@Override
	public Set<Grade> getGrades ()
	{
		return this.instance.getGrades ();
	}

	@Override
	protected boolean addGrade (Grade grade)
	{
		return this.instance.addGrade (grade);
	}

	public ActivityInstance getInstance ()
	{
		return this.instance;
	}

	protected void setInstance (ActivityInstance instance)
	{
		this.instance = instance;
	}

	@Override
	public List<LogEntry> getLog ()
	{
		return this.instance.getLog ();
	}

	@Override
	protected boolean addLog (LogEntry entry)
	{
		return this.instance.addLog (entry);
	}

	@Override
	public String toString ()
	{
		return new String (this.getType () + ": " + this.getName ());
	}
}
