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
import java.util.LinkedList;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
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
 * @version 1.1
 */

public abstract class AbstractActivity extends AbstractElement implements Activity
{
	/**
	 * Implementation of the <code>ActivityElementFactory</code> interface.  Allows
	 * the builders to create instances of the <code>Activity</code>.
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

		/**
		 * Add the specified <code>SubActivity</code> to the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity    The <code>Activity</code> to which the
		 *                     <code>SubActivity</code> is to be added, not null
		 * @param  subactivity The <code>SubActivity</code> to add to the
		 *                     <code>Activity</code>, not null
		 *
		 * @return             <code>True</code> if the <code>SubActivity</code>
		 *                     was successfully added to the <code>Actvity</code>,
		 *                     <code>False</code> otherwise
		 */

		@Override
		public final boolean addSubActivity (final Activity activity, final SubActivity subactivity)
		{
			assert activity instanceof AbstractActivity : "activity is not an instance of AbstractActivity";
			assert subactivity instanceof AbstractActivity : "subactivity is not an instance of AbstractActivity";
			assert AbstractActivity.getSubActivityClass (activity.getClass ()) == subactivity.getClass () : "subactivity is not a registered subactivity of activity";
			assert (activity == subactivity.getParent ()) : "the parent of subactivity is not equal to activity";

			return ((AbstractActivity) activity).addSubActivity (subactivity);
		}

		/**
		 * Remove the specified <code>SubActivity</code> from the specified
		 * <code>Activity</code>.
		 *
		 * @param  activity    The <code>Activity</code> from which the
		 *                     <code>SubActivity</code> is to be removed, not null
		 * @param  subactivity The <code>SubActivity</code> to remove from the
		 *                     <code>Activity</code>, not null
		 *
		 * @return             <code>True</code> if the <code>SubActivity</code>
		 *                     was successfully removed from the
		 *                     <code>Actvity</code>, <code>False</code>
		 *                     otherwise
		 */

		@Override
		public final boolean removeSubActivity (final Activity activity, final SubActivity subactivity)
		{
			assert activity instanceof AbstractActivity : "activity is not an instance of AbstractActivity";
			assert subactivity instanceof AbstractActivity : "subactivity is not an instance of AbstractActivity";
			assert AbstractActivity.getSubActivityClass (activity.getClass ()) == subactivity.getClass () : "subactivity is not a registered subactivity of activity";
			assert (activity == subactivity.getParent ()) : "the parent of subactivity is not equal to activity";

			return ((AbstractActivity) activity).removeSubActivity (subactivity);
		}
	}

	/** Mapping of <code>ActivityType</code> instances to implementation classes */
	private static final ActivityDataMap ACTIVITYIMPL;

	/** The primary key for the activity */
	private Long id;

	/** The <code>List</code> of <code>SubActivity</code> instances */
	private List<SubActivity> subactivities;

	/** The log entries associated with the activity */
	private List<LogEntry> log;

	static
	{
		ACTIVITYIMPL = new ActivityDataMap ();
	}

	/**
	 * Get the <code>Activity</code> implementation class which is associated with
	 * the specified <code>ActivityType</code>.
	 *
	 * @param  type The <code>ActivityType</code>
	 *
	 * @return      The <code>Activity </code> data class for the given
	 *              <code>ActivityType</code>, may be null
	 */

	public static final Class<? extends Activity> getActivityClass (final ActivityType type)
	{
		return AbstractActivity.ACTIVITYIMPL.getActivityClass (type);
	}

	/**
	 * Get the <code>SubActivity</code> implementation class which is associated
	 * with the specified <code>Activity</code> implementation class.
	 *
	 * @param  activity The <code>Activity</code> implementation class
	 *
	 * @return          The <code>SubActivity</code> implementation class, may be
	 *                  null
	 */

	public static final Class<? extends SubActivity> getSubActivityClass (final Class<? extends Activity> activity)
	{
		return AbstractActivity.ACTIVITYIMPL.getSubActivityClass (activity);
	}

	/**
	 * Register an association between an <code>ActivityType</code> and the class
	 * implementing the <code>Activity</code> interface for that
	 * <code>ActivityType</code>.
	 *
	 * @param  source A <code>String</code> representation of the
	 *                <code>ActivitySource</code>, not null
	 * @param  type   A <code>String</code> representation of the
	 *                <code>ActivityType</code>, not null
	 * @param  impl   The implementation class, not null
	 */

	protected static final void registerActivityClass (final String source, final String type, final Class<? extends Activity> impl)
	{
		assert source != null : "source is NULL";
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		AbstractActivity.ACTIVITYIMPL.registerActivityClass (source, type, impl);
	}

	/**
	 * Register an association between an <code>Activity</code> implementation
	 * class and a <code>SubActivity</code> implementation class.
	 *
	 * @param  activity    The <code>Activity</code> implementation, not null
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 */

	protected static final void registerSubActivityClass (final Class<? extends Activity> activity, final Class<? extends SubActivity> subactivity)
	{
		assert activity != null : "activity is NULL";
		assert subactivity != null : "subactivity is NULL";

		AbstractActivity.ACTIVITYIMPL.registerSubActivityClass (activity, subactivity);
	}

	/**
	 * Create the <code>AbstractActivity</code> with null values.
	 */

	protected AbstractActivity ()
	{
		this.id = null;

		this.log = new LinkedList<LogEntry> ();
		this.subactivities = new LinkedList<SubActivity> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Activity</code>
	 * instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
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

	/**
	 * Determine if there are <code>SubActivity</code> instances associated with
	 * the <code>Activity</code> instance.
	 *
	 * @return <code>True</code> if the <code>Activity</code> instance has
	 *         <code>SubActivity</code> instances associated with it.
	 *         <code>False</code> otherwise
	 */

	@Override
	public boolean hasSubActivities ()
	{
		return ! this.subactivities.isEmpty ();
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances associated
	 * with the <code>Actvity</code>.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		return new ArrayList<SubActivity> (this.subactivities);
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances for
	 * the <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>Activity</code> instance is loaded.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected void setSubActivities (final List<SubActivity> subactivities)
	{
		assert subactivities != null : "subactivities is NULL";

		this.subactivities = subactivities;
	}

	/**
	 * Add the specified <code>SubActivity</code> to the <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected boolean addSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		return this.subactivities.add (subactivity);
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code> otherwise
	 */

	protected boolean removeSubActivity (final SubActivity subactivity)
	{
		assert subactivity != null : "subactivity is NULL";

		return this.subactivities.remove (subactivity);
	}
}
