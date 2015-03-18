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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.util.List;

import java.util.ArrayList;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractActivityElementFactory;

/**
 * Abstract base class for all of the <code>Activity</code> implementations.
 * Every class that implements the <code>Activity</code> interface must extend
 * this class.  This class contains the components that are common to all
 * <code>Activity</code> implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractActivity extends AbstractElement implements Activity
{
	/**
	 * Implementation of the <code>ActivityElementFactory</code> interface.  Allows
	 * the builders to create instances of <code>ActivityInstance</code>.
	 */

	protected static abstract class Factory extends AbstractElement.Factory<Activity> implements AbstractActivityElementFactory
	{
		/**
		 * Add the specified <code>LogEntry</code> to the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> to which the
		 *                  <code>LogEntry</code> is to be added, not null
		 * @param  entry    The <code>LogEntry</code> to add to the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>LogEntry</code> was
		 *                  successfully added to the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		@Override
		public boolean addLogEntry (final Activity activity, final LogEntry entry)
		{
			assert activity instanceof AbstractActivity : "activity is not an instance of AbstractActivity";
			assert entry != null : "entry is NULL";
			
			return ((AbstractActivity) activity).addLog (entry);
		}

		/**
		 * Remove the specified <code>LogEntry</code> from the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity The <code>Activity</code> from which the
		 *                  <code>LogEntry</code> is to be removed, not null
		 * @param  entry    The <code>LogEntry</code> to remove from the
		 *                  <code>Activity</code>, not null
		 *
		 * @return          <code>True</code> if the <code>LogEntry</code> was
		 *                  successfully removed from the <code>Activity</code>,
		 *                  <code>False</code> otherwise
		 */

		@Override
		public boolean removeLogEntry (final Activity activity, final LogEntry entry)
		{
			assert activity instanceof AbstractActivity : "activity is not an instance of AbstractActivity";
			assert entry != null : "entry is NULL";

			return ((AbstractActivity) activity).removeLog (entry);
		}
	}

	/** Mapping of <code>ActivityType</code> instances ti implementation classes */
	private static final ActivityDataMap implementations;

	/** The primary key for the activity */
	private Long id;

	/** The log entries associated with the activity*/
	private List<LogEntry> log;

	static
	{
		implementations = new ActivityDataMap ();
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

	public static final Class<? extends Activity> getImplClass (final ActivityType type)
	{
		return AbstractActivity.implementations.getElement (type);
	}

	/**
	 * Get the parent <code>Class</code> for the specified child
	 * <code>Class</code>.
	 *
	 * @param  child The child class
	 *
	 * @return       The parent class, or null if the child is not registered
	 */

	public static final Class<? extends ActivityGroup> getParent (final Class<? extends ActivityGroupMember> child)
	{
		return AbstractActivity.implementations.getParent (child);
	}

	/**
	 * Get the child <code>Class</code> for the specified parent
	 * <code>Class</code>.
	 *
	 * @param  parent The parent class
	 *
	 * @return        The child class, or null if the parent is not registered
	 */

	public static final Class<? extends ActivityGroupMember> getChild (final Class<? extends ActivityGroup> parent)
	{
		return AbstractActivity.implementations.getChild (parent);
	}

	/**
	 * Register a <code>ActivityType</code> to implementation class mapping.  
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static final void registerImplClass (final String source, final String type, final Class<? extends Activity> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		
		AbstractActivity.implementations.registerElement (source, type, impl);
	}

	/**
	 * Register a parent child relationship between the classes implementing the
	 * sub-activities.
	 *
	 * @param  parent The parent class, not null
	 * @param  child  The child class, not null
	 */

	protected static final void registerRelationship (final Class<? extends ActivityGroup> parent, final Class<? extends ActivityGroupMember> child)
	{
		assert parent != null : "parent is NULL";
		assert child != null : "child is NULL";

		AbstractActivity.implementations.registerRelationship (parent, child);
	}

	/**
	 * Create the <code>AbstractActivity</code> with null values.
	 */

	protected AbstractActivity ()
	{
		this.id = null;

		this.log = new ArrayList<LogEntry> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded, or by the <code>ActivityBuilder</code> implementation to set the
	 * <code>DataStore</code> identifier, prior to storing a new
	 * <code>Activity</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances which
	 * act upon the <code>Activity</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	@Override
	public List<LogEntry> getLog ()
	{
		return new ArrayList<LogEntry> (this.log);
	}

	/**
	 * Initialize the <code>List</code> of <code>LogEntry</code> instances
	 * associated with the <code>Activity</code> instance.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  log The <code>List</code> of <code>LogEntry</code> instances, not
	 *             null
	 */

	protected void setLog (final List<LogEntry> log)
	{
		assert log != null : "log is NULL";
		
		this.log = log;
	}

	/**
	 * Add the specified <code>LogEntry</code> to the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	protected boolean addLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";
		
		return this.log.add (entry);
	}

	/**
	 * Remove the specified <code>LogEntry</code> from the specified
	 * <code>Activity</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>LogEntry</code> was
	 *                  successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeLog (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";
		
		return this.log.remove (entry);
	}
}
