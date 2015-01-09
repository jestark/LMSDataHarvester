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

public abstract class GenericGroupedActivityGroup<T extends AbstractNamedActivity, E extends ActivityGroupMember> extends GenericGroupedActivityMember<T> implements ActivityGroup<E>, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	private Set<E> children;

	public GenericGroupedActivityGroup ()
	{
		super ();
		this.children = null;
	}

	public GenericGroupedActivityGroup (String name, T parent)
	{
		super (name, parent);

		this.children = new HashSet<E> ();
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

				result = ebuilder.isEquals ();
			}
		}

		return result;
	}

	@Override
	public int hashCode()
	{
		final int base = 1033;
		final int mult = 967;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	public Set<E> getChildren()
	{
		return new HashSet<E> (this.children);
	}

	protected void setChildren(Set<E> children)
	{
		this.children = children;
	}

	public void addChild(E child)
	{
		this.children.add (child);
	}
}
