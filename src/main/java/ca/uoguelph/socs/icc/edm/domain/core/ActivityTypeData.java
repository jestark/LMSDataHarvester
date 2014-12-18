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

import java.util.Set;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

public class ActivityTypeData implements ActivityType
{
	private Long id;
	private String name;
	private ActivitySource source;
	private Set<Action> actions;

	protected ActivityTypeData ()
	{
		this.id = null;
		this.name = null;
		this.source = null;
		this.actions = null;
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
				ebuilder.append (this.name, ((ActivityTypeData) obj).name);
				ebuilder.append (this.source, ((ActivityTypeData) obj).source);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1009;
		final int mult = 997;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.source);

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
	public String getName ()
	{
		return this.name;
	}

	protected void setName (String name)
	{
		this.name = name;
	}

	@Override
	public ActivitySource getSource ()
	{
		return this.source;
	}

	protected void setSource (ActivitySource source)
	{
		this.source = source;
	}

	@Override
	public Set<Action> getActions ()
	{
		return new HashSet<Action> (this.actions);
	}

	protected void setActions (Set<Action> actions)
	{
		this.actions = actions;
	}

	public void addAction (Action action)
	{
		this.actions.add (action);
	}

	@Override
	public String toString ()
	{
		return new String (this.source.toString () + ": " + this.name);
	}
}
