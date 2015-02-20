/* Copyright (C) 2014, 2015 James E. Stark
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
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupElementFactory;

/**
 * A generic representation of a node in a sub-activity tree.  This class
 * extends <code>GenericGroupedActivityMember</code> to act as an abstract base
 * class for sub-activity tree nodes (elements of the tree with both parents and
 * children) by implementing the <code>ActivityGroup</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class GenericGroupedActivityGroup extends GenericGroupedActivityMember implements ActivityGroup, Serializable
{
	/**
	 * Implementation of the <code>ActivityGroupElementFactory</code>.
	 * Centralizes the implementation of the common add/remove operations.
	 */

	protected static abstract class Factory extends GenericGroupedActivityMember.Factory implements ActivityGroupElementFactory
	{
		/** The <code>Class</code> of the child <code>Element</code> */
		private final Class<? extends ActivityGroupMember> child;

		/**
		 * Create an instance of the <code>Factory</code>.  This method sets the
		 * <code>Class</code> of the child element to be used to enforce the safety
		 * of the types in the tree.
		 *
		 * @param  child The Class of the child <code>Element</code>, not null
		 */

		protected Factory (final Class<? extends ActivityGroupMember> child)
		{
			assert child != null : "Child class is NULL";

			this.child = child;
		}

		/**
		 * Add the specified <code>ActivityGroupMember</code> to the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> to which the
		 *                <code>ActivityGroupMember</code> is to be added, not null
		 * @param  member The <code>ActivityGroupMember</code> to add to the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully added to the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public final boolean addChild (final ActivityGroup group, final ActivityGroupMember member)
		{
			assert group != null : "ActivityGroup is NULL";
			assert member != null : "ActivityGroup Member is NULL";
			assert this.child.isInstance (member) : "Required: " + this.child.getSimpleName () + " Found:" + (member.getClass ()).getSimpleName ();

			return ((GenericGroupedActivityGroup) group).addChild (member);
		}

		/**
		 * Remove the specified <code>ActivityGroupMember</code> from the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> from which the
		 *                <code>ActivityGroupMember</code> is to be removed, not null
		 * @param  member The <code>ActivityGroupMember</code> to remove from the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully removed from the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public final boolean removeChild (final ActivityGroup group, final ActivityGroupMember member)
		{
			assert group != null : "ActivityGroup is NULL";
			assert member != null : "ActivityGroup Member is NULL";
			assert this.child.isInstance (member) : "Required: " + this.child.getSimpleName () + " Found:" + (member.getClass ()).getSimpleName ();

			return ((GenericGroupedActivityGroup) group).removeChild (member);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The set of sub-activities  */
	private Set<ActivityGroupMember> children;

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public GenericGroupedActivityGroup ()
	{
		super ();
		this.children = null;
	}

	/**
	 * Create the <code>Activity</code>.
	 *
	 * @param  parent The <code>ActivityGroup</code> containing this
	 *                <code>Activity</code> instance
	 * @param  name   The name of the <code>Activity</code>
	 */

	public GenericGroupedActivityGroup (final ActivityGroup parent, final String name)
	{
		super (parent, name);

		this.children = new HashSet<ActivityGroupMember> ();
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.  The <code>Activity</code> instances are compared based upon the
	 * data contained in the superclass.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals(final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof GenericGroupedActivityGroup)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.appendSuper (super.equals (obj));

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the data contained in the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode()
	{
		final int base = 1033;
		final int mult = 967;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>Set</code> of <code>ActivityGroupMember</code> instances (or
	 * Sub-Activities) associated with the <code>ActvityGroup</code>.
	 *
	 * @return The <code>Set</code> of sub-activities
	 */

	public Set<ActivityGroupMember> getChildren ()
	{
		return new HashSet<ActivityGroupMember> (this.children);
	}

	/**
	 * Initialize the <code>Set</code> of sub-activity instances for the
	 * <code>Activity</code>.  This method is intended to be used by a 
	 * <code>DataStore</code> when the <code>Activity</code> instance is loaded.
	 *
	 * @param  children The <code>Set</code> of sub-activity instances, not null
	 */

	protected void setChildren (final Set<ActivityGroupMember> children)
	{
		assert children != null : "children is NULL";

		this.children = children;
	}

	/**
	 * Add the specified <code>ActivityGroupMember</code> to the specified
	 * <code>ActivityGroup</code>.
	 *
	 * @param  child The <code>ActivityGroupMember</code> to add, not null
	 *
	 * @return       <code>True</code> if the <code>ActivityGroupMember</code>
	 *               was successfully added, <code>False</code> otherwise
	 */

	protected boolean addChild (final ActivityGroupMember child)
	{
		assert child != null : "child is NULL";

		return this.children.add (child);
	}

	/**
	 * Remove the specified <code>ActivityGroupMember</code> from the specified
	 * <code>ActivityGroup</code>.
	 *
	 * @param  child The <code>ActivityGroupMember</code> to remove, not null
	 *
	 * @return       <code>True</code> if the <code>ActivityGroupMember</code>
	 *               was successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeChild (final ActivityGroupMember child)
	{
		assert child != null : "child is NULL";

		return this.children.remove (child);
	}
}
