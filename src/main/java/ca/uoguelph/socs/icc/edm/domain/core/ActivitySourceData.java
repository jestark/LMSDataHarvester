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

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivitySourceElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivitySourceFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager;

public class ActivitySourceData implements ActivitySource
{
	private static final class ActivitySourceDataFactory implements ActivitySourceElementFactory
	{
		@Override
		public ActivitySource create (String name)
		{
			ActivitySourceData source = new ActivitySourceData ();

			source.setName (name);

			return source;
		}

		@Override
		public void setId (ActivitySource ActivitySource, Long id)
		{
			((ActivitySourceData) ActivitySource).setId (id);
		}
	}

	private Long id;
	private String name;
	private Set<ActivityType> types;

	static
	{
		(ActivitySourceFactory.getInstance ()).registerElement (ActivitySourceData.class, DefaultActivitySourceManager.class, DefaultActivitySourceBuilder.class, new ActivitySourceDataFactory ());
	}

	protected ActivitySourceData ()
	{
		this.id = null;
		this.name = null;
		this.types = null;
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
				ebuilder.append (this.name, ((ActivitySourceData) obj).name);

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode ()
	{
		final int base = 1097;
		final int mult = 883;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);

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
	public Set<ActivityType> getTypes ()
	{
		return new HashSet<ActivityType> (this.types);
	}

	protected void setTypes (Set<ActivityType> types)
	{
		this.types = types;
	}

	@Override
	public String toString ()
	{
		return this.name;
	}
}
