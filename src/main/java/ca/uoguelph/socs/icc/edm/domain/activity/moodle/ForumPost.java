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

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityGroupMemberBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;
import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>ActivityGroupMember</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>ActivityGroupMember</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivityMember</code>
 * template, with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = forum
 * <li>ClassName      = ForumPost
 * <li>ParentClass    = ForumDiscussion
 * <li>Builder        = DefaultActivityGroupMemberBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.2
 */

public class ForumPost extends GenericGroupedActivityMember
{
	/**
	 * Implementation of the <code>ActivityGroupMemberElementFactory</code>.
	 * Allows the builders to create instances of <code>ForumPost</code>.
	 */

	private static final class Factory extends AbstractActivity.Factory implements ActivityGroupMemberElementFactory
	{
		/**
		 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
		 *
		 * @param  parent The parent <code>Activity</code>, not null
		 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
		 *
		 * @return        The new sub-activity (<code>ActivityGroupMember</code>)
		 *                instance
		 */

		public ActivityGroupMember create (final Activity parent, final String name)
		{
			assert parent instanceof ForumDiscussion : "parent is not an instance of ForumDiscussion";
			assert name != null : "name is NULL";

			return new ForumPost (parent, name);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>ForumPost</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityMember.registerActivity (ForumPost.class, ForumDiscussion.class, DefaultActivityGroupMemberBuilder.class, ActivityGroupMemberElementFactory.class, new Factory ());
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
	 * @param  parent The parent <code>Activity</code>, not null
	 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
	 */

	public ForumPost (final Activity parent, final String name)
	{
		super (parent, name);
	}

	/**
	 * Get the parent <code>Activity</code> instance for the sub-activity.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * parent class.
	 *
	 * @return The parent <code>Activity</code>
	 */

	@Override
	public Activity getParent ()
	{
		return super.getParent ();
	}

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * sub-activity.  This method is intended to be used by a 
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * parent class.
	 *
	 * @param  parent The <code>Activity</code> containing this
	 *                <code>Activity</code> instance
	 */

	protected void setParent (final Activity parent)
	{
		assert parent != null : "parent is NULL";

		super.setParent (parent);
	}
}
