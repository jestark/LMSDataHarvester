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
import java.util.Objects;

import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>LogEntry</code> */
	protected static final MetaData<LogEntry> METADATA;

	/** The <code>DataStore</code> identifier of the <code>LogEntry</code> */
	public static final Property<LogEntry, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>LogEntry</code> */
	public static final Property<LogEntry, DomainModel> MODEL;

	/** The associated <code>Action</code> */
	public static final Property<LogEntry, Action> ACTION;

	/** The associated <code>Activity</code> */
	public static final Property<LogEntry, ActivityReference> ACTIVITY;

	/** The associated <code>Course</code> (read only) */
	public static final Property<LogEntry, Course> COURSE;

	/** The associated <code>Enrolment</code> */
	public static final Property<LogEntry, Enrolment> ENROLMENT;

	/** The associated <code>LogReference</code> (<code>SubActivity</code>) */
	public static final Property<LogEntry, LogReference> REFERENCE;

	/** The associated <code>Network</code> */
	public static final Property<LogEntry, Network> NETWORK;

	/** The time that the <code>LogEntry</code> was created */
	public static final Property<LogEntry, Date> TIME;

	/** Select the <code>LogEntry</code> instance by its id */
	public static final Selector<LogEntry> SELECTOR_ID;

	/** Select all of the <code>LogEntry</code> instances */
	public static final Selector<LogEntry> SELECTOR_ALL;

	/** Select all <code>LogEntry</code> instances by <code>Action</code> */
	public static final Selector<LogEntry> SELECTOR_ACTION;

	/** Select all <code>LogEntry</code> instances by <code>Course</code> */
	public static final Selector<LogEntry> SELECTOR_COURSE;

	/** Select all <code>LogEntry</code> instances by <code>Network</code> */
	public static final Selector<LogEntry> SELECTOR_NETWORK;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>LogEntry</code>.
	 */

	static
	{
		ID = Property.of (LogEntry.class, Long.class, "id");
		MODEL = Property.of (LogEntry.class, DomainModel.class, "domainmodel");
		ACTION = Property.of (LogEntry.class, Action.class, "action", Property.Flags.REQUIRED);
		ACTIVITY = Property.of (LogEntry.class, ActivityReference.class, "activity", Property.Flags.REQUIRED);
		COURSE = Property.of (LogEntry.class, Course.class, "course", Property.Flags.REQUIRED);
		ENROLMENT = Property.of (LogEntry.class, Enrolment.class, "enrolment", Property.Flags.REQUIRED);
		REFERENCE = Property.of (LogEntry.class, LogReference.class, "reference", Property.Flags.RECOMMENDED);
		NETWORK = Property.of (LogEntry.class, Network.class, "network", Property.Flags.REQUIRED);
		TIME = Property.of (LogEntry.class, Date.class, "time", Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (LogEntry.class, Selector.Cardinality.KEY, ID);
		SELECTOR_ALL = Selector.of (LogEntry.class, Selector.Cardinality.MULTIPLE, "all");
		SELECTOR_ACTION = Selector.of (LogEntry.class, Selector.Cardinality.MULTIPLE, ACTION);
		SELECTOR_COURSE = Selector.of (LogEntry.class, Selector.Cardinality.MULTIPLE, COURSE);
		SELECTOR_NETWORK = Selector.of (LogEntry.class, Selector.Cardinality.MULTIPLE, NETWORK);

		METADATA = MetaData.builder (LogEntry.class)
			.addProperty (ID, LogEntry::getId, LogEntry::setId)
			.addProperty (MODEL, LogEntry::getDomainModel, LogEntry::setDomainModel)
			.addProperty (ACTION, LogEntry::getAction, LogEntry::setAction)
			.addProperty (ACTIVITY, LogEntry::getActivityReference, LogEntry::setActivityReference)
			.addProperty (COURSE, LogEntry::getCourse)
			.addProperty (ENROLMENT, LogEntry::getEnrolment, LogEntry::setEnrolment)
			.addProperty (NETWORK, LogEntry::getNetwork, LogEntry::setNetwork)
			.addProperty (REFERENCE, LogEntry::getReference, LogEntry::setReference)
			.addProperty (TIME, LogEntry::getTime, LogEntry::setTime)
			.addRelationship (Action.METADATA, ACTION, SELECTOR_ACTION)
			.addRelationship (ActivityReference.METADATA, ACTIVITY, ActivityReference.LOGENTRIES)
			.addRelationship (Enrolment.METADATA, ENROLMENT, Enrolment.LOGENTRIES)
			.addRelationship (Network.METADATA, NETWORK, SELECTOR_NETWORK)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_ACTION)
			.addSelector (SELECTOR_COURSE)
			.addSelector (SELECTOR_NETWORK)
			.build ();
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
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>LogEntry</code>.
	 */

	protected LogEntry ()
	{
		super ();
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("enrolment", this.getEnrolment ())
			.add ("action", this.getAction ())
			.add ("activity", this.getActivity ())
			.add ("network", this.getNetwork ())
			.add ("time", this.getTime ())
			.add ("subactivity", this.getSubActivity ());
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
		return (obj == this) ? true : (obj instanceof LogEntry)
			&& Objects.equals (this.getAction (), ((LogEntry) obj).getAction ())
			&& Objects.equals (this.getActivity (), ((LogEntry) obj).getActivity ())
			&& Objects.equals (this.getEnrolment (), ((LogEntry) obj).getEnrolment ())
			&& Objects.equals (this.getNetwork (), ((LogEntry) obj).getNetwork ())
			&& Objects.equals (this.getTime (), ((LogEntry) obj).getTime ());
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
	 * <li>The time
	 * <ul>
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getAction (), this.getActivity (), this.getEnrolment (), this.getNetwork (), this.getTime ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>LogEntry</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogEntry</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return LogEntry.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return LogEntry.METADATA.selectors ();
	}

	/**
	 * Determine if the value contained in the <code>Element</code> represented
	 * by the specified <code>Property</code> has the specified value.  If the
	 * <code>Property</code> represents a singe value, then this method will be
	 * equivalent to calling the <code>equals</code> method on the value
	 * represented by the <code>Property</code>.  This method is equivalent to
	 * calling the <code>contains</code> method for <code>Property</code>
	 * instances that represent collections.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Property</code> equals/contains the specified value,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public <V> boolean hasValue (final Property<V> property, final V value)
	{
		return LogEntry.METADATA.getReference (property)
			.hasValue (this, value);
	}

	/**
	 * Get a <code>Stream</code> containing all of the values in this
	 * <code>Element</code> instance which are represented by the specified
	 * <code>Property</code>.  This method will return a <code>Stream</code>
	 * containing zero or more values.  For a single-valued
	 * <code>Property</code>, the returned <code>Stream</code> will contain
	 * exactly zero or one values.  An empty <code>Stream</code> will be
	 * returned if the associated value is null.  A <code>Stream</code>
	 * containing all of the values in the associated collection will be
	 * returned for multi-valued <code>Property</code> instances.
	 *
	 * @param  <V>      The type of the values in the <code>Stream</code>
	 * @param  property The <code>Property</code>, not null
	 *
	 * @return          The <code>Stream</code>
	 */

	@Override
	public <V> Stream<V> stream (final Property<V> property)
	{
		return LogEntry.METADATA.getReference (property)
			.stream (this);
	}

	/**
	 * Get an <code>LogEntryBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>LogEntryBuilder</code> on the specified <code>DomainModel</code> and
	 * initializes it with the contents of this <code>LogEntry</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>LogEntryBuilder</code>
	 */

	@Override
	public LogEntryBuilder getBuilder (final DomainModel model)
	{
		return LogEntry.builder (Preconditions.checkNotNull (model, "model"))
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
	 * <code>Activity</code>.  This method is intended to be used to initialize
	 * a new <code>LogEntry</code> instance.
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
	 * Get the <code>ActivityReference</code> for the <code>Activity</code>
	 * upon which the logged <code>Action</code> was performed.
	 *
	 * @return The <code>ActivityReference</code>
	 */

	protected abstract ActivityReference getActivityReference ();

	/**
	 * Set the <code>ActivityReference</code> upon which the logged action was
	 * performed.  This method is intended to be used  to initialize a new
	 * <code>LogEntry</code> instance.
	 *
	 * @param  activity The <code>ActivityReference</code>, not null
	 */

	protected abstract void setActivityReference (ActivityReference activity);

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
	 * performed the logged action.   This method is intended to be used to
	 * initialize a new <code>LogEntry</code> instance.
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
	 * originated.  This method is intended to be used to initialize a new
	 * <code>LogEntry</code> instance.
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

	@CheckReturnValue
	protected abstract LogReference getReference ();

	/**
	 * Set the reference to the <code>SubActivity</code> to the
	 * <code>LogEntry</code>.  This method is intended to be used to initialize
	 * a new <code>LogEntry</code> instance.
	 *
	 * @param  reference The <code>LogReference</code> instance
	 */

	protected abstract void setReference (@Nullable LogReference reference);

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	@CheckReturnValue
	public abstract SubActivity getSubActivity ();

	/**
	 * Get the time of the logged <code>Action</code>.
	 *
	 * @return A <code>Date</code> containing the logged time
	 */

	public abstract Date getTime();

	/**
	 * Set the time of the logged <code>Action</code>.  This method is intended
	 * to be used to initialize a new <code>LogEntry</code>instance.
	 *
	 * @param  time The time, not null
	 */

	protected abstract void setTime (Date time);
}
