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

import java.util.Date;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of an entry in the log.  An instance of the
 * <code>LogEntry</code> interface contains information about an
 * <code>Action</code> which was performed against an <code>Activity</code>, by
 * a particular <code>User</code> (represented by their <code>Enrolment</code>
 * instance), along with the time that the <code>Action</code> was performed
 * and, optionally, the remote IP address.
 * <p>
 * Within the domain model, <code>LogEntry</code> is a leaf level interface.
 * No instances of any other domain model interface depend upon the existence
 * of an instance of the <code>LogEntry</code> interface.  Instances of the
 * <code>LogEntry</code> interface have strong dependencies upon instances of
 * the <code>Action</code>, <code>Activity</code> and <code>Enrolment</code>
 * interfaces.  If an instance of any of these interfaces deleted, then all of
 * the associated instances of the <code>LogEntry</code> interface must be
 * deleted as well.
 * <p>
 * Once created, <code>LogEntry</code> instances are immutable.
 *
 * @author  James E. Stark
 * @version 1.1
 * @see     LogEntryBuilder
 * @see     LogEntryLoader
 */

public abstract class LogEntry extends Element
{
	/** The associated <code>Action</code> */
	public static final Property<Action> ACTION;

	/** The associated <code>Activity</code> */
	public static final Property<Activity> ACTIVITY;

	/** The associated <code>Course</code> (read only) */
	public static final Property<Course> COURSE;

	/** The associated <code>Enrolment</code> */
	public static final Property<Enrolment> ENROLMENT;

	/** The associated <code>Network</code> */
	public static final Property<Network> NETWORK;

	/** The time that the <code>LogEntry</code> was created */
	public static final Property<Date> TIME;

	/** Select all <code>LogEntry</code> instances by <code>Action</code> */
	public static final Selector SELECTOR_ACTION;

	/** Select all <code>LogEntry</code> instances by <code>Course</code> */
	public static final Selector SELECTOR_COURSE;

	/** Select all <code>LogEntry</code> instances by <code>Network</code> */
	public static final Selector SELECTOR_NETWORK;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>LogEntry</code>.
	 */

	static
	{
		ACTION = Property.getInstance (Action.class, "action", false, true);
		ACTIVITY = Property.getInstance (Activity.class, "activity", false, true);
		COURSE = Property.getInstance (Course.class, "course", false, true);
		ENROLMENT = Property.getInstance (Enrolment.class, "enrolment", false, true);
		NETWORK = Property.getInstance (Network.class, "network", false, true);
		TIME = Property.getInstance (Date.class, "time", false, true);

		SELECTOR_ACTION = Selector.getInstance (ACTION, false);
		SELECTOR_COURSE = Selector.getInstance (COURSE, false);
		SELECTOR_NETWORK = Selector.getInstance (NETWORK, false);

		Definition.getBuilder (LogEntry.class, Element.class)
			.addProperty (COURSE, LogEntry::getCourse)
			.addProperty (TIME, LogEntry::getTime, LogEntry::setTime)
			.addRelationship (ACTION, LogEntry::getAction, LogEntry::setAction)
			.addRelationship (ACTIVITY, LogEntry::getActivity, LogEntry::setActivity)
			.addRelationship (ENROLMENT, LogEntry::getEnrolment, LogEntry::setEnrolment)
			.addRelationship (NETWORK, LogEntry::getNetwork, LogEntry::setNetwork)
			.addSelector (SELECTOR_ACTION)
			.addSelector (SELECTOR_COURSE)
			.addSelector (SELECTOR_NETWORK)
			.build ();
	}

	/**
	 * Get an <code>LogEntryBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>LogEntryBuilder</code> on the specified <code>DataStore</code> and
	 * initializes it with the contents of this <code>LogEntry</code> instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>LogEntryBuilder</code>
	 */

	@Override
	public LogEntryBuilder getBuilder (final DataStore datastore)
	{
		if (datastore == null)
		{
			throw new NullPointerException ();
		}

		return new LogEntryBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.
	 *
	 * @return A reference to the logged <code>Action</code>
	 */

	public abstract Action getAction();

	/**
	 * Set the <code>Action</code> which was performed upon the logged
	 * <code>Activity</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  action The <code>Action</code>, not null
	 */

	protected abstract void setAction (Action action);

	/**
	 * Get the <code>Activity</code> upon which the logged <code>Action</code>
	 * was performed.
	 *
	 * @return A reference to the associated <code>Activity</code>
	 */

	public abstract Activity getActivity();

	/**
	 * Set the <code>Activity</code>  upon which the logged action was
	 * performed.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  activity The <code>Activity</code>, not null
	 */

	protected abstract void setActivity (Activity activity);

	/**
	 * Get the <code>Course</code> for which the <code>Action</code> was
	 * logged.
	 *
	 * @return A reference to the associated <code>Course</code>
	 */

	public abstract Course getCourse();

	/**
	 * Get the <code>Enrolment</code> instance for the user which performed the
	 * logged <code>Action</code>.
	 *
	 * @return A reference to the associated <code>Enrolment</code>
	 */

	public abstract Enrolment getEnrolment();

	/**
	 * Set the <code>Enrolment</code> instance for the <code>User</code> which
	 * performed the logged action.   This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  enrolment The <code>Enrolment</code>, not null
	 */

	protected abstract void setEnrolment (Enrolment enrolment);

	/**
	 * Get the <code>Network</code> from which the logged <code>Action</code>
	 * originated.
	 *
	 * @return The <code>Network</code>
	 */

	public abstract Network getNetwork ();

	/**
	 * Set the <code>Network</code> from which the logged <code>Action</code>
	 * originated.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  network The <code>Network</code>, not null
	 */

	protected abstract void setNetwork (Network network);

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	public abstract SubActivity getSubActivity ();

	/**
	 * Get the time of the logged <code>Action</code>.
	 *
	 * @return A <code>Date</code> containing the logged time
	 */

	public abstract Date getTime();

	/**
	 * Set the time of the logged <code>Action</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the <code>LogEntry</code>
	 * instance is loaded.
	 *
	 * @param  time The time, not null
	 */

	protected abstract void setTime (Date time);
}
