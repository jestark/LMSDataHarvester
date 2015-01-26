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

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityTypeBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityTypeElementFactory;
import ca.uoguelph.socs.icc.edm.domain.factory.ActivityTypeFactory;

/**
 * Implementation of the <code>ActivityType</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivityType</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivityType</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityTypeBuilder
 * @see     ca.uoguelph.socs.icc.edm.domain.ActivityTypeManager
 */

public class ActivityTypeData implements ActivityType, Serializable
{
	private static final class ActivityTypeDataFactory implements ActivityTypeElementFactory
	{
		@Override
		public ActivityType create (ActivitySource source, String name)
		{
			return new ActivityTypeData (source, name);
		}

		@Override
		public void setId (ActivityType type, Long id)
		{
			((ActivityTypeData) type).setId (id);
		}
	}
	
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;
	
	/** The primary key of the activity type */
	private Long id;

	/** The name of the activity type */
	private String name;

	/** The source of the activity type */
	private ActivitySource source;

	/** The set of actions which are associated with the activity type */
	private Set<Action> actions;

	static
	{
		(ActivityTypeFactory.getInstance ()).registerElement (ActivityTypeData.class, DefaultActivityTypeBuilder.class, new ActivityTypeDataFactory ());
	}

	public ActivityTypeData ()
	{
		this.id = null;
		this.name = null;
		this.source = null;
		this.actions = null;
	}

	public ActivityTypeData (ActivitySource source, String name)
	{
		this ();

		this.name = name;
		this.source = source;

		this.actions = new HashSet<Action> ();
	}

	@Override
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof ActivityTypeData)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));
			ebuilder.append (this.name, ((ActivityTypeData) obj).name);
			ebuilder.append (this.source, ((ActivityTypeData) obj).source);

			result = ebuilder.isEquals ();
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
