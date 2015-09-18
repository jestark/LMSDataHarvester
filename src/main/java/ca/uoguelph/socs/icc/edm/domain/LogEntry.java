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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
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

	/** The associated <code>LogReference</code> (<code>SubActivity</code>) */
	public static final Property<LogReference> REFERENCE;

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
		ACTION = Property.getInstance (Action.class, "action", Property.Flags.REQUIRED);
		ACTIVITY = Property.getInstance (Activity.class, "activity", Property.Flags.REQUIRED);
		COURSE = Property.getInstance (Course.class, "course", Property.Flags.REQUIRED);
		ENROLMENT = Property.getInstance (Enrolment.class, "enrolment", Property.Flags.REQUIRED);
		REFERENCE = Property.getInstance (LogReference.class, "reference", Property.Flags.MUTABLE);
		NETWORK = Property.getInstance (Network.class, "network", Property.Flags.REQUIRED);
		TIME = Property.getInstance (Date.class, "time", Property.Flags.REQUIRED);

		SELECTOR_ACTION = Selector.getInstance (ACTION, false);
		SELECTOR_COURSE = Selector.getInstance (COURSE, false);
		SELECTOR_NETWORK = Selector.getInstance (NETWORK, false);

		Definition.getBuilder (LogEntry.class, Element.class)
			.addProperty (COURSE, LogEntry::getCourse)
			.addProperty (TIME, LogEntry::getTime, LogEntry::setTime)
			.addRelationship (ACTION, LogEntry::getAction, LogEntry::setAction)
			.addRelationship (ACTIVITY, LogEntry::getActivity, LogEntry::setActivity)
			.addRelationship (ENROLMENT, LogEntry::getEnrolment, LogEntry::setEnrolment)
			.addRelationship (REFERENCE, LogEntry::getReference, LogEntry::setReference)
			.addRelationship (NETWORK, LogEntry::getNetwork, LogEntry::setNetwork)
			.addSelector (SELECTOR_ACTION)
			.addSelector (SELECTOR_COURSE)
			.addSelector (SELECTOR_NETWORK)
			.build ();
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DataStore</code>.
	 *
	 * @param  datastore             The <code>DataStore</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DataStore</code> is closed
	 * @throws IllegalStateException if the <code>DataStore</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DataStore</code> is
	 *                               immutable
	 */

	public static LogEntryBuilder builder (final DataStore datastore)
	{
		assert datastore != null : "datastore is NULL";

		return new LogEntryBuilder (datastore);
	}

	/**
	 * Get an instance of the <code>LogEntryBuilder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>LogEntryBuilder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static LogEntryBuilder builder (final DomainModel model)
	{
		if (model == null)
		{
			throw new NullPointerException ("model is NULL");
		}

		return LogEntry.builder (model.getDataStore ());
	}

	/**
	 * Compare two <code>LogEntry</code> instances and determine if they are
	 * equal.
	 * <p>
	 * <ul>
	 * <li>The <code>Action</code>
	 * <li>The <code>Activity</code>
	 * <li>The <code>Enrolment</code>
	 * <li>The <code>Network</code>
	 * <li>The <code>Reference</code>
	 * <li>The time
	 * <ul>
	 *
	 * @param  obj The <code>LogEntry</code> instance to compare with this one
	 *
	 * @return     <code>True</code> if the <code>LogEntry</code> instances
	 *             should be considered to be equal, <code>False</code>
	 *             otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof LogEntry)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.getAction (), ((LogEntry) obj).getAction ());
			ebuilder.append (this.getActivity (), ((LogEntry) obj).getActivity ());
			ebuilder.append (this.getEnrolment (), ((LogEntry) obj).getEnrolment ());
			ebuilder.append (this.getNetwork (), ((LogEntry) obj).getNetwork ());
			ebuilder.append (this.getTime (), ((LogEntry) obj).getTime ());
			ebuilder.append (this.getReference (), ((LogEntry) obj).getReference ());

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogEntry</code> instance.
	 * The hash code is computed based upon the following fields:
	 * <p>
	 * <ul>
	 * <li>The <code>Action</code>
	 * <li>The <code>Activity</code>
	 * <li>The <code>Enrolment</code>
	 * <li>The <code>Network</code>
	 * <li>The <code>Reference</code>
	 * <li>The time
	 * <ul>
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1093;
		final int mult = 887;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.getAction ());
		hbuilder.append (this.getActivity ());
		hbuilder.append (this.getEnrolment ());
		hbuilder.append (this.getNetwork ());
		hbuilder.append (this.getTime ());
		hbuilder.append (this.getReference ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogEntry</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("enrolment", this.getEnrolment ());
		builder.append ("action", this.getAction ());
		builder.append ("activity", this.getActivity ());
		builder.append ("network", this.getNetwork ());
		builder.append ("time", this.getTime ());
		builder.append ("subactivity", this.getReference ());

		return builder.toString ();
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
		assert datastore != null : "datastore is null";

		return LogEntry.builder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>LogEntry</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<LogEntry> getMetaData ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (LogEntry.class, this.getClass ());
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
	 * Get the reference to the <code>SubActivity</code> for the
	 * <code>LogEntry</code>.  Some <code>LogEntry</code> instances will record
	 * an <code>Action</code> upon a <code>SubActivity</code>, rather than the
	 * <code>Activity</code> itself.  This method gets the
	 * <code>LogReference</code> instance containing the
	 * </code>SubActivity</code>.
	 *
	 * @return The <code>LogReference</code> instance
	 */

	public abstract LogReference getReference ();

	/**
	 * Set the reference to the <code>SubActivity</code> to the
	 * <code>LogEntry</code>.  This method is intended to be used by a
	 * <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded, or by the <code>LogEntryBuilder</code> when the
	 * <code>LogEntry</code> is created.
	 *
	 * @param  reference The <code>LogReference</code> instance, not null
	 */

	protected abstract void setReference (LogReference reference);

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
