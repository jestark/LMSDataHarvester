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

import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityGroupMemberBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
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
 * <li>ActivityType   = forum
 * <li>ClassName      = ForumDiscussion
 * <li>ParentClass    = Forum
 * <li>ChildClass     = ForumPost
 * <li>Builder        = DefaultActivityGroupMemberBuilder
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

	private static final class Factory extends GenericGroupedActivityGroup.Factory implements ActivityGroupMemberElementFactory
	{
		/**
		 * Create an instance of the <code>Factory</code>, passing the child
		 * <code>Class</code> to the super class.
		 */

		protected Factory ()
		{
			super (ForumPost.class);
		}

		/**
		 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
		 *
		 * @param  parent The parent <code>ActivityGroup</code>, not null
		 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
		 *
		 * @return        The new sub-activity (<code>ActivityGroupMember</code>)
		 *                instance
		 */

		public ActivityGroupMember create (final ActivityGroup parent, final String name)
		{
			assert parent instanceof Forum : "parent is not an instance of Forum";
			assert name != null : "name is NULL";

			return new ForumDiscussion (parent, name);
		}
	}

	/**
	 * Register the <code>ForumDiscussion</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityMember.registerActivity (ForumDiscussion.class, Forum.class, DefaultActivityGroupMemberBuilder.class, ActivityGroupMemberElementFactory.class, new Factory ());
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

	public ForumDiscussion (final ActivityGroup parent, final String name)
	{
		super (parent, name);
	}
}
