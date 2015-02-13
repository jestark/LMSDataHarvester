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

package ca.uoguelph.socs.icc.edm.domain.activity.moodle;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityGroupMemberBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Forum
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>ActivityGroup</code> interface,
 * along with the relevant manager, and builder.  See the
 * <code>ActivityGroup</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Forum
 * <li>ClassName      = ForumDiscussion
 * <li>ParentClass    = Forum
 * <li>ChildClass     = ForumPost
 * <li>Builder        = DefaultActivityGroupMemberBuilder
 * <li>HashBase       = 2027
 * <li>HashMult       = 673
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class ForumDiscussion extends GenericGroupedActivityGroup
{
	/**
	 * Implementation of the <code>ActivityGroupMemberElementFactory</code>.
	 * Allows the builders to create instances of <code>ForumDiscussion</code>.
	 */

	private static final class Factory implements ActivityGroupElementFactory, ActivityGroupMemberElementFactory
	{
		/**
		 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
		 *
		 * @param  parent The parent <code>ActivityGroup</code>, not null
		 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
		 *
		 * @return        The new sub-activity (<code>ActivityGroupMember</code>)
		 *                instance
		 */

		public ActivityGroupMember create (ActivityGroup parent, String name)
		{
			if (! (parent instanceof Forum))
			{
				throw new IllegalArgumentException ("Parent is not an instance of Forum");
			}

			return new ForumDiscussion (parent, name);
		}

		/**
		 * Write the specified <code>DataStore</code> ID number into the
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> to which the ID number is
		 *                  assigned not null
		 * @param  id       The ID number assigned to the <code>Activity</code>, not
		 *                  null
		 */

		public void setId (Activity activity, Long id)
		{
			((ForumDiscussion) activity).setId (id);
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

		public boolean addChild (ActivityGroup group, ActivityGroupMember member)
		{
			if (! (member instanceof ForumPost))
			{
				throw new IllegalArgumentException ("Child is not an instance of ForumPost");
			}

			return ((ForumDiscussion) group).addChild (member);
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

		public boolean removeChild (ActivityGroup group, ActivityGroupMember member)
		{
			if (! (member instanceof ForumPost))
			{
				throw new IllegalArgumentException ("Child is not an instance of ForumPost");
			}

			return ((ForumDiscussion) group).removeChild (member);
		}
	}

	/**
	 * Register the <code>ForumDiscussion</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityMember.registerActivity (ForumDiscussion.class, Forum.class, DefaultActivityGroupMemberBuilder.class, new Factory ());
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public ForumDiscussion ()
	{
		super ();
	}

	/**
	 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
	 *
	 * @param  parent The parent <code>ActivityGroup</code>, not null
	 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
	 */

	public ForumDiscussion (ActivityGroup parent, String name)
	{
		super (parent, name);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>ForumDiscussion</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2027;
		final int mult = 673;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
