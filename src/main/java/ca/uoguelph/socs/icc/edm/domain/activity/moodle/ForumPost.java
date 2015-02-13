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

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityGroupMemberBuilder;

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
 * <li>ClassName      = ForumPost
 * <li>ParentClass    = ForumDiscussion
 * <li>Builder        = DefaultActivityGroupMemberBuilder
 * <li>HashBase       = 2029
 * <li>HashMult       = 661
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class ForumPost extends GenericGroupedActivityMember
{
	/**
	 * Implementation of the <code>ActivityGroupMemberElementFactory</code>.
	 * Allows the builders to create instances of <code>ForumPost</code>.
	 */

	private static final class Factory implements ActivityGroupMemberElementFactory
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
			if (! (parent instanceof ForumDiscussion))
			{
				throw new IllegalArgumentException ("Parent is not an instance of ForumDiscussion");
			}

			return new ForumPost (parent, name);
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
			((ForumPost) activity).setId (id);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>ForumPost</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityMember.registerActivity (ForumPost.class, ForumDiscussion.class, DefaultActivityGroupMemberBuilder.class, new Factory ());
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public ForumPost ()
	{
		super ();
	}

	/**
	 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
	 *
	 * @param  parent The parent <code>ActivityGroup</code>, not null
	 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
	 */

	public ForumPost (ActivityGroup parent, String name)
	{
		super (parent, name);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>ForumPost</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2029;
		final int mult = 661;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

