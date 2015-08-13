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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a child element in a hierarchy of <code>Activity</code>
 * instances.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class SubActivity extends ParentActivity
{
	/** <code>Activity</code> to <code>SubActivity</code> class mapping */
	private static final Map<Class<? extends ParentActivity>, Class<? extends SubActivity>> subactivities;

	/** The <code>MetaData</code> definition for the <code>SubActivity</code> */
	protected static final Definition<SubActivity> metadata;

	/** The <code>DataStore</code> identifier of the <code>SubActivity</code> */
	public static final Property<Long> ID;

	/** The name of the <code>SubActivity</code> */
	public static final Property<String> NAME;

	/** The parent <code>Activity</code> */
	public static final Property<ParentActivity> PARENT;

	/** The <code>LogEntry</code> instances associated with the <code>SubActivity</code> */
	public static final Property<LogEntry> LOGENTRIES;

	/** The <code>SubActivity</code> instances for the <code>SubActivity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>SubActivity</code>.
	 */

	static
	{
		subactivities = new HashMap<> ();

		ID = Property.getInstance (SubActivity.class, Long.class, "id", false, false);
		NAME = Property.getInstance (SubActivity.class, String.class, "name", false, true);
		PARENT = Property.getInstance (SubActivity.class, ParentActivity.class, "parent", false, true);

		LOGENTRIES = Property.getInstance (SubActivity.class, LogEntry.class, "logentries", true, false);
		SUBACTIVITIES = Property.getInstance (SubActivity.class, SubActivity.class, "subactivities", true, false);

		metadata = Definition.getBuilder (SubActivity.class, Element.class)
			.addProperty (ID, SubActivity::getId, SubActivity::setId)
			.addProperty (NAME, SubActivity::getName, SubActivity::setName)
			.addRelationship (PARENT, SubActivity::getParent, SubActivity::setParent)
			.addRelationship (LOGENTRIES, SubActivity::getLog, SubActivity::addLog, SubActivity::removeLog)
			.addRelationship (SUBACTIVITIES, SubActivity::getSubActivities, SubActivity::addSubActivity, SubActivity::removeSubActivity)
			.build ();

		Profile.registerMetaData (metadata);
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is
	 * associated with the specified <code>Activity</code> implementation
	 * class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 *
	 * @return          The <code>SubActivity</code> implementation class, may be
	 *                  null
	 */

	public static final Class<? extends SubActivity> getSubActivityClass (final Class<? extends ParentActivity> activity)
	{
		assert activity != null : "activity is NULL";
		assert SubActivity.subactivities.containsKey (activity) : "Activity is not registered";

		return SubActivity.subactivities.get (activity);
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	protected static final void registerImplementation (final Class<? extends ParentActivity> activity, final Class<? extends SubActivity> subactivity)
	{
		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";
		assert (! SubActivity.subactivities.containsKey (activity)) : "activity is already registered";

		SubActivity.subactivities.put (activity, subactivity);
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public final ActivityType getType()
	{
		return this.getParent ().getType ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public final Course getCourse ()
	{
		return this.getParent ().getCourse ();
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public final Set<Grade> getGrades ()
	{
		return this.getParent ().getGrades ();
	}

	/**
	 * Get the parent <code>Activity</code> instance for the
	 * <code>SubActivity</code>.
	 *
	 * @return The parent <code>Activity</code>
	 */

	public abstract ParentActivity getParent ();

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * <code>SubActivity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>Activity</code> containing this
	 *                  <code>SubActivity</code> instance
	 */

	protected abstract void setParent (ParentActivity activity);

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
