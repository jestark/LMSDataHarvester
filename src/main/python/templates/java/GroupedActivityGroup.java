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

package ca.uoguelph.socs.icc.edm.domain.activity.${ActivitySource};

import java.util.List;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.${Builder};
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.AbstractActivity;
import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivityMember;

/**
 * Implementation of the <code>Activity</code> interface for the ${ActivitySource}/${ActivityType}
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivityGroup</code>
 * template, with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = ${ActivitySource}
 * <li>ActivityType   = ${ActivityType}
 * <li>ClassName      = ${ClassName}
 * <li>ParentClass    = ${ParentClass}
 * <li>Builder        = ${Builder}
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.2
 */

public class ${ClassName} extends GenericGroupedActivityMember
{
	/**
	 * Implementation of the <code>ActivityGroupMemberElementFactory</code>.
	 * Allows the builders to create instances of <code>${ClassName}</code>.
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
			assert parent instanceof ${ParentClass} : "parent is not an instance of ${ParentClass}";
			assert name != null : "name is NULL";

			return new ${ClassName} (parent, name);
		}
	}

	/**
	 * Register the <code>${ClassName}</code> with the factories on initialization.
	 */

	static
	{
		GenericGroupedActivityMember.registerActivity (${ClassName}.class, ${ParentClass}.class, ${Builder}.class, ActivityGroupMemberElementFactory.class, new Factory ());
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public ${ClassName} ()
	{
		super ();
	}

	/**
	 * Create a new sub-activity (<code>ActivityGroupMember</code>) instance.
	 *
	 * @param  parent The parent <code>Activity</code>, not null
	 * @param  name   The name of the <code>ActivityGroupMember</code>, not null
	 */

	public ${ClassName} (final Activity parent, final String name)
	{
		super (parent, name);
	}

	/**
	 * Get the <code>List</code> of <code>ActivityGroupMember</code> instances (or
	 * Sub-Activities) associated with the <code>Actvity</code>.
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

	/**
	 * Get the parent <code>Activity</code> instance for the sub-activity.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of the
	 * parent class.
	 *
	 * @return The parent <code>ActivityGroup</code>
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
	 * @param  parent The <code>ActivityGroup</code> containing this
	 *                <code>Activity</code> instance
	 */

	protected void setParent (final Activity parent)
	{
		assert parent != null : "parent is NULL";

		super.setParent (parent);
	}
}
