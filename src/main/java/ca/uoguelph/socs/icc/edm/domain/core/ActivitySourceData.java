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

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivitySourceBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivitySourceElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivitySourceFactory;
import ca.uoguelph.socs.icc.edm.domain.manager.DefaultActivitySourceManager;

/**
 * Implementation of the <code>ActivitySource</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivitySource</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivitySource</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivitySourceBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivitySourceManager
 */

public class ActivitySourceData implements ActivitySource, Serializable
{
	private static final class ActivitySourceDataFactory implements ActivitySourceElementFactory
	{
		@Override
		public ActivitySource create (String name)
		{
			return new ActivitySourceData (name);
		}

		@Override
		public void setId (ActivitySource ActivitySource, Long id)
		{
			((ActivitySourceData) ActivitySource).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the activity source */
	private Long id;

	/** The name of the activity source */
	private String name;

	/** The set of activity types which are associated with the source */
	private Set<ActivityType> types;

	static
	{
		(ActivitySourceFactory.getInstance ()).registerElement (ActivitySourceData.class, DefaultActivitySourceManager.class, DefaultActivitySourceBuilder.class, new ActivitySourceDataFactory ());
	}

	public ActivitySourceData ()
	{
		this.id = null;
		this.name = null;
		this.types = null;
	}

	public ActivitySourceData (String name)
	{
		this ();

		this.name = name;

		this.types = new HashSet<ActivityType> ();
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivitySourceData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((ActivitySourceData) obj).name);

			result = ebuilder.isEquals ();
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
