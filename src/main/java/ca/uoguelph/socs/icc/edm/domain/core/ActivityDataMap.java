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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

/**
 * Mapping between <code>ActivityType</code> objects and the classes containing
 * the data for the associated <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

final class ActivityDataMap
{
	/** The log */
	private final Logger log;

	/** <code>ActivitySource</code> name to class map */
	private final Map<String, ActivitySource> sources;

	/** <code>ActivityType</code> to implementation class map */
	private final Map<ActivityType, Class<? extends Activity>> types;

	/** <code>ActivityGroupMember</code> parent class mapping */
	private final Map<Class<? extends ActivityGroupMember>, Class<? extends Activity>> parents;

	/** <code>Activity</code> child class mapping */
	private final Map<Class<? extends Activity>, Class<? extends ActivityGroupMember>> children;

	/**
	 * Create the <code>ActivityDataMap</code>.
	 */

	public ActivityDataMap ()
	{
		this.log = LoggerFactory.getLogger (ActivityDataMap.class);

		this.sources = new HashMap<String, ActivitySource> ();
		this.types = new HashMap<ActivityType, Class<? extends Activity>> ();
		this.parents = new HashMap<Class<? extends ActivityGroupMember>, Class<? extends Activity>> ();
		this.children = new HashMap<Class<? extends Activity>, Class<? extends ActivityGroupMember>> ();
	}

	/**
	 * Register a <code>ActivityType</code> to implementation class mapping.  This
	 * method is intended to be called from the static initializer for the class
	 * implementing the <code>Activity</code> interface for the corresponding
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	public void registerElement (final String source, final String type, final Class<? extends Activity> impl)
	{
		this.log.trace ("registerElement {}, {}, {}", type, source, impl);

		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		if (! this.sources.containsKey (source))
		{
			this.sources.put (source, new ActivitySourceData (source));
		}

		ActivityType atype = new ActivityTypeData (this.sources.get (source), type);

		if (this.types.containsKey (atype))
		{
			this.log.error ("ActivityType ({}) already registered", atype);
			throw new IllegalStateException ("ActivityType already registered");
		}

		this.types.put (atype, impl);
	}

	/**
	 * Register a parent child relationship between the classes implementing the
	 * sub-activities.
	 *
	 * @param  parent The parent class, not null
	 * @param  child  The child class, not null
	 */

	public void registerRelationship (final Class<? extends Activity> parent, final Class<? extends ActivityGroupMember> child)
	{
		this.log.trace ("registerReplationship {}, {}", parent, child);

		assert parent != null : "parent is NULL";
		assert child != null : "child is NULL";
		assert (! this.parents.containsKey (child)) : "child is already registered";
		assert (! this.children.containsKey (parent)) : "parent is already registered";

		this.parents.put (child, parent);
		this.children.put (parent, child);
	}

	/**
	 * Get the implementation class which contains the <code>Activity</code>
	 * specific data for the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>
	 */

	public Class<? extends Activity> getElement (final ActivityType type)
	{
		assert type != null : "type is null";

		return this.types.get (type);
	}

	/**
	 * Get the parent <code>Class</code> for the specified child
	 * <code>Class</code>.
	 *
	 * @param  child The child class
	 *
	 * @return       The parent class, or null if the child is not registered
	 */

	public Class<? extends Activity> getParent (final Class<? extends ActivityGroupMember> child)
	{
		assert child != null : "Child is NULL";

		return this.parents.get (child);
	}

	/**
	 * Get the child <code>Class</code> for the specified parent
	 * <code>Class</code>.
	 *
	 * @param  parent The parent class
	 *
	 * @return        The child class, or null if the parent is not registered
	 */

	public Class<? extends ActivityGroupMember> getChild (final Class<? extends Activity> parent)
	{
		assert parent != null : "parent is NULL";

		return this.children.get (parent);
	}
}
