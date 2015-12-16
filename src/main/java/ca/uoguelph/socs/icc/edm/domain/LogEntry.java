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
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
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

	public static class Builder extends Element.Builder<LogEntry>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>Action</code> instances */
		private final Retriever<Action> actionRetriever;

		/** Helper to substitute <code>Action</code> instances */
		private final Retriever<Activity> activityRetriever;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** Helper to substitute <code>Network</code> instances */
		private final Retriever<Network> networkRetriever;

		/** The <code>DataStore</code> ID number for the <code>LogEntry</code> */
		private @Nullable Long id;

		/** The associated <code>Action</code> */
		private @Nullable Action action;

		/** The associated <code>Activity</code> */
		private @Nullable Activity activity;

		/** The associated <code>Enrolment</code> */
		private @Nullable Enrolment enrolment;

		/** The associated <code>Network</code> */
		private @Nullable Network network;

		/** The time that the entry was logged */
		private @Nullable Date time;

		/** The <code>LogReferenceBuilder</code> */
		private @Nullable LogReference.Builder referenceBuilder;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  definition         The <code>Definition</code>, not null
		 * @param  idGenerator        The <code>IdGenerator</code>, not null
		 * @param  logRetriever       <code>Retriever</code> for
		 *                            <code>LogEntry</code> instances, not null
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
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<LogEntry> logRetriever,
				final Retriever<Action> actionRetriever,
				final Retriever<Activity> activityRetriever,
				final Retriever<Enrolment> enrolmentRetriever,
				final Retriever<Network> networkRetriever)
		{
			super (model, definition, logRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert actionRetriever != null : "actionRetriever is NULL";
			assert activityRetriever != null : "activityRetriever is NULL";
			assert enrolmentRetriever != null : "enrolmentRetriever is NULL";
			assert networkRetriever != null : "networkRetriever is NULL";

			this.idGenerator = idGenerator;
			this.actionRetriever = actionRetriever;
			this.activityRetriever = activityRetriever;
			this.enrolmentRetriever = enrolmentRetriever;
			this.networkRetriever = networkRetriever;

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
		 * @return The new <code>LogEntry</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected LogEntry create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  entry The <code>LogEntry</code> to be inserted, not null
		 * @return       The <code>LogEntry</code> to be inserted
		 */

		@Override
		protected LogEntry preInsert (final LogEntry entry)
		{
			assert entry != null : "entry is NULL";

			this.log.debug ("Setting ID");
			entry.setId (this.idGenerator.nextId ());

			return entry;
		}

		/**
		 * Implementation of the post-Insert hook to create the associated
		 * <code>LogReference</code>, if necessary.
		 *
		 * @param  entry The <code>LogEntry</code> not null
		 * @return       The <code>LogEntry</code>
		 */

		@Override
		protected LogEntry postInsert (final LogEntry entry)
		{
			if (this.referenceBuilder != null)
			{
				this.referenceBuilder.setEntry (entry)
					.build ();
			}

			return entry;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

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
		 * @param  entry The <code>LogEntry</code>, not null
		 * @return       This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>LogEntry</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final LogEntry entry)
		{
			this.log.trace ("load: entry={}", entry);

			super.load (entry);

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
		 * Get the <code>ActivityReference</code> with which the
		 * <code>LogEntry</code> is associated.  This method exists for the
		 * benefit of the <code>LogEntry</code> implementation.
		 *
		 * @return the <code>ActivityReference</code>
		 */

		@CheckReturnValue
		protected ActivityReference getActivityReference ()
		{
			return this.activity.getReference ();
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>LogEntry</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the <code>Action</code> which was performed upon the logged
		 * activity.
		 *
		 * @return A reference to the logged <code>Action</code>
		 */

		@CheckReturnValue
		public final Action getAction ()
		{
			return this.action;
		}

		/**
		 * Set the <code>Action</code> which was performed upon the logged
		 * <code>Activity</code>.
		 *
		 * @param  action The <code>Action</code>, not null
		 * @return        This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Action</code> is not in
		 *                                  the <code>DataStore</code>
		 */

		public final Builder setAction (final Action action)
		{
			this.log.trace ("setAction: action={}", action);

			this.verifyRelationship (this.actionRetriever, action, "action");

			return this;
		}

		/**
		 * Get the <code>Activity</code> upon which the logged
		 * <code>Action</code> was performed.
		 *
		 * @return The associated <code>Activity</code>
		 */

		@CheckReturnValue
		public final Activity getActivity ()
		{
			return this.activity;
		}

		/**
		 * Set the <code>Activity</code> upon which the logged <code>Action</code>
		 * was performed.
		 *
		 * @param  activity The <code>Activity</code>, not null
		 * @return          This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Activity</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public final Builder setActivity (final Activity activity)
		{
			this.log.trace ("setActivity: activity={}", activity);

			this.verifyRelationship (this.activityRetriever, activity, "activity");

			return this;
		}

		/**
		 * Get the <code>Enrolment</code> instance for the user which performed
		 * the logged action.
		 *
		 * @return The associated <code>Enrolment</code>
		 */

		@CheckReturnValue
		public final Enrolment getEnrolment ()
		{
			return this.enrolment;
		}

		/**
		 * Set the <code>Enrolment</code> instance for the <code>User</code> which
		 * performed the logged action.
		 *
		 * @param  enrolment The <code>Enrolment</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>Enrolment</code> is not
		 *                                  in the <code>DataStore</code>
		 */

		public final Builder setEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("setEnrolment: enrolment={}", enrolment);

			this.enrolment = this.verifyRelationship (this.enrolmentRetriever, enrolment, "enrolment");

			return this;
		}

		/**
		 * Get the <code>Network</code> from which the logged
		 * <code>Action</code> originated.
		 *
		 * @return The associated <code>Network</code>
		 */

		@CheckReturnValue
		public final Network getNetwork ()
		{
			return this.network;
		}

		/**
		 * Set the <code>Network</code> from which the logged <code>Action</code>
		 * originated.
		 *
		 * @param  network The <code>Network</code>, not null
		 * @return         This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the specified
		 *                                  <code>Network</code> does not exist
		 *                                  in the <code>Datastore</code>
		 */

		public final Builder setNetwork (final Network network)
		{
			this.log.trace ("setNetwork: network={}", network);

			this.network = this.verifyRelationship (this.networkRetriever, network, "network");

			return this;
		}

		/**
		 * Get the <code>SubActivity</code> upon which the logged
		 * <code>Action</code> was performed.
		 *
		 * @return The associated <code>SubActivity</code>, may be null
		 */

		@CheckReturnValue
		public final SubActivity getSubActivity ()
		{
			return (this.referenceBuilder != null) ? this.referenceBuilder.getSubActivity () : null;
		}

		/**
		 * Set the <code>SubActivity</code> upon which the logged
		 * <code>Action</code> was performed.  The <code>Subactivity</code> is
		 * optional, so it may be null.
		 *
		 * @param  subActivity The <code>SubActivity</code>
		 * @return             This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>SubActivity</code> is
		 *                                  not in the <code>DataStore</code>
		 */

		public final Builder setSubActivity (final SubActivity subActivity)
		{
			this.log.trace ("setSubActivity: subActivity={}", subActivity);

			if (subActivity != null)
			{
				this.referenceBuilder = LogReference.builder (this.model, subActivity);
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

		@CheckReturnValue
		public final Date getTime ()
		{
			return (Date) this.time.clone ();
		}

		/**
		 * Set the time of the logged <code>Action</code>.
		 *
		 * @param  time The time
		 * @return      This <code>Builder</code>
		 */

		public final Builder setTime (final Date time)
		{
			this.log.trace ("setTime: time={}", time);

			this.time = (time == null) ? new Date () : (Date) time.clone ();

			return this;
		}
	}

	/**
	 * Dagger Component interface for creating <code>Builder</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@ElementScope
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {LogEntryBuilderModule.class})
	protected interface LogEntryComponent extends Element.ElementComponent<LogEntry>
	{
		/**
		 * Create the Builder instance.
		 *
		 * @return The <code>Builder</code>
		 */

		@Override
		public abstract Builder getBuilder ();
	}

	/**
	 * Dagger module for creating <code>Retriever</code> instances.  This module
	 * contains implementation-independent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class LogEntryModule extends Element.ElementModule<LogEntry> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {LogEntryModule.class, Action.ActionModule.class, Activity.ActivityModule.class,
		Enrolment.EnrolmentModule.class, Network.NetworkModule.class})
	public static final class LogEntryBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>LogEntryBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public LogEntryBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  generator          The <code>IdGenerator</code>, not null
		 * @param  retriever          The <code>Retriever</code>, not null
		 * @param  actionRetriever    <code>Retriever</code> for
		 *                            <code>Action</code> instances, not null
		 * @param  activityRetriever  <code>Retriever</code> for
		 *                            <code>Activity</code> instances, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  networkRetriever   <code>Retriever</code> for
		 *                            <code>Network</code> instances, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator generator,
				final @Named ("TableRetriever") Retriever<LogEntry> retriever,
				final @Named ("QueryRetriever") Retriever<Action> actionRetriever,
				final @Named ("TableRetriever") Retriever<Activity> activityRetriever,
				final @Named ("TableRetriever") Retriever<Enrolment> enrolmentRetriever,
				final @Named ("QueryRetriever") Retriever<Network> networkRetriever)
		{
			return new Builder (model, this.definition, generator, retriever, actionRetriever,
					activityRetriever, enrolmentRetriever, networkRetriever);
		}
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected abstract class Definition extends Element.Definition<LogEntry>
	{
		/** Method reference to the implementation constructor  */
		private final Function<LogEntry.Builder, LogEntry> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends LogEntry> impl,
				final Function<LogEntry.Builder, LogEntry> creator)
		{
			super (LogEntry.METADATA, impl);

			assert creator != null : "creator is NULL";
			this.creator = creator;
		}

		/**
		 * Create a new instance of the <code>Component</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param model The <code>DomainModel</code>, not null
		 * @return      The <code>Component</code>
		 */

		@Override
		protected LogEntry.LogEntryComponent getComponent (final DomainModel model)
		{
			return DaggerLogEntry_LogEntryComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (LogEntry.class, model))
				.logEntryBuilderModule (new LogEntryBuilderModule (this))
				.build ();
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
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The <code>Builder</code> instance
	 *
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

		return (LogEntry.Builder) model.getElementComponent (LogEntry.class)
			.getBuilder ();
	}

	/**
	 * Create the <code>LogEntry</code>.
	 */

	protected LogEntry ()
	{
		super ();
	}

	/**
	 * Create the <code>LogEntry</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected LogEntry (final Builder builder)
	{
		super (builder);

		this.setActivityReference (Preconditions.checkNotNull (builder.getActivityReference (), "activity"));
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
	 * Connect all of the relationships for this <code>LogEntry</code> instance.
	 * This method is intended to be used just after the <code>LogEntry</code>
	 * is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return LogEntry.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>LogEntry</code>
	 * instance.  This method is intended to be used just before the
	 * <code>LogEntry</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return LogEntry.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
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
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>LogEntry</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
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
