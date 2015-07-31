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

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

/**
 * Mapping between <code>ActivityType</code> objects and the classes containing
 * the data for the associated <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public final class ActivityDataMap
{
	/** The log */
	private final Logger log;

	/** <code>ActivitySource</code> name to class map */
	private final Map<String, ActivitySource> sources;

	/** <code>ActivityType</code> to implementation class map */
	private final Map<ActivityType, Class<? extends Element>> activities;

	/** <code>Activity</code> to <code>SubActivity</code> class mapping */
	private final Map<Class<? extends Element>, Class<? extends Element>> subactivities;

	/**
	 * Create the <code>ActivityDataMap</code>.
	 */

	public ActivityDataMap ()
	{
		this.log = LoggerFactory.getLogger (ActivityDataMap.class);

		this.sources = new HashMap<String, ActivitySource> ();
		this.activities = new HashMap<ActivityType, Class<? extends Element>> ();
		this.subactivities = new HashMap<Class<? extends Element>, Class<? extends Element>> ();
	}

	/**
	 * Get the corresponding <code>ActivitySource</code> instance for the
	 * specified name.  If the <code>ActivitySource</code> instance does not
	 * exist, it will be created.
	 *
	 * @param  name The name of the <code>ActivitySource</code> not null
	 *
	 * @return      The corresponding <code>ActivitySource</code> instance
	 */

	public ActivitySource getActivitySource (final String name)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is an empty String";

		if (! this.sources.containsKey (name))
		{
			ActivitySourceData source = new ActivitySourceData ();
			source.setName (name);

			this.sources.put (name, source);
		}

		return this.sources.get (name);
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the
	 * class implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 * @param  impl The implementation class, not null
	 */

	public void registerActivityClass (final ActivityType type, final Class<? extends Element> impl)
	{
		this.log.trace ("registerActivityImplClass type={}, impl={}", type, impl);

		assert impl != null : "impl is NULL";
		assert type != null : "type is NULL";
		assert (! this.activities.containsKey (type)) : "type is already registered";

		this.activities.put (type, impl);
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

	public void registerActivityClass (final String source, final String type, final Class<? extends Element> impl)
	{
		this.log.trace ("registerElement source={}, type={}, impl={}", type, source, impl);

		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		ActivityTypeData atype = new ActivityTypeData ();
		atype.setSource (this.getActivitySource (source));
		atype.setName (type);

		this.registerActivityClass (atype, impl);
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	public void registerSubActivityClass (final Class<? extends Element> activity, final Class<? extends Element> subactivity)
	{
		this.log.trace ("registerSubActivity activity={}, subactivity={}", activity, subactivity);

		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";
		assert (! this.subactivities.containsKey (activity)) : "activity is already registered";

		this.subactivities.put (activity, subactivity);
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

	public Class<? extends Element> getActivityClass (final ActivityType type)
	{
		return this.activities.get (type);
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is
	 * associated with the specified <code>Activity</code> implementation
	 * class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 *
	 * @return          The <code>SubActivity</code> implementation class, may
	 *                  be null
	 */

	public Class<? extends Element> getSubActivityClass (final Class<? extends Element> activity)
	{
		return this.subactivities.get (activity);
	}
}
