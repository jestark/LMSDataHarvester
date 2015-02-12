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

package ca.uoguelph.socs.icc.edm.domain.activity;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.core.ActivitySourceData;
import ca.uoguelph.socs.icc.edm.domain.core.ActivityTypeData;

/**
 * Mapping between <code>ActivityType</code> objects and the classes containing
 * the data for the associated <code>Activity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class ActivityDataMap
{
	/** Singleton instance */
	private static final ActivityDataMap instance;

	/** The log */
	private final Logger log;

	/** <code>ActivitySource</code> name to class map */
	private final Map<String, ActivitySource> sources;

	/** <code>ActivityType</code> to implementation class map */
	private final Map<ActivityType, Class<? extends Activity>> types;

	/** <code>ActivityGroupMember</code> parent class mapping */
	private final Map<Class<? extends ActivityGroupMember>, Class<? extends ActivityGroup>> parents;

	/** <code>ActivityGroup</code> child class mapping */
	private final Map<Class<? extends ActivityGroup>, Class<? extends ActivityGroupMember>> children;

	/**
	 * Create the singleton instance when the class is loaded
	 */

	static
	{
		instance = new ActivityDataMap ();
	}

	/**
	 * Get an instance of the <code>ActivityDataMap</code>.
	 *
	 * @return The <code>ActivityDataMap</code> instance
	 */

	public static ActivityDataMap getInstance ()
	{
		return ActivityDataMap.instance;
	}

	/**
	 * Create the <code>ActivityDataMap</code>.
	 */

	private ActivityDataMap ()
	{
		this.log = LoggerFactory.getLogger (ActivityDataMap.class);

		this.sources = new HashMap<String, ActivitySource> ();
		this.types = new HashMap<ActivityType, Class<? extends Activity>> ();
		this.parents = new HashMap<Class<? extends ActivityGroupMember>, Class<? extends ActivityGroup>> ();
		this.children = new HashMap<Class<? extends ActivityGroup>, Class<? extends ActivityGroupMember>> ();
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

	public void registerElement (String source, String type, Class<? extends Activity> impl)
	{
		this.log.trace ("Register ActivityType -> implementation map {} ({}) -> {}", type, source, impl);

		if (source == null)
		{
			this.log.error ("Attempting to Register a NULL ActivitySource");
			throw new NullPointerException ();
		}

		if (type == null)
		{
			this.log.error ("Attempting to Register a NULL ActivityType");
			throw new NullPointerException ();
		}

		if (impl == null)
		{
			this.log.error ("Attempting to Register a NULL Activity implementation");
			throw new NullPointerException ();
		}

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
	 * @param  parent                   The parent class, not null
	 * @param  child                    The child class, not null
	 *
	 * @throws IllegalArgumentException If the parent of child class is already
	 *                                  registered
	 */

	public void registerRelationship (Class<? extends ActivityGroup> parent, Class<? extends ActivityGroupMember> child)
	{
		if (parent == null)
		{
			this.log.error ("Attempting to register a NULL parent");
			throw new NullPointerException ();
		}

		if (child == null)
		{
			this.log.error ("Attempting to register a NULL child");
			throw new NullPointerException ();
		}

		if (this.parents.containsKey (child))
		{
			this.log.error ("Child already registered: {}", child);
			throw new IllegalArgumentException ("Child already registered");
		}

		if (this.children.containsKey (parent))
		{
			this.log.error ("Parent already registered: {}", parent);
			throw new IllegalArgumentException ("Parent already registered");
		}

		this.parents.put (child, parent);
		this.children.put (parent, child);
	}

	/**
	 * Get the <code>Set</code> of registered <code>ActivityType</code> objects.
	 *
	 * @return The <code>Set</code> of <code>ActivityType</code> objects
	 */

	public Set<ActivityType> getRegisteredElements ()
	{
		return this.types.keySet ();
	}

	/**
	 * Get the <code>Set</code> of registered parent classes.
	 *
	 * @return The <code>Set</code> of registered parent classes
	 */

	public Set<Class<? extends ActivityGroup>> getRegisteredParents ()
	{
		return this.children.keySet ();
	}

	/**
	 * Get the <code>Set</code> of registered child classes.
	 *
	 * @return The <code>Set</code> of registered child classes
	 */

	public Set<Class<? extends ActivityGroupMember>> getRegisteredChildren ()
	{
		return this.parents.keySet ();
	}

	/**
	 * Get the implementation class which contains the <code>Activity</code>
	 * specific data for the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>, not null
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>
	 */

	public Class<? extends Activity> getElement (ActivityType type)
	{
		if (type == null)
		{
			this.log.error ("Attempting to get implementation mapping for a NULL ActivityType");
			throw new NullPointerException ();
		}

		return this.types.get (type);
	}

	/**
	 * Get the parent <code>Class</code> for the specified child
	 * <code>Class</code>.
	 *
	 * @param  child The child class
	 * @return       The parent class, or null if the child is not registered
	 */

	public Class<? extends ActivityGroup> getParent (ActivityGroupMember child)
	{
		return this.parents.get (child);
	}

	/**
	 * Get the child <code>Class</code> for the specified parent
	 * <code>Class</code>.
	 *
	 * @param  parent The parent class
	 * @return        The child class, or null if the parent is not registered
	 */

	public Class<? extends ActivityGroupMember> getChild (ActivityGroup parent)
	{
		return this.children.get (parent);
	}
}
