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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

import ca.uoguelph.socs.icc.edm.domain.metadata.Definition;
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
	/** <code>SubActivity</code> to <code>LogReference</code> class mapping */
	private static final Map<Class<? extends SubActivity>, Class<? extends LogReference>> references;

	/** The associated <code>LogEntry</code>*/
	public static final Property<LogEntry> ENTRY;

	/** The associated <code>SubActivity</code> */
	public static final Property<SubActivity> SUBACTIVITY;

	/** Select an <code>LogReference</code> instance by its <code>LogEntry</code> */
	public static final Selector SELECTOR_ENTRY;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>LogReference</code>.
	 */

	static
	{
		references = new HashMap<> ();

		ENTRY = Property.getInstance (LogEntry.class, "logentry", Property.Flags.REQUIRED);
		SUBACTIVITY = Property.getInstance (SubActivity.class, "subactivity", Property.Flags.REQUIRED);

		SELECTOR_ENTRY = Selector.getInstance (ENTRY, true);

		Definition.getBuilder (LogReference.class, Element.class)
			.addRelationship (ENTRY, LogReference::getEntry, LogReference::setEntry)
			.addRelationship (SUBACTIVITY, LogReference::getSubActivity, LogReference::setSubActivity)
			.addSelector (SELECTOR_ENTRY)
			.build ();
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
	public boolean equals (Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof LogReference)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();

			ebuilder.append (this.getEntry (), ((LogReference) obj).getEntry ());
			ebuilder.append (this.getSubActivity (), ((LogReference) obj).getSubActivity ());

			result = ebuilder.isEquals ();
		}

		return result;
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
		final int base = 1109;
		final int mult = 877;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);

		hbuilder.append (this.getEntry ());
		hbuilder.append (this.getSubActivity ());

		return hbuilder.toHashCode ();
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>LogReference</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>LogReference</code> instance
	 */

	@Override
	public String toString ()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("entry", this.getEntry ());
		builder.append ("subactivity", this.getSubActivity ());

		return builder.toString ();
	}

	/**
	 * Get an <code>LogReferenceBuilder</code> instance for the specified
	 * <code>DataStore</code>.  This method creates an
	 * <code>LogReferenceBuilder</code> on the specified <code>DataStore</code>
	 * and initializes it with the contents of this <code>LogReference</code>
	 * instance.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 *
	 * @return           The initialized <code>LogreferenceBuilder</code>
	 */

	@Override
	public LogReferenceBuilder getBuilder (final DataStore datastore)
	{
		assert datastore != null : "datastore is null";

		return new LogReferenceBuilder (datastore)
			.load (this);
	}

	/**
	 * Get the <code>MetaData</code> instance for this <code>LogEntry</code>
	 * using the specified <code>DataStore</code>.
	 *
	 * @return The <code>MetaData</code>
	 */

	@Override
	protected MetaData<LogReference> getMetaData ()
	{
		return this.getDomainModel ()
			.getDataStore ()
			.getProfile ()
			.getCreator (LogReference.class, this.getClass ());
	}

	/**
	 * Get the parent <code>LogEntry</code> instance.
	 *
	 * @return The parent <code>LogEntry</code> instance
	 */

	public abstract LogEntry getEntry ();

	/**
	 * Set the reference to the parent <code>LogEntry</code>.  This method is
	 * intended to be used by a <code>DataStore</code> when the
	 * <code>LogEntry</code> instance is loaded.
	 *
	 * @param  entry The parent <code>LogEntry</code> instance, not null
	 */

	protected abstract void setEntry (LogEntry entry);

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
	 * by a <code>DataStore</code> when the <code>LogEntry</code> instance is
	 * loaded.
	 *
	 * @param  subactivity The <code>SubActivity</code>, not null
	 */

	protected abstract void setSubActivity (SubActivity subactivity);
}
