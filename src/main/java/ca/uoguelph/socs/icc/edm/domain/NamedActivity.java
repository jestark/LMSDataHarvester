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

import java.util.Map;
import java.util.List;
import java.util.Set;

import java.util.HashMap;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

import ca.uoguelph.socs.icc.edm.domain.element.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.element.ActivityTypeData;

/**
 * Generic representation of an <code>Activity</code> which has a name.  This
 * class acts as an abstract base class for all of the <code>Activity</code>
 * implementations which have a name.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class NamedActivity extends Activity
{
	/** <code>ActivitySource</code> name to class map */
	private static final Map<String, ActivitySource> sources;

	/** <code>ActivityType</code> to implementation class map */
	private static final Map<ActivityType, Class<? extends NamedActivity>> activities;

	/** The <code>Grade</code> instances associated with the <code>Activity</code> */
	public static final Property<Grade> GRADES;

	/** The <code>SubActivity</code> instances for the <code>Activity</code> */
	public static final Property<SubActivity> SUBACTIVITIES;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>NamedActivity</code>.
	 */

	static
	{
		sources = new HashMap<> ();
		activities = new HashMap<> ();

		GRADES = Property.getInstance (Grade.class, "grade", true, false);
		SUBACTIVITIES = Property.getInstance (SubActivity.class, "subactivities", true, false);

		Definition.getBuilder (NamedActivity.class, Activity.class)
			.addProperty (Activity.ID, NamedActivity::getId, NamedActivity::setId)
			.addProperty (Activity.NAME, NamedActivity::getName, NamedActivity::setName)
			.addRelationship (Activity.COURSE, NamedActivity::getCourse, NamedActivity::setCourse)
			.addRelationship (Activity.TYPE, NamedActivity::getType, NamedActivity::setType)
			.addRelationship (Activity.LOGENTRIES, NamedActivity::getLog, NamedActivity::addLog, NamedActivity::removeLog)
			.addRelationship (GRADES, NamedActivity::getGrades, NamedActivity::addGrade, NamedActivity::removeGrade)
			.addRelationship (SUBACTIVITIES, NamedActivity::getSubActivities, NamedActivity::addSubActivity, NamedActivity::removeSubActivity)
			.addSelector (Element.SELECTOR_ID)
			.addSelector (Element.SELECTOR_ALL)
			.addSelector (Activity.SELECTOR_TYPE)
			.build ();
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated
	 * with the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends NamedActivity> getActivityClass (final ActivityType type)
	{
		assert type != null : "type is NULL";
		assert NamedActivity.activities.containsKey (type) : "Activity Type is not registered";

		return NamedActivity.activities.get (type);
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the
	 * class implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static final void registerImplementation (final String source, final String type, final Class<? extends NamedActivity> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert source.length () > 0 : "source is empty";

		ActivityType atype = new ActivityTypeData ();

		if (! NamedActivity.sources.containsKey (source))
		{
			ActivitySource asource = new ActivitySourceData ();
			asource.setName (source);

			NamedActivity.sources.put (source, asource);
		}

		atype.setSource (NamedActivity.sources.get (source));
		atype.setName (type);

		assert ! NamedActivity.activities.containsKey (atype) : "Implementation class already registered for ActivityType";

		NamedActivity.activities.put (atype, impl);
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code>
	 * instance is loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected abstract void setName (String name);

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>Activity</code> instance is loaded.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	protected abstract void setGrades (Set<Grade> grades);

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addGrade (Grade grade);

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	protected abstract boolean removeGrade (Grade grade);

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
