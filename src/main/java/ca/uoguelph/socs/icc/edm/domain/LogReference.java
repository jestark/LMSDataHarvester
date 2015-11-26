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

import java.util.HashMap;
import java.util.Map;
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

	public static final class Builder implements Element.Builder<LogReference>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>LogEntry</code> instances*/
		private final Retriever<LogEntry> entryRetriever;

		/** Helper to substitute <code>SubActivity</code> instances*/
		private final Retriever<SubActivity> subActivityRetriever;

		/** Helper to operate on <code>LogReference</code> instances */
		private final Persister<LogReference> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<LogReference> supplier;

		/** The loaded of previously created <code>LogReference</code> */
		private LogReference reference;

		/** The associated <code>LogEnty</code>*/
		private LogEntry entry;

		/** The associated <code>SubActivity</code>*/
		private SubActivity subActivity;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  datastore The <code>DataStore</code>, not null
		 */

		protected Builder (
				final Supplier<LogReference> supplier,
				final Persister<LogReference> persister,
				final Retriever<LogEntry> entryRetriever,
				final Retriever<SubActivity> subActivityRetriever)
		{
			assert persister != null : "persister is NULL";
			assert entryRetriever != null : "entryRetriever is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.entryRetriever = entryRetriever;
			this.subActivityRetriever = subActivityRetriever;
			this.persister = persister;
			this.supplier = supplier;

			this.reference = null;
			this.entry = null;
			this.subActivity = null;
		}

		/**
		 * Create an instance of the <code>LogReference</code>.
		 *
		 * @return                       The new <code>LogReference</code>
		 *                               instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public LogReference build ()
		{
			this.log.trace ("build:");

			if (this.entry == null)
			{
				this.log.error ("Attempting to create an LogReference without a LogEntry");
				throw new IllegalStateException ("entry is NULL");
			}

			if (this.subActivity == null)
			{
				this.log.error ("Attempting to create an LogReference without a SubActivity");
				throw new IllegalStateException ("subActivity is NULL");
			}

			LogReference result = this.supplier.get ();
			result.setEntry (this.entry);
			result.setSubActivity (this.subActivity);

			this.reference = persister.insert (this.reference, result);

			return this.reference;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>LogReference</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.reference = null;
			this.entry = null;
			this.subActivity = null;

			return this;
		}

		/**
		 * Load a <code>LogReference</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>LogReference</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  reference                The <code>LogReference</code>, not
		 *                                  null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>LogReference</code> instance
		 *                                  to be loaded are not valid
		 */

		public Builder load (final LogReference reference)
		{
			this.log.trace ("load: reference={}", reference);

			if (reference == null)
			{
				this.log.error ("Attempting to load a NULL LogReference");
				throw new NullPointerException ();
			}

			this.reference = reference;
			this.setEntry (reference.getEntry ());
			this.setSubActivity (reference.getSubActivity ());

			return this;
		}

		/**
		 * Get the associated <code>LogEntry</code>.
		 *
		 * @return The associated <code>LogEntry</code>
		 */

		public LogEntry getEntry ()
		{
			return this.entry;
		}

		/**
		 * Set the associated <code>LogEntry</code>.
		 *
		 * @param  entry                    The <code>LogEntry</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>LogEntry</code> is not
		 *                                  in the <code>DataStore</code>
		 * @throws IllegalArgumentException if the <code>LogEntry</code> already
		 *                                  has a <code>LogReference</code>
		 */

		public Builder setEntry (final LogEntry entry)
		{
			this.log.trace ("setEntry: entry={}", entry);

			if (entry == null)
			{
				this.log.error ("entry is NULL");
				throw new NullPointerException ("entry is NULL");
			}

			this.entry = this.entryRetriever.fetch (entry);

			if (this.entry == null)
			{
				this.log.error ("The specified LogEntry does not exist in the DataStore");
				throw new IllegalArgumentException ("LogEntry is not in the DataStore");
			}

			if (this.entry.getReference () != null)
			{
				this.log.error ("The entry already has another reference assigned to it");
				throw new IllegalArgumentException ("Entry already has a reference");
			}

			return this;
		}

		/**
		 * Get the associated <code>SubActivity</code>.
		 *
		 * @return The associated <code>SubActivity</code>
		 */

		public SubActivity getSubActivity ()
		{
			return this.subActivity;
		}

		/**
		 * Set the referenced <code>SubActivity</code>.
		 *
		 * @param  subActivity              The <code>SubActivity</code>, not
		 *                                  null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException if the <code>SubActivity</code> is
		 *                                  not in the <code>DataStore</code>
		 */

		public Builder setSubActivity (final SubActivity subActivity)
		{
			this.log.trace ("setSubActivity: subActivity={}", subActivity);

			if (subActivity == null)
			{
				this.log.error ("subActivity is NULL");
				throw new NullPointerException ("subActivity is NULL");
			}

			this.subActivity = this.subActivityRetriever.fetch (subActivity);

			if (this.subActivity == null)
			{
				this.log.error ("The specified SubActivity does not exist in the DataStore");
				throw new IllegalArgumentException ("SubActivity is not in the DataStore");
			}

			return this;
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
	 *
	 * @return             The <code>LogReference</code> implementation class
	 */

	public static Class<? extends LogReference> getLogClass (final Class<? extends SubActivity> subactivity)
	{
		assert subactivity != null : "subactivity is NULL";
		assert LogReference.references.containsKey (subactivity) : "subactivity is not registered";

		return LogReference.references.get (subactivity);
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
	 * Compare two <code>LogReference</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>LogReference</code> instance to compare to the one
	 *             represented by the called instance
	 *
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
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return LogReference.METADATA.properties ();
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
		return LogReference.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>LogReference</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return null; // new Builder (Preconditions.checkNotNull (model, "model"))
			// .load (this);
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
