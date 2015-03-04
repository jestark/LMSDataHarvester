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

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupElementFactory;

/**
 * Generic representation of an <code>Activity</code> tree.  This class
 * extends <code>GenericNamedActivity</code> to act as an abstract base class
 * for the root node of a sub-activity tree, by implementing the
 * <code>ActivityGroup</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class GenericGroupedActivity extends GenericNamedActivity implements ActivityGroup, Serializable
{
	/**
	 * Implementation of the <code>ActivityGroupElementFactory</code>.
	 * Centralizes the implementation of the common add/remove operations.
	 */

	protected static abstract class Factory extends GenericNamedActivity.Factory implements ActivityGroupElementFactory
	{
		/** The <code>Class</code> of the parent <code>Element</code> */
		private final Class<?> parent; 

		/** The <code>Class</code> of the child <code>Element</code> */
		private final Class<?> child;

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
			this.parent = (this.getClass ()).getEnclosingClass ();
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

		@Override
		public final boolean addChild (final ActivityGroup group, final ActivityGroupMember member)
		{
			assert this.parent.isInstance (group) : "group is not an instance of " + this.parent.getSimpleName ();
			assert this.child.isInstance (member) : "member is not an instance of " + this.child.getSimpleName ();

			return ((GenericGroupedActivity) group).addChild (member);
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

		@Override
		public final boolean removeChild (final ActivityGroup group, final ActivityGroupMember member)
		{
			assert this.parent.isInstance (group) : "group is not an instance of " + this.parent.getSimpleName ();
			assert this.child.isInstance (member) : "member is not an instance of " + this.child.getSimpleName ();

			return ((GenericGroupedActivity) group).removeChild (member);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The set of sub-activities  */
	private Set<ActivityGroupMember> children;

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public GenericGroupedActivity ()
	{
		super ();

		this.children = new HashSet<ActivityGroupMember> ();
	}

	/**
	 * Create the <code>GenericGroupedActivity</code>.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  stealth Indicator if the <code>Activity</code> was added by the
	 *                 system, not null
	 * @param  name    The name of the <code>Activity</code>
	 */

	public GenericGroupedActivity (final ActivityType type, final Course course, final Boolean stealth, final String name)
	{
		super (type, course, stealth, name);

		this.children = new HashSet<ActivityGroupMember> ();
	}

	/**
	 * Get the <code>Set</code> of <code>ActivityGroupMember</code> instances (or
	 * Sub-Activities) associated with the <code>ActvityGroup</code>.
	 *
	 * @return The <code>Set</code> of sub-activities
	 */

	public Set<ActivityGroupMember> getChildren()
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
