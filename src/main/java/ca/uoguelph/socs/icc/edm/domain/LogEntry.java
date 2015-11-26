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
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
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
 * @version 1.0
 */

public abstract class LogEntry extends Element
{
	/**
	 * Create new <code>LogEntry</code> instances.  This class implements
	 * <code>Builder</code>, adding the functionality required to create
	 * <code>LogEntry</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Builder implements Element.Builder<LogEntry>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>Action</code> instances */
		private final Retriever<Action> actionRetriever;

		/** Helper to substitute <code>Action</code> instances */
		private final Retriever<Activity> activityRetriever;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** Helper to substitute <code>Network</code> instances */
		private final Retriever<Network> networkRetriever;

		/** Helper to operate on <code>LogEntry</code> instances */
		private final Persister<LogEntry> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<LogEntry> supplier;

		/** The loaded or previously created <code>LogEntry</code> */
		private LogEntry entry;

		/** The <code>DataStore</code> ID number for the <code>LogEntry</code> */
		private Long id;

		/** The associated <code>Action</code> */
		private Action action;

		/** The associated <code>Activity</code> */
		private Activity activity;

		/** The associated <code>Enrolment</code> */
		private Enrolment enrolment;

		/** The associated <code>Network</code> */
		private Network network;

		/** The time that the entry was logged */
		private Date time;

		/** The <code>LogReferenceBuilder</code> */
		private LogReference.Builder referenceBuilder;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier           Method reference to the constructor of the
		 *                            implementation class, not null
		 * @param  persister          The <code>Persister</code> used to store the
		 *                            <code>LogEntry</code>, not null
		 * @param  actionRetriever    <code>Retriever</code> for
		 *                            <code>Action</code> instances, not null
		 * @param  activityRetriever  <code>Retriever</code> for
		 *                            <code>Activity</code> instances, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  networkRetriever   <code>Retriever</code> for
		 *                            <code>Network</code> instances, not null
		 */

		protected Builder (
				final Supplier<LogEntry> supplier,
				final Persister<LogEntry> persister,
				final Retriever<Action> actionRetriever,
				final Retriever<Activity> activityRetriever,
				final Retriever<Enrolment> enrolmentRetriever,
				final Retriever<Network> networkRetriever)
		{
			assert persister != null : "persister is NULL";
			assert actionRetriever != null : "actionRetriever is NULL";
			assert activityRetriever != null : "activityRetriever is NULL";
			assert enrolmentRetriever != null : "enrolmentRetriever is NULL";
			assert networkRetriever != null : "networkRetriever is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.actionRetriever = actionRetriever;
			this.activityRetriever = activityRetriever;
			this.enrolmentRetriever = enrolmentRetriever;
			this.networkRetriever = networkRetriever;
			this.persister = persister;
			this.supplier = supplier;

			this.entry = null;
			this.id = null;
			this.action = null;
			this.activity = null;
			this.enrolment = null;
			this.network = null;
			this.time = null;
			this.referenceBuilder = null;
		}

		/**
		 * Create an instance of the <code>LogEntry</code>.
		 *
		 * @return                       The new <code>LogEntry</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public LogEntry build ()
		{
			this.log.trace ("build:");

			if (this.action == null)
			{
				this.log.error ("Attempting to create an LogEntry without an Action");
				throw new IllegalStateException ("action is NULL");
			}

			if (this.activity == null)
			{
				this.log.error ("Attempting to create an LogEntry without an Activity");
				throw new IllegalStateException ("activity is NULL");
			}

			if (this.enrolment == null)
			{
				this.log.error ("Attempting to create an LogEntry without an Enrolment");
				throw new IllegalStateException ("enrolment is NULL");
			}

			if (this.time == null)
			{
				this.log.error ("Attempting to create an LogEntry without a time");
				throw new IllegalStateException ("time is NULL");
			}

			LogEntry result = this.supplier.get ();
			result.setId (this.id);
			result.setAction (this.action);
			result.setActivityReference (this.activity.getReference ());
			result.setEnrolment (this.enrolment);
			result.setNetwork (this.network);
			result.setTime (this.time);

			this.entry = this.persister.insert (this.entry, result);

			// Create the reference
			if (this.referenceBuilder != null)
			{
				this.referenceBuilder.setEntry (this.entry)
					.build ();
			}

			return this.entry;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.id = null;
			this.action = null;
			this.activity = null;
			this.enrolment = null;
			this.network = null;
			this.time = null;
			this.referenceBuilder = null;

			return this;
		}

		/**
		 * Load a <code>LogEntry</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>LogEntry</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  entry                    The <code>LogEntry</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>LogEntry</code> instance to be
		 *                                  loaded are not valid
		 */

		public Builder load (final LogEntry entry)
		{
			this.log.trace ("load: entry={}", entry);

			if (entry == null)
			{
				this.log.error ("Attempting to load a NULL LogEntry");
				throw new NullPointerException ();
			}

			this.entry = entry;
			this.id = entry.getId ();
			this.setAction (entry.getAction ());
			this.setActivity (entry.getActivity ());
			this.setEnrolment (entry.getEnrolment ());
			this.setNetwork (entry.getNetwork ());
			this.setTime (entry.getTime ());

			if (entry.getReference () != null)
			{
				this.referenceBuilder = null;
			}

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>LogEntry</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the <code>Action</code> which was performed upon the logged
		 * activity.
		 *
		 * @return A reference to the logged <code>Action</code>
		 */

		public Action getAction ()
		{
			return this.action;
		}

		/**
		 * Set the <code>Action</code> which was performed upon the logged
		 * <code>Activity</code>.
		 *
		 * @param  action                   The <code>Action</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>Action</code> is not in
		 *                                  the <code>DataStore</code>
		 */

		public Builder setAction (final Action action)
		{
			this.log.trace ("setAction: action={}", action);

			if (action == null)
			{
				this.log.error ("Action is NULL");
				throw new NullPointerException ("Action is NULL");
			}

			this.action = this.actionRetriever.fetch (action);

			if (this.action == null)
			{
				this.log.error ("The specified Action does not exist in the DataStore");
				throw new IllegalArgumentException ("Action is not in the DataStore");
			}

			return this;
		}

		/**
		 * Get the <code>Activity</code> upon which the logged
		 * <code>Action</code> was performed.
		 *
		 * @return The associated <code>Activity</code>
		 */

		public Activity getActivity ()
		{
			return this.activity;
		}

		/**
		 * Set the <code>Activity</code> upon which the logged <code>Action</code>
		 * was performed.
		 *
		 * @param  activity                 The <code>Activity</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public Builder setActivity (final Activity activity)
		{
			this.log.trace ("setActivity: activity={}", activity);

			if (activity == null)
			{
				this.log.error ("Activity is NULL");
				throw new NullPointerException ("Activity is NULL");
			}

			this.activity = activityRetriever.fetch (activity);

			if (this.activity == null)
			{
				this.log.error ("The specified Activity does not exist in the DataStore");
				throw new IllegalArgumentException ("Activity is not in the DataStore");
			}

			return this;
		}

		/**
		 * Get the <code>Enrolment</code> instance for the user which performed
		 * the logged action.
		 *
		 * @return The associated <code>Enrolment</code>
		 */

		public Enrolment getEnrolment ()
		{
			return this.enrolment;
		}

		/**
		 * Set the <code>Enrolment</code> instance for the <code>User</code> which
		 * performed the logged action.
		 *
		 * @param  enrolment                The <code>Enrolment</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>Enrolment</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public Builder setEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("setEnrolment: enrolment={}", enrolment);

			if (enrolment == null)
			{
				this.log.error ("Enrolment is NULL");
				throw new NullPointerException ("Enrolment is NULL");
			}

			this.enrolment = this.enrolmentRetriever.fetch (enrolment);

			if (this.enrolment == null)
			{
				this.log.error ("The specified Enrolment does not exist in the DataStore");
				throw new IllegalArgumentException ("Enrolment is not in the DataStore");
			}

			return this;
		}

		/**
		 * Get the <code>Network</code> from which the logged
		 * <code>Action</code> originated.
		 *
		 * @return The associated <code>Network</code>
		 */

		public Network getNetwork ()
		{
			return this.network;
		}

		/**
		 * Set the <code>Network</code> from which the logged <code>Action</code>
		 * originated.
		 *
		 * @param  network                  The <code>Network</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the specified
		 *                                  <code>Network</code> does not exist
		 *                                  in the <code>Datastore</code>
		 */

		public Builder setNetwork (final Network network)
		{
			this.log.trace ("setNetwork: network={}", network);

			if (network == null)
			{
				throw new NullPointerException ();
			}

			this.network = this.networkRetriever.fetch (network);

			if (this.network == null)
			{
				this.log.error ("The specified Network does not exist in the DataStore");
				throw new IllegalArgumentException ("Network is not in the DataStore");
			}

			return this;
		}

		/**
		 * Get the <code>SubActivity</code> upon which the logged
		 * <code>Action</code> was performed.
		 *
		 * @return The associated <code>SubActivity</code>, may be null
		 */

		public SubActivity getSubActivity ()
		{
			return (this.referenceBuilder != null) ? this.referenceBuilder.getSubActivity () : null;
		}

		/**
		 * Set the <code>SubActivity</code> upon which the logged
		 * <code>Action</code> was performed.  The <code>Subactivity</code> is
		 * optional, so it may be null.
		 *
		 * @param  subActivity              The <code>SubActivity</code>
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>SubActivity</code> is
		 *                                  not in the <code>DataStore</code>
		 */

		public Builder setSubActivity (final SubActivity subActivity)
		{
			this.log.trace ("setSubActivity: subActivity={}", subActivity);

			if (subActivity != null)
			{
	//			this.subActivity = DataStoreProxy.getInstance (SubActivity.class,
	//					subActivity.getClass (),
	//					SubActivity.SELECTOR_ID,
	//					datastore)
	//				.fetch (subActivity);
	//
	//			if (this.subActivity == null)
	//			{
	//				this.log.error ("The specified SubActivity does not exist in the DataStore");
	//				throw new IllegalArgumentException ("SubActivity is not in the DataStore");
	//			}
			}
			else
			{
				this.referenceBuilder = null;
			}

			return this;
		}

		/**
		 * Get the time of the logged <code>Action</code>.
		 *
		 * @return The associated time
		 */

		public Date getTime ()
		{
			return (Date) this.time.clone ();
		}

		/**
		 * Set the time of the logged <code>Action</code>.
		 *
		 * @param  time The time
		 *
		 * @return      This <code>Builder</code>
		 */

		public Builder setTime (final Date time)
		{
			this.log.trace ("setTime: time={}", time);

			if (time == null)
			{
				this.time = new Date ();
			}
			else
			{
				this.time = (Date) time.clone ();
			}

			return this;
		}
	}

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
		ID = Property.of (LogEntry.class, Long.class, "id",
				LogEntry::getId, LogEntry::setId);

		MODEL = Property.of (LogEntry.class, DomainModel.class, "domainmodel",
				LogEntry::getDomainModel, LogEntry::setDomainModel);

		ACTION = Property.of (LogEntry.class, Action.class, "action",
				LogEntry::getAction, LogEntry::setAction,
				Property.Flags.REQUIRED);

		ACTIVITY = Property.of (LogEntry.class, ActivityReference.class, "activity",
				LogEntry::getActivityReference, LogEntry::setActivityReference,
				Property.Flags.REQUIRED);

		COURSE = Property.of (LogEntry.class, Course.class, "course",
				LogEntry::getCourse);

		ENROLMENT = Property.of (LogEntry.class, Enrolment.class, "enrolment",
				LogEntry::getEnrolment, LogEntry::setEnrolment,
				Property.Flags.REQUIRED);

		NETWORK = Property.of (LogEntry.class, Network.class, "network",
				LogEntry::getNetwork, LogEntry::setNetwork,
				Property.Flags.REQUIRED);

		REFERENCE = Property.of (LogEntry.class, LogReference.class, "reference",
				LogEntry::getReference, LogEntry::setReference,
				Property.Flags.RECOMMENDED);

		TIME = Property.of (LogEntry.class, Date.class, "time",
				LogEntry::getTime, LogEntry::setTime,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_ACTION = Selector.of (Selector.Cardinality.MULTIPLE, ACTION);
		SELECTOR_COURSE = Selector.of (Selector.Cardinality.MULTIPLE, COURSE);
		SELECTOR_NETWORK = Selector.of (Selector.Cardinality.MULTIPLE, NETWORK);

		SELECTOR_ALL = Selector.builder (LogEntry.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (LogEntry.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (REFERENCE)
			.addProperty (TIME)
			.addRelationship (ACTION, Action.METADATA, SELECTOR_ACTION)
			.addRelationship (ACTIVITY, ActivityReference.METADATA, ActivityReference.LOGENTRIES)
			.addRelationship (ENROLMENT, Enrolment.METADATA, Enrolment.LOGENTRIES)
			.addRelationship (NETWORK, Network.METADATA, SELECTOR_NETWORK)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>Builder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>LogEntry</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
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
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>LogEntry</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
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
