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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.util.List;
import java.util.Set;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;

/**
 * Abstract implementation of the <code>Activity</code> interface.  This class
 * acts as an abstract base class, containing the common <code>Element</code>
 * references, for all of the classes implementing the <code>Activity</code>
 * interface, which are not sub-activities.  It is expected that instances of
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivityInstance extends Activity
{
	/** The <code>MetaData</code> definition for the <code>ActivityInstance</code> */
	protected static final Definition<ActivityInstance> metadata;

	static
	{
		metadata = Definition.getBuilder (ActivityInstance.class, Activity.class)
			.addRelationship (Activity.COURSE, Activity::getCourse, ActivityInstance::setCourse)
			.addRelationship (Activity.TYPE, Activity::getType, ActivityInstance::setType)
			.build ();

		Profile.registerMetaData (metadata);
	}

	/**
	 * Set the <code>Course</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  course The <code>Course</code>, not null
	 */

	protected abstract void setCourse (Course course);

	/**
	 * Set the <code>ActvityType</code> with which the <code>Activity</code> is
	 * associated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 */

	protected abstract void setType (ActivityType type);
}
