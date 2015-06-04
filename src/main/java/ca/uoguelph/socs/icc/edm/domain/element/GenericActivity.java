/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultGenericActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>Activity</code> interface, for
 * <code>Activity</code> instances which do not contain additional data.  It is
 * expected that instances of this class will be accessed though the
 * <code>Activity</code> interface, along with the relevant manager, and
 * builder.  See the <code>Activity</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     ca.uoguelph.socs.icc.edm.domain.builder.DefaultActivityBuilder
 */


public class GenericActivity extends ActivityInstance implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Static initializer to register the <code>GenericActivity</code> class
	 * with the factories.
	 */

	static
	{
		DefinitionBuilder<GenericActivity, Activity.Properties> builder = DefinitionBuilder.newInstance (Activity.class, GenericActivity.class, Activity.Properties.class);
		builder.setCreateMethod (GenericActivity::new);

		builder.addUniqueAttribute (Activity.Properties.ID, Long.class, false, false, AbstractElement::getId, AbstractElement::setId);

		builder.addAttribute (Activity.Properties.COURSE, Course.class, true, false, ActivityInstance::getCourse, ActivityInstance::setCourse);
		builder.addAttribute (Activity.Properties.TYPE, ActivityType.class, true, false, ActivityInstance::getType, ActivityInstance::setType);

		builder.addRelationship ("grades", Grade.class, ActivityInstance::addGrade, ActivityInstance::removeGrade);
		builder.addRelationship ("log", LogEntry.class, AbstractActivity::addLog, AbstractActivity::removeLog);

		AbstractElement.registerElement (builder.build (), DefaultGenericActivityBuilder.class);
	}

	/**
	 * Create the <code>GenericActivity</code> with null values.
	 */

	protected GenericActivity ()
	{
		super ();
	}

	/**
	 * Create a new <code>GenericActivity</code> instance.
	 *
	 * @param  type    The <code>ActivityType</code> of the
	 *                 <code>Activity</code>, not null
	 * @param  course  The <code>Course</code> which is associated with the
	 *                 <code>Activity</code> instance, not null
	 */

	public GenericActivity (final ActivityType type, final Course course)
	{
		super (type, course);
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof GenericActivity)
		{
			result = super.equals (obj);
		}

		return result;
	}

	/**
	 * Get the name of the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances have names.  For those
	 * <code>Activity</code> instances which do not have names, the name of the
	 * associated <code>ActivityType</code> will be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return (this.getType ()).getName ();
	}
}
