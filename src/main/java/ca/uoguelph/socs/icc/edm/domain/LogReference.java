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

import java.util.Map;

import java.util.HashMap;
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
 * An abstract representation of the relationship between a
 * <code>LogEntry</code> and a <code>SubActivity</code>.  This class acts as
 * the abstract base class for all of the logging related to sub-activities.
 *
 * @author  James E. Stark
 * @version 1.1
 */

public abstract class LogReference extends Element
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** <code>SubActivity</code> to <code>LogReference</code> class mapping */
	private static final Map<Class<? extends SubActivity>, Class<? extends LogReference>> references;

	/** The <code>MetaData</code> for the <code>LogReference</code> */
	protected static final MetaData<LogReference> METADATA;

	/** The <code>DataStore</code> identifier of the <code>LogReference</code> */
	public static final Property<LogReference, Long> ID;

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

		MODEL = Property.of (LogReference.class, DomainModel.class, "domainmodel");
		ENTRY = Property.of (LogReference.class, LogEntry.class, "entry", Property.Flags.REQUIRED);
		SUBACTIVITY = Property.of (LogReference.class, SubActivity.class, "subactivity", Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (LogReference.class, Selector.Cardinality.KEY, ID);
		SELECTOR_ALL = Selector.of (LogReference.class, Selector.Cardinality.MULTIPLE, "all");

		METADATA = MetaData.builder (LogReference.class)
			.addProperty (MODEL, LogReference::getDomainModel, LogReference::setDomainModel)
			.addProperty (ENTRY, LogReference::getEntry, LogReference::setEntry)
			.addProperty (SUBACTIVITY, LogReference::getSubActivity, LogReference::setSubActivity)
			.addRelationship (LogEntry.METADATA, ENTRY, LogEntry.REFERENCE)
			.addRelationship (SubActivity.METADATA, SUBACTIVITY, SubActivity.REFERENCES)
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
		return LogReference.METADATA.getReference (property)
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
		return LogReference.METADATA.getReference (property)
			.stream (this);
	}

	/**
	 * Get an <code>LogReferenceBuilder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an
	 * <code>LogReferenceBuilder</code> on the specified <code>DomainModel</code>
	 * and initializes it with the contents of this <code>LogReference</code>
	 * instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>LogreferenceBuilder</code>
	 */

	@Override
	public LogReferenceBuilder getBuilder (final DomainModel model)
	{
		return null; // new LogReferenceBuilder (Preconditions.checkNotNull (model, "model"))
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
