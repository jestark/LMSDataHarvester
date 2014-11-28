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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericGroupedActivityMember<T extends AbstractNamedActivity> extends AbstractNamedActivity implements ActivityGroupMember
{
	private Long id;
	private T parent;
	private List<LogReference> log;

	protected GenericGroupedActivityMember()
	{
		super ();
		this.id = null;
		this.log = null;
		this.parent = null;
	}

	@Override
	public boolean equals(Object obj)
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
				ebuilder.append (this.parent, ((GenericGroupedActivityMember) obj).parent);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode()
	{
		final int base = 1031;
		final int mult = 971;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());
		hbuilder.append (this.parent);

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
		return this.parent.getCourse ();
	}

	@Override
	public ActivityType getType ()
	{
		return this.parent.getType ();
	}

	@Override
	public Boolean isStealth ()
	{
		return this.parent.isStealth ();
	}

	@Override
	public Set<Grade> getGrades ()
	{
		return this.parent.getGrades ();
	}

	@Override
	protected boolean addGrade (Grade grade)
	{
		return this.parent.addGrade (grade);
	}

	@Override
	public List<LogEntry> getLog ()
	{
		List<LogEntry> result = new ArrayList<LogEntry> ();

		for (LogReference ref : this.log)
		{
			result.add (ref.getEntry ());
		}

		return result;
	}

	protected void setLog (List<LogReference> log)
	{
		this.log = log;
	}

	@Override
	protected boolean addLog (LogEntry entry)
	{
		return false;
	}

	public T getParent()
	{
		return this.parent;
	}

	protected void setParent(T parent)
	{
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		return new String (this.parent + ": " + this.getName ());
	}
}
