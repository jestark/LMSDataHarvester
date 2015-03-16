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
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.builder.NamedActivityElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivity;
import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

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
 * <li>ClassName      = Forum
 * <li>ChildClass     = ForumDiscussion
 * <li>Builder        = DefaultNamedActivityBuilder
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Forum extends GenericGroupedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Forum</code>.
	 */

	private static final class Factory extends GenericGroupedActivity.Factory implements NamedActivityElementFactory
	{
		/**
		 * Create an instance of the <code>Factory</code>, passing the child
		 * <code>Class</code> to the super class.
		 */

		protected Factory ()
		{
			super (ForumDiscussion.class);
		}

		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  type    The <code>ActivityType</code> of the
		 *                 <code>Activity</code>, not null
		 * @param  course  The <code>Course</code> which is associated with the
		 *                 <code>Activity</code> instance, not null
		 * @param  name    The name of the <code>Activity</code>, not null
		 *
		 * @return         The new <code>Activity</code> instance
		 */

		public Activity create (final ActivityType type, final Course course, final String name)
		{
			assert type != null : "type is NULL";
			assert course != null : "course is NULL";
			assert name != null : "name is NULL";

			return new Forum (type, course, name);
		}

	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Forum</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Forum.class, DefaultNamedActivityBuilder.class, NamedActivityElementFactory.class, new Factory (), "moodle", "forum");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Forum ()
	{
		super ();
	}

	/**
	 * Create a new <code>Activity</code> instance.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 * @param  name    The name of the <code>Activity</code>, not null
	 */

	public Forum (final ActivityType type, final Course course, final String name)
	{
		super (type, course, name);
	}

	/**
	 * Get the <code>List</code> of <code>ActivityGroupMember</code> instances (or
	 * Sub-Activities) associated with the <code>ActvityGroup</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @return The <code>List</code> of sub-activities
	 */

	public List<ActivityGroupMember> getChildren ()
	{
		return super.getChildren ();
	}

	/**
	 * Initialize the <code>List</code> of sub-activity instances for the
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is loaded.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * child class.
	 *
	 * @param  children The <code>List</code> of sub-activity instances, not null
	 */

	protected void setChildren (final List<ActivityGroupMember> children)
	{
		assert children != null : "children is NULL";

		super.setChildren (children);
	}
}
