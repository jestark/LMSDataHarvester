/* Copyright (C) 2014, 2015, 2016 James E. Stark
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

import java.util.HashMap;
import java.util.Map;
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
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * An abstract representation of the relationship between a
 * <code>LogEntry</code> and a <code>SubActivity</code>.  This class acts as
 * the abstract base class for all of the logging related to sub-activities.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class LogReference extends Element
{
	/**
	 * Create new <code>LogReference</code> instances.  This is an internal class
	 * used to create <code>LogReference</code> instances.  Generally, it should
	 * only be used by the <code>LogEntryBuilder</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<LogReference>
	{
		/** Helper to substitute <code>LogEntry</code> instances*/
		private final Retriever<LogEntry> entryRetriever;

		/** Helper to substitute <code>SubActivity</code> instances*/
		private final Retriever<SubActivity> subActivityRetriever;

		/** The associated <code>LogEnty</code>*/
		private @Nullable LogEntry entry;

		/** The associated <code>SubActivity</code>*/
		private @Nullable SubActivity subActivity;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model                The <code>DomainModel</code>, not null
		 * @param  definition           The <code>Definition</code>, not null
		 * @param  refRetriever         <code>Retriever</code> for
		 *                              <code>LogReference</code> instances, not
		 *                              null
		 * @param  entryRetriever       <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 * @param  subActivityRetriever <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 */

		protected Builder (
				final DomainModel model,
				final Definition definition,
				final Retriever<LogReference> refRetriever,
				final Retriever<LogEntry> entryRetriever,
				final Retriever<SubActivity> subActivityRetriever)
		{
			super (model, definition, refRetriever);

			assert entryRetriever != null : "entryRetriever is NULL";

			this.entryRetriever = entryRetriever;
			this.subActivityRetriever = subActivityRetriever;

			this.entry = null;
			this.subActivity = null;
		}

		/**
		 * Create an instance of the <code>LogReference</code>.
		 *
		 * @return The new <code>LogReference</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected LogReference create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>LogReference</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

			this.entry = null;

			return this;
		}

		/**
		 * Load a <code>LogReference</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>LogReference</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  reference The <code>LogReference</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>LogReference</code> instance
		 *                                  to be loaded are not valid
		 */

		@Override
		public Builder load (final LogReference reference)
		{
			this.log.trace ("load: reference={}", reference);

			super.load (reference);

			if (! Objects.equals (this.getSubActivity (), reference.getSubActivity ()))
			{
				throw new IllegalArgumentException ("SubActivity instances are different");
			}

			this.setEntry (reference.getEntry ());

			return this;
		}

		/**
		 * Get the associated <code>LogEntry</code>.
		 *
		 * @return The associated <code>LogEntry</code>
		 */

		@CheckReturnValue
		public final LogEntry getEntry ()
		{
			return this.entry;
		}

		/**
		 * Set the associated <code>LogEntry</code>.
		 *
		 * @param  entry The <code>LogEntry</code>, not null
		 * @return       This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>LogEntry</code> is not
		 *                                  in the <code>DataStore</code>
		 * @throws IllegalArgumentException if the <code>LogEntry</code> already
		 *                                  has a <code>LogReference</code>
		 */

		public final Builder setEntry (final LogEntry entry)
		{
			this.log.trace ("setEntry: entry={}", entry);

			this.entry = this.verifyRelationship (this.entryRetriever, entry, "entry");

			Preconditions.checkArgument (this.entry.getReference () == null,
					"Entry already has a reference");

			return this;
		}

		/**
		 * Get the associated <code>SubActivity</code>.
		 *
		 * @return The associated <code>SubActivity</code>
		 */

		@CheckReturnValue
		public final SubActivity getSubActivity ()
		{
			return this.subActivity;
		}

		/**
		 * Set the referenced <code>SubActivity</code>.
		 *
		 * @param  subActivity The <code>SubActivity</code>, not null
		 * @return             This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the <code>SubActivity</code> is
		 *                                  not in the <code>DataStore</code>
		 */

		private final Builder setSubActivity (final SubActivity subActivity)
		{
			this.log.trace ("setSubActivity: subActivity={}", subActivity);

			this.subActivity = this.verifyRelationship (this.subActivityRetriever, subActivity, "subActivity");

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
	@Component (modules = {LogReferenceBuilderModule.class})
	protected interface LogReferenceComponent extends Element.ElementComponent<LogReference>
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
	public static final class LogReferenceModule extends Element.ElementModule<LogReference> {}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {LogReferenceModule.class, LogEntry.LogEntryModule.class, SubActivity.SubActivityModule.class})
	public static final class LogReferenceBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>LogReferenceBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public LogReferenceBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is NULL";
			this.definition = definition;
		}

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model                The <code>DomainModel</code>, not null
		 * @param  retriever            The <code>Retriever</code>, not null
		 * @param  entryRetriever       <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 * @param  subActivityRetriever <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final @Named ("TableRetriever") Retriever<LogReference> retriever,
				final @Named ("TableRetriever") Retriever<LogEntry> entryRetriever,
				final @Named ("TableRetriever") Retriever<SubActivity> subActivityRetriever)
		{
			return new Builder (model, this.definition, retriever, entryRetriever, subActivityRetriever);
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

	protected static abstract class Definition extends Element.Definition<LogReference>
	{
		/** Method reference to the implementation constructor  */
		private final Function<LogReference.Builder, LogReference> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends LogReference> impl,
				final Function<LogReference.Builder, LogReference> creator)
		{
			super (LogReference.METADATA, impl);

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
		protected LogReference.LogReferenceComponent getComponent (final DomainModel model)
		{
			return DaggerLogReference_LogReferenceComponent.builder ()
				.domainModelModule (new DomainModel.DomainModelModule (LogReference.class, model))
				.logReferenceBuilderModule (new LogReferenceBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** <code>SubActivity</code> to <code>LogReference</code> class mapping */
	private static final Map<Class<? extends SubActivity>, Class<? extends LogReference>> references;

	/** The <code>MetaData</code> for the <code>LogReference</code> */
	protected static final MetaData<LogReference> METADATA;

	/** The <code>DomainModel</code> which contains the <code>LogReference</code> */
	public static final Property<LogReference, DomainModel> MODEL;

	/** The associated <code>LogEntry</code>*/
	public static final Property<LogReference, LogEntry> ENTRY;

	/** The associated <code>SubActivity</code> */
	public static final Property<LogReference, SubActivity> SUBACTIVITY;

	/** Select the <code>LogReference</code> instance by its id */
	public static final Selector<LogReference> SELECTOR_ID;

	/** Select all of the <code>LogReference</code> instances */
	public static final Selector<LogReference> SELECTOR_ALL;

	/** The associated <code>LogEntry</code> */
	protected LogEntry entry;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>LogReference</code>.
	 */

	static
	{
		references = new HashMap<> ();

		MODEL = Property.of (LogReference.class, DomainModel.class, "domainmodel",
				LogReference::getDomainModel, LogReference::setDomainModel);

		ENTRY = Property.of (LogReference.class, LogEntry.class, "entry",
				LogReference::getEntry, LogReference::setEntry,
				Property.Flags.REQUIRED);

		SUBACTIVITY = Property.of (LogReference.class, SubActivity.class, "subactivity",
				LogReference::getSubActivity, LogReference::setSubActivity,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ENTRY);

		SELECTOR_ALL = Selector.builder (LogReference.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (LogReference.class)
			.addProperty (MODEL)
			.addRelationship (ENTRY, LogEntry.METADATA, LogEntry.REFERENCE)
			.addRelationship (SUBACTIVITY, SubActivity.METADATA, SubActivity.REFERENCES)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.build ();
	}

	/**
	 * Register an association between an <code>SubActivity</code>
	 * implementation class and a <code>LogReference</code> implementation
	 * class.
	 *
	 * @param  subactivity The <code>SubActivity</code> implementation, not null
	 * @param  log         The <code>LogReference</code> implementation, not null
	 */

	protected static void registerImplementation (final Class<? extends SubActivity> subactivity, final Class<? extends LogReference> log)
	{
		assert subactivity != null : "subactivity is NULL";
		assert log != null : "log is NULL";
		assert (! LogReference.references.containsKey (subactivity)) : "subactivity is already registered";

		LogReference.references.put (subactivity, log);
	}

	/**
	 * Get the <code>LogReference</code> implementation class which is
	 * associated with the specified <code>SubActivity</code> implementation
	 * class.
	 *
	 * @param  subactivity The <code>SubActivity</code> implementation class,
	 *                     not null
	 * @return             The <code>LogReference</code> implementation class
	 */

	public static Class<? extends LogReference> getLogClass (final Class<? extends SubActivity> subactivity)
	{
		assert subactivity != null : "subactivity is NULL";
		assert LogReference.references.containsKey (subactivity) : "subactivity is not registered";

		return LogReference.references.get (subactivity);
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.  The <code>Builder</code> will be initialized
	 * with the specified <code>SubActivity</code>.
	 *
	 * @param  model       The <code>DomainModel</code>, not null
	 * @param  subActivity The <code>SubActivity</code>, not null
	 * @return             The <code>Builder</code> instance
	 *
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	protected static Builder builder (final DomainModel model, final SubActivity subActivity)
	{
		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (subActivity, "subActivity");

		return ((LogReference.Builder) model.getElementComponent (LogReference.class,
					LogReference.getLogClass (subActivity.getClass ()))
			.getBuilder ())
			.setSubActivity (subActivity);
	}

	/**
	 * Create the <code>LogReference</code>.
	 */

	protected LogReference ()
	{
		super ();

		this.entry = null;
	}

	/**
	 * Create the <code>LogReference</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected LogReference (final Builder builder)
	{
		super (builder);

		this.entry = Preconditions.checkNotNull (builder.getEntry (), "entry");
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
			.add ("entry", this.getEntry ())
			.add ("subactivity", this.getSubActivity ());
	}

	/**
	 * Connect all of the relationships for this <code>LogReference</code>
	 * instance.  This method is intended to be used just after the
	 * <code>LogReference</code> is inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return LogReference.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>LogReference</code>
	 * instance.  This method is intended to be used just before the
	 * <code>LogReference</code> is removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return LogReference.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Determine if this <code>Element</code> depends on the specified
	 * <code>Element</code> class.  This method is used by
	 * <code>compareTo</code> to order different <code>Element</code> classes
	 * based on their dependencies.
	 *
	 * @param  element The <code>Element</code> implementation class, not null
	 * @return         <code>true</code> if this<code>Element</code> depends on
	 *                 the specified class, <code>false</code> otherwise
	 */

	@Override
	protected boolean isDependency (final Class <? extends Element> element)
	{
		return LogReference.METADATA.dependencies ()
			.anyMatch (d -> d.isAssignableFrom (element));
	}

	/**
	 * Compare two <code>LogReference</code> instances, based upon the
	 * associated <code>LogEntry</code> instances.
	 * <p>
	 * Note:  The result from <code>compareTo</code> may not agree with the
	 * result from <code>equals</code>.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>LogReference</code> instances
	 *                 are equal, less than 0 of the argument is is greater, and
	 *                 greater than 0 if the argument is less than the
	 *                 <code>LogReference</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof LogReference)
			{
				result = this.entry.compareTo (((LogReference) element).entry);
			}
			else
			{
				result = super.compareTo (element);
			}
		}

		return result;
	}

	/**
	 * Compare two <code>LogReference</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>LogReference</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>LogReference</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof LogReference)
			&& Objects.equals (this.getEntry (), ((LogReference) obj).getEntry ())
			&& Objects.equals (this.getSubActivity (), ((LogReference) obj).getSubActivity ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>LogReference</code>
	 * instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getEntry (), this.getSubActivity ());
	}

	/**
	 * Get a <code>Stream</code> containing all of the associated
	 * <code>Element</code> instances.
	 *
	 * @return The <code>Stream</code>
	 */

	@Override
	public Stream<Element> associations ()
	{
		return LogReference.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>LogReference</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogReference</code> instance
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
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>LogReference</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return LogReference.builder (Preconditions.checkNotNull (model, "model"), this.getSubActivity ())
			.load (this);
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>LogEntry</code>
	 * instance.  Since <code>LogReference</code> is dependent on the
	 * <code>LogEntry</code> instance for its <code>DataStore</code>
	 * identifier, the identifier from the associated <code>LogEntry</code>
	 * will be returned.
	 *
	 * @return A <code>Long</code> containing <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.entry.getId ();
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is a no-op as
	 * the associated <code>LogEntry</code> provides the ID.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
	}

	/**
	 * Get the parent <code>LogEntry</code> instance.
	 *
	 * @return The parent <code>LogEntry</code> instance
	 */

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	/**
	 * Set the reference to the parent <code>LogEntry</code>.  This method is
	 * intended to be used to initialize a new <code>Logreference</code>.
	 *
	 * @param  entry The parent <code>LogEntry</code> instance, not null
	 */

	protected void setEntry (final LogEntry entry)
	{
		assert entry != null : "entry is NULL";

		this.entry = entry;
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	public abstract SubActivity getSubActivity ();

	/**
	 * Set the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.  This method is intended to be used
	 * to initialize a new <code>LogReference</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code>, not null
	 */

	protected abstract void setSubActivity (SubActivity subactivity);
}
